package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import sailing.objects.Island;

/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    final public boolean debug = true;
    
    
    /**
     * �ӵ��浽����·��Ҫ������
     * @param gl
     */
    public void draw(GL2 gl){
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  
        //����ģ�͹۲����  
    	gl.glColor3d(0.0, 0.0, 0.0);
    	if(debug) {
			System.out.println("HEIGHT is "+mySize.getHeight() +" width is "+ mySize.getWidth());
		}
    	//������
    	for(int i = 0; i< mySize.getHeight()-1 ; i++) { //loop from 1 to 9 
    		for(int y = 0; y< mySize.getWidth()-1; y++) {
    			if(debug) {
    				System.out.println("i:"+i+" y:"+y+" altitute:"+this.getGridAltitude(i, y));
    			}
    			//�����������ʱ��
    			gl.glBegin(gl.GL_LINE_LOOP);
    			gl.glVertex3d(i,this.getGridAltitude(i, y),y);
    			gl.glVertex3d(i,this.getGridAltitude(i, y+1),y+1);
    			gl.glVertex3d(i+1,this.getGridAltitude(i+1, y+1),y+1);
    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y),y);
    			gl.glEnd();
    		}
    	}
    	drawTrees(gl);
    	//ÿ��drawtree����gl.glPopMatrix();�������ڻص��˵�ͼԭ��
    	//gl.glPopMatrix();
    	drawRoad(gl);
    }
    
    public void drawTrees(GL2 gl){
    	for(Tree tree : myTrees){
    		tree.draw(gl);
    	}
    }
    
    public void drawRoad(GL2 gl){
    	for(Road road: myRoads){
    		road.draw(gl);
    	}
    }
    
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        int xu = (int) Math.ceil(x);
    	int xd = (int) Math.floor(x);
    	int zu = (int) Math.ceil(z);
    	int zd = (int) Math.floor(z);
    		
    	double r1 = (xu-x)*getGridAltitude(xd, zd) + (x-xd)*getGridAltitude(xu,zd);
    	double r2 = (xu-x)*getGridAltitude(xd, zu) + (x-xd)*getGridAltitude(xu,zu);
        altitude = (zu-z)*r1+(z-zd)*r2;
        if(debug) {
    		System.out.println("r1: "+r1);
    	}
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z,this);
        myTrees.add(tree);
    }

    

    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine,this);
        myRoads.add(road);        
    }

    
    
    
    
    
    
    
    
    

    
    public void triangleTest(GL2 gl){
    	//���е��涼Ҫ������ʱ����ƣ���//������Ļ����Ȼ���  
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  
        //����ģ�͹۲����  
        gl.glLoadIdentity();  
        //��������������1.5����λ������Ļ������6����λ  
        gl.glTranslatef(-1.0f, 0.0f, -6.0f);  
        //������ת�ᣬ��Y��Ϊ��ת����תrtri��  
        gl.glRotatef(30, 45, 1.0f, 0.0f);  
         /** 
         * ����Ĵ��봴��һ����������������ת�Ľ������� 
         * ���������϶���߳�ԭ��һ����λ���������ĵ���ԭ��һ����λ�� 
         * �϶����ڵ����ͶӰλ�ڵ��������. 
         * ע�����е��棭�����ζ�����ʱ�������Ƶġ� 
         * ���ʮ����Ҫ�����Ժ�Ŀγ����һ��������͡� 
         * ���ڣ���ֻ������Ҫô����ʱ�룬Ҫô��˳ʱ�룬 
         * ����Զ��Ҫ�����ִ������һ�𣬳��������㹻�����ɱ�����ô���� 
         * �������ǿ�ʼ���ƽ������ĸ����� 
         */  
        gl.glBegin(GL2.GL_TRIANGLES);           // Drawing Using Triangles  
        /** 
         * ��ʼ���ƽ������ĵ�ǰ���� 
         */  
        //���õ�ǰ����ɫΪ��ɫ������ǰ������϶���  
        gl.glColor3f(1.0f, 0.0f, 0.0f);           
        gl.glVertex3f(0.0f, 1.0f, 0.0f);              
       //���õ�ǰ����ɫΪ��ɫ���������ǰ������󶥵�  
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);  
        //���õ�ǰ����ɫΪ��ɫ,����ǰ������Ҷ���    
        gl.glVertex3f(1.0f, -1.0f, 1.0f);  
          
        /** 
         * ���ƽ��������Ҳ��� 
         * ������Ⱦ��ɫΪ��ɫ����ǰ�ĵ�Ϊ�Ҳ�����϶��� 
         */  
        gl.glColor3f(0.0f, 1.0f, 0.0f);           
        gl.glVertex3f(0.0f, 1.0f, 0.0f);  
        //���õ�ǰ����ɫΪ��ɫ����ǰ�ĵ�Ϊ�Ҳ�����󶥵�     
        gl.glVertex3f(1.0f, -1.0f, 1.0f);  
        //���õ�ǰ����ɫΪ��ɫ����ǰ�ĵ�λ�Ҳ����Ҷ���  
        gl.glVertex3f(1.0f, -1.0f, -1.0f);  
  
        /** 
         * ���ƽ������ı��� 
         * ���õ�ǰ��Ⱦ��ɫΪ��ɫ����ǰ�ĵ�λ������϶��� 
         */  
        gl.glColor3f(0.0f, 0.0f, 1.0f);           
        gl.glVertex3f(0.0f, 1.0f, 0.0f);  
         //���õ�ǰ����ɫΪ��ɫ����ǰ�ĵ�Ϊ������󶥵�   
        gl.glVertex3f(1.0f, -1.0f, -1.0f);  
        //���õ�ǰ����ɫΪ��ɫ����ǰ�ĵ�Ϊ������Ҷ���  
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);  
  
        /** 
         * ���ƽ������������ 
         * ������ɫΪ��ɫ�������������϶��� 
         */  
        gl.glColor3f(1.0f, 1.0f, 0.0f);           
        gl.glVertex3f(0.0f, 1.0f, 0.0f);  
        //������ɫΪ��ɫ�������������󶥵�       
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);  
        //������ɫΪ��ɫ�������������Ҷ���       
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);  
        //��ɽ������Ļ���  
        gl.glEnd();  
        //����ģ�͹۲����  
        gl.glLoadIdentity();  

    }
    

}