package ass2.spec;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

public class Camera implements GLEventListener{
	final public boolean debug = true;
	double xtrans = 0.0;
	double ytrans = 0.0;
	double ztrans = 0.0;
	int yrotate = 0;
	Terrain myTerrain;


	public Camera(Terrain theTerrain){
		myTerrain = theTerrain;
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		if(debug) {
			System.out.println("display method working");
		}
		// clear both the colour buffer and the depth buffer
		//���ñ���Ϊ����ɫ
		gl.glClearColor(0.0f, 245/255f, 1.0f, 0.5f);  
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		//���Ҫ�ı�cameraλ�ã�����ֱ��gl.glrotate
		gl.glTranslated(xtrans-2.0, ytrans+1, ztrans-20); 
        gl.glRotatef(30, 1.0f, 0.0f, 0.0f); 
        gl.glRotatef(-30+yrotate, 0.0f, 1.0f, 0.0f); 
        gl.glPushMatrix();
        //����GL�ڵ�ͼӦ��λ��
		drawShape(gl);
	}

	
	
	//�����Ļ�ͼ�ķ���������terrian�ڲ�����tree֮��ķ���������ֻҪ����terrian�ͺ���
	private void drawShape(GL2 gl){
		//myTerrain.triangleTest(gl);
		myTerrain.draw(gl);
	}


	@Override
	public void init(GLAutoDrawable arg0) {   //��ȡGL����  
		GL2 gl = arg0.getGL().getGL2();  
		gl.glLoadIdentity();
		GLU glu = new GLU();
		
		
		
		
		//������Ӱƽ��  
		gl.glShadeModel(GL2.GL_SMOOTH);  
		//���ñ�����ɫΪ��ɫ
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  
		//������Ȼ���  
		gl.glClearDepth(1.0f);  
		//������Ȳ���  
		gl.glEnable(GL.GL_DEPTH_TEST);  
		//��������Ȳ��Ե�����  
		gl.glDepthFunc(GL.GL_LEQUAL);  
		// ����ϵͳ��͸�ӽ�������  
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);   
		// Really Nice Perspective Calculations  

	}

	/**
	 * ���ڱ任��С
	 */
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {  
		final GLU glu = new GLU();  
		if(debug) {
			System.out.println("reshape method working");
		}
		final GL2 gl = glDrawable.getGL().getGL2();  
		//��ֹ�����  
		if (height <= 0) // avoid a divide by zero error!  
			height = 1;  
		final float h = (float) width / (float) height;  
		//�����Ӵ��Ĵ�С  
		gl.glViewport(0, 0, width, height);  
		//ѡ��ͶӰ���� ,ͶӰ������Ϊ���ǵĳ�������͸�ӡ�  
		gl.glMatrixMode(GL2.GL_PROJECTION);  
		//����ͶӰ����;  
		gl.glLoadIdentity();  
		//�����ӿڵĴ�С  
		//glu.gluOrtho2D(1.0, 1.0, 1.0, 1.0);�����ƽ�еģ�ô��͸��
		glu.gluPerspective(45.0f, h, 1.0, 40.0); //--�۾������ĽǶ� ������������Զ
		//TODO ���֪��1.0����������ռ�Ĵ�С�ģ�Ҫ���ٲŻᳬ�����أ�
		//����ģ�͹۲����ģ�͹۲�����д�������ǵ�����ѶϢ��  
		gl.glMatrixMode(GL2.GL_MODELVIEW);  
		gl.glLoadIdentity();  
	}


	public void rotateRight() {
		if(debug){
			System.out.println("keyboard: Rotata Right");
		}
		yrotate += 10;		
	}
	public void rotateLeft() {
		if(debug){
			System.out.println("keyboard: Rotate LEft");
		}
		yrotate += -10;		
	}

	public void stepLeft() {
		if(debug){
			System.out.println("keyboard: Step Left");
		}
		xtrans += -0.1;
	}

	public void stepRight() {
		if(debug){
			System.out.println("keyboard: Step Right");
		}
		xtrans += 0.1;		
	}
/**
 * The camera should move up and down following the terrain. So if you move it forward up a hill, the camera should move up the hill.
 * ��ͷҪ����ɽ���ƶ�(��Զ��ɽ���Ϸ�һ������,�������ɽ��С��ĳ������,���Զ���̧)
 */
	public void stepForward() {
		if(debug){
			System.out.println("keyboard: Step Forward");
		}
		ztrans += 0.1;
		
	}

	public void stepBackward() {
		if(debug){
			System.out.println("keyboard: Step Backward");
		}
		ztrans += -0.1;
	}  


	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

}
