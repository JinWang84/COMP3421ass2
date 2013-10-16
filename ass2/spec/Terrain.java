package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;


//import sailing.objects.Island;

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
    boolean drawTriangle = false;
    
    /**
     * �ӵ��浽����·��Ҫ������
     * @param gl
     */
    public void draw(GL2 gl, Texture groundTexture, Texture treeTexture, Texture roadTexture){
    	//gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  
        //����ģ�͹۲����  
    	
    	if(debug) {
			System.out.println("HEIGHT is "+mySize.getHeight() +" width is "+ mySize.getWidth());
		}
		
		
    	//������
    	float textureTop, textureBottom, textureLeft, textureRight;
    	TextureCoords textureCoords = groundTexture.getImageTexCoords();
        textureTop = textureCoords.top();
        textureBottom = textureCoords.bottom();
        textureLeft = textureCoords.left();
        textureRight = textureCoords.right();
        
        // Enables this texture's target in the current GL context's state.
        groundTexture.enable(gl);  // same as gl.glEnable(texture.getTarget());
        // gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        // Binds this texture to the current GL context.
        groundTexture.bind(gl);  // same as gl.glBindTexture(texture.getTarget(), texture.getTextureObject());
    	
    	for(int i = 0; i< mySize.getHeight()-1 ; i++) { //loop from 1 to 9 
    		for(int y = 0; y< mySize.getWidth()-1; y++) {
    			if(debug) {
    				System.out.println("i:"+i+" y:"+y+" altitute:"+this.getGridAltitude(i, y));
    			}
    			//�����������ʱ��
    			gl.glColor3d(102/255d, 102/255d, 0.0);
    			/** 
    	         * ����������ĳ�����ʹ�ö��������Ӧ��ʹ���� 
    	         *  glBindTexture(GL_TEXTURE_2D, texture[ ��ʹ�������Ӧ������ ]) 
    	         * ѡ��Ҫ�󶨵�����������ı�����ʱ��Ӧ�ð��µ����� 
    	         * ��һ��ֵ��ָ�����ǣ��������� glBegin() �� glEnd() ֮������� 
    	         * ������ glBegin() ֮ǰ�� glEnd() ֮��󶨡� 
    	         */  
    	       // gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);  
	    	        if(!drawTriangle){
	    			gl.glBegin(gl.GL_QUADS);
	    			/*double[] a = {i,this.getGridAltitude(i, y),y};
	    			double[] b = {i,this.getGridAltitude(i, y+1),y+1};
	    			double[] c = {i+1,this.getGridAltitude(i+1, y+1),y+1};
	    			double[] n = MathUtil.normal(a, b, c);
	    			*/
	    			//gl.glNormal3d(0, 1, 0);
	    			gl.glTexCoord2f(textureLeft, textureBottom);//����
	    			gl.glVertex3d(i,this.getGridAltitude(i, y),y);
	    			gl.glTexCoord2f(textureRight, textureBottom); //����
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1),y+1);
	    			gl.glTexCoord2f(textureRight, textureTop);//����
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1, y+1),y+1);
	    			gl.glTexCoord2f(textureLeft, textureTop);//����
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y),y);
	    			gl.glEnd();
    	        } else {
	    			gl.glBegin(gl.GL_TRIANGLES);
	    			gl.glTexCoord2f(textureLeft, textureBottom);//����
	    			gl.glVertex3d(i,this.getGridAltitude(i, y),y);
	    			gl.glTexCoord2f(textureRight, textureBottom); //����
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1),y+1);
	    			gl.glTexCoord2f(textureLeft, textureTop);//����
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y),y);
	    			gl.glEnd();
	    			
	    			gl.glBegin(gl.GL_TRIANGLES);
	    			gl.glTexCoord2f(textureRight, textureBottom); //����
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1),y+1);
	    			gl.glTexCoord2f(textureRight, textureTop);//����
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1, y+1),y+1);
	    			gl.glTexCoord2f(textureLeft, textureTop);//����
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y),y);
	    			gl.glEnd();
    	        }
    			
    			if(!drawTriangle){
	    			//����������,polygonoffset���񲻶�?
	    			gl.glColor3d(0.0, 0.0, 0.0);
	    			//�����������ʱ��
	    			gl.glBegin(gl.GL_LINE_LOOP);
	    			gl.glVertex3d(i,this.getGridAltitude(i, y)+0.01,y);
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1)+0.01,y+1);
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1, y+1)+0.01,y+1);
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y)+0.01,y);
	    			gl.glEnd();
	    			gl.glDisable( GL.GL_POLYGON_OFFSET_FILL ); 
    			} else {
    				gl.glColor3d(0.0, 0.0, 0.0);
    				gl.glBegin(gl.GL_LINE_LOOP);
	    			gl.glVertex3d(i,this.getGridAltitude(i, y),y);
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1),y+1);
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y),y);
	    			gl.glEnd();
	    			
	    			gl.glBegin(gl.GL_LINE_LOOP);
	    			gl.glVertex3d(i,this.getGridAltitude(i, y+1)+0.01,y+1);
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1, y+1)+0.01,y+1);
	    			gl.glVertex3d(i+1,this.getGridAltitude(i+1,y)+0.01,y);
	    			gl.glEnd();
    			}
    		}
    	}
    	drawAvatar(gl);
    	drawTrees(gl,treeTexture);
    	//ÿ��drawtree����gl.glPopMatrix();�������ڻص��˵�ͼԭ��
    	//gl.glPopMatrix();
    	drawRoad(gl, roadTexture);
    }
    
    public Avatar getAvatar(){
    	return avatar;
    }
    
    private Avatar avatar = new Avatar(this);
    public void drawAvatar(GL2 gl){    	
    	avatar.draw(gl);
    }
    
    public void drawTrees(GL2 gl,Texture TreeTexture){
    	for(Tree tree : myTrees){
    		tree.draw(gl,TreeTexture);
    	}
    }
    
    public void drawRoad(GL2 gl,Texture texture){
    	for(Road road: myRoads){
    		road.draw(gl, texture);
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
    	if(zu!=z && x!=xu){
	    	double r1 = Math.abs((xu-x)*getGridAltitude(xd, zd)) + Math.abs((x-xd)*getGridAltitude(xu,zd));
	    	double r2 = Math.abs((xu-x)*getGridAltitude(xd, zu)) +  Math.abs((x-xd)*getGridAltitude(xu,zu));
	        altitude = (zu-z)*r1+(z-zd)*r2;
	        if(debug) {
	    		System.out.println("r1: "+r1);
	    	}
    	} else if(zu == z && xu != x){ //��z������ʱ
    		double r1 = Math.abs((xu-x)*getGridAltitude(xd, zd)) + Math.abs((x-xd)*getGridAltitude(xu,zd));
    		return r1;
    	} else if(xu == x && zu != z){ //��x������ʱ
    		double r1 = Math.abs((zu-z)*getGridAltitude(xd, zd)) + Math.abs((z-zd)*getGridAltitude(xd,zu));
    		return r1;
    	} else if(xu == x && zu == z){ //��x z��������ʱ
    		return getGridAltitude((int)x,(int)z);
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
        //gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  
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
