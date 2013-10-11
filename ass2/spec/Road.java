package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
//import javax.vecmath.Matrix3d;
/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    public final boolean debug = true;
    private Terrain myTerrain;
    
    public double altitude(double x, double z){
    	return myTerrain.altitude(x, z);
    }
    
    public void draw(GL2 gl, Texture texture){
    	
    	// 1 �ظ���t=0~1��ÿ�����x , z -> points[2]
    	
    	
    	// 2 ͨ�� x, z ���altitude -> y
    	// 3 ���t=0~0.1֮����������� ��ʾΪtangent[3],�����t=0������,��t=0.1���޹�
    	// 4 ����tangent��ת90��(��rotate Matrix)
    	// 5 ����tangent scale ���ȵ�1/2 (��scale matrix)
    	// 6 ��vector+ (x,y,z)�ȵ�·�ص�����
    	
    	// 7 �ظ�����ֱ�����t=0.9����������,ȡt=0.9~1������,���t=1.0�����ߵ���t=0.9������
    	double[] scaleRotateTangent = new double[4];
    	gl.glColor3d(110/255d, 110/255d, 110/255d);
    	
    	float textureTop, textureBottom, textureLeft, textureRight;
    	TextureCoords textureCoords = texture.getImageTexCoords();
        textureTop = textureCoords.top();
        textureBottom = textureCoords.bottom();
        textureLeft = textureCoords.left();
        textureRight = textureCoords.right();
        
     // Enables this texture's target in the current GL context's state.
        texture.enable(gl);  // same as gl.glEnable(texture.getTarget());
        // gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        // Binds this texture to the current GL context.
        texture.bind(gl);  // same as gl.glBindTexture(texture.getTarget(), texture.getTextureObject());
    	
        
    	
    	
    	gl.glBegin(gl.GL_QUAD_STRIP);
    	
    	
    	for(double t = 0.1 ; t <= 0.9; t += 0.1){ //t=0.0��t=1.0�ĵ�����
    		double[] point0 = point(t);
    		double x0 = point0[0];
    		double z0 = point0[1];
    		double y0 = altitude(x0, z0)+0.01;
    		double[] midpoint = {x0,y0,z0};
    		
    		double t1 = t+0.1;
    		double[] point1 = point(t1);
    		double x1 = point1[0];
    		double z1 = point1[1];
    		double y1 = altitude(x1,z1)+0.01;
    		
    		double[] tangent = {x1-x0, y1-y0, z1-z0,1}; //for matrix calculation
    		if(debug){
    			System.out.println("In Road: t is "+t+" point is "+ x0+" "+ y0+" "+ z0+" ");
        		
    			System.out.println(" Tangent is "+ tangent[0]+" "+ tangent[1]+" "+ tangent[2]+" ");
    		}
    		
    		double[] rotateTangent = MathUtil.multiply(MathUtil.rotationMatrix(90,false,true,false), tangent); 
    		
    		scaleRotateTangent = MathUtil.multiply(MathUtil.scaleMatrix(myWidth/2), rotateTangent);
    		double[] result0 = {scaleRotateTangent[0]+midpoint[0],scaleRotateTangent[1]+midpoint[1],scaleRotateTangent[2]+midpoint[2]};
    		double[] result1 = {-scaleRotateTangent[0]+midpoint[0],-scaleRotateTangent[1]+midpoint[1],-scaleRotateTangent[2]+midpoint[2]};
    		gl.glTexCoord2f(textureLeft, textureBottom);
    		gl.glVertex3d(result0[0], result0[1], result0[2]);
    		gl.glTexCoord2f(textureRight, textureBottom);
    		gl.glVertex3d(result1[0], result1[1], result1[2]); 	    		
			gl.glTexCoord2f(textureRight, textureTop);
			gl.glVertex3d(scaleRotateTangent[0]+x1, scaleRotateTangent[1]+y1, scaleRotateTangent[2]+z1);
			gl.glTexCoord2f(textureLeft, textureTop);
			gl.glVertex3d(-scaleRotateTangent[0]+x1, -scaleRotateTangent[1]+y1, -scaleRotateTangent[2]+z1);
			if(debug){
    			System.out.println("In Road: +side "+ result0[0]+" "+ result0[1]+" "+ result0[2]+" ");
        		
    			System.out.println("In Road: -side "+ result1[0]+" "+ result1[1]+" "+ result1[2]+" ");
    		}
    	}
    	gl.glEnd();
    	
    	
    	//��t=1.0ʱ
    	/*
    	double[] point0 = point(1.0);
		double x0 = point0[0];
		double z0 = point0[1];
		double y0 = altitude(x0, z0);
		double[] midpoint = {x0,y0,z0};
    	double[] result0 = {scaleRotateTangent[0]+midpoint[0],scaleRotateTangent[1]+midpoint[1],scaleRotateTangent[2]+midpoint[2]};
		double[] result1 = {-scaleRotateTangent[0]+midpoint[0],-scaleRotateTangent[1]+midpoint[1],-scaleRotateTangent[2]+midpoint[2]};
		gl.glVertex3d(result0[0], result0[1], result0[2]);
		gl.glVertex3d(result1[0], result1[1], result1[2]);
    	*/
    	
    	
    	/*
    	double[] pPre =  new double[2];
    	pPre[0] = myPoints.get(0);
    	pPre[1] = myPoints.get(1);
    	double pPreAl = myTerrain.altitude(pPre[0],pPre[1]);    	
    	if(debug){
    		System.out.println("pPre :"+pPre[0]+" "+pPre[1]+" "+pPreAl);
    	}
    	gl.glColor3d(1.0, 1.0, 1.0);
    	gl.glBegin(gl.GL_QUAD_STRIP);
    	for(double i = 0.0; i<=1.0; i += 0.1){
    		double[] p = point(i);
    		
    		//��ʱ��������:point�ڵĵ���flat,��xu,xd,zu,zd�����,��point��y�Ǹ�ƽ���altitude
    		//����,ֱ�Ӽ���road���ڵ�ÿ�����λ��,�ò�ֵ�㷨
    		//����·���п�ȵ�,������һ��quad��ʾ??��α�ʾ??
    		double pAl = myTerrain.altitude(p[0], p[1]);
    		if(debug){
        		System.out.println("point is "+ point(i)[0]+ " "+ point(i)[1]+" "+pAl);
        	}		    		
    		gl.glVertex3d(pPre[0]-myWidth/2, pPreAl, pPre[1]);
    		gl.glVertex3d(pPre[0]+myWidth/2, pPreAl, pPre[1]);
    		gl.glVertex3d(p[0]+myWidth/2, pAl, p[1]);
    		gl.glVertex3d(p[0]-myWidth/2, pAl, p[1]);
    		*/
    		/*
    		gl.glVertex3d(pPre[0], pPreAl, pPre[1]);
    		gl.glVertex3d(pPre[0], pPreAl, pPre[1]);
    		gl.glVertex3d(p[0], pAl, p[1]);
    		gl.glVertex3d(p[0], pAl, p[1]);
    		*/
    	/*
    		pPre[0] = p[0];
    		pPre[1] = p[1];
    		pPreAl = pAl;
    		
    		if(debug){
        		System.out.println("now pPre is "+ pPre[0]+ " "+ pPre[1]+" "+pPreAl);
        	}
    	}
    	gl.glEnd();
		gl.glFlush();
    	*/
    }
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width/8;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine, Terrain terrain) {
    	myTerrain = terrain;
        myWidth = width/8;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }


}
