package ass2.spec;

import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JLabel;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class Camera implements GLEventListener {
	final public boolean debug = true;
	double xtrans = 0.0;
	double ytrans = 0.0;
	double ztrans = 0.0;
	int yrotate = 0;
	Terrain myTerrain;
	GLU glu = new GLU();
	float ambient = 0.7f;
	float diffuse = 1.5f;
	float specular = 0.3f;
	JLabel myLabel;
	boolean lightEnable = true;
	double cameraAvatarDistance = 5.0;
	boolean flipped = false;

	public Camera(Terrain theTerrain, JLabel label) {
		myTerrain = theTerrain;
		myLabel = label;
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		if (debug) {
			System.out.println("display method working");
		}
		// clear both the colour buffer and the depth buffer
		// ���ñ���Ϊ����ɫ
		gl.glClearColor(0.0f, 245 / 255f, 1.0f, 0.5f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		double avatarRadius = avatarAngle * Math.PI / 180.0;
		double eyex = myTerrain.getAvatar().x - Math.sin(avatarRadius)
				* cameraAvatarDistance;
		double eyey = myTerrain.getAvatar().getY() + 3.0;
		double eyez = myTerrain.getAvatar().z - Math.cos(avatarRadius)
				* cameraAvatarDistance;

		if (!flipped) {
			glu.gluLookAt(eyex, eyey, eyez, myTerrain.getAvatar().x, myTerrain
					.getAvatar().getY(), myTerrain.getAvatar().z, 0, 1, 0);
		} else {
			glu.gluLookAt(eyex, -eyey, eyez, myTerrain.getAvatar().x, myTerrain
					.getAvatar().getY(), myTerrain.getAvatar().z, 0, -1, 0);
		}
		gl.glPushMatrix();

		if (lightEnable) {
			setLighting(gl);
		}

		// ����GL�ڵ�ͼӦ��λ��
		drawShape(gl);
		myLabel.setText("<HTML>Avatar Position<P>X:" + myTerrain.getAvatar().x
				+ "<P>Y:" + myTerrain.getAvatar().y + "<P>Z:"
				+ myTerrain.getAvatar().z + "<P>angle:" + avatarAngle
				+ "<P>Light:" + "<P>Sunlight direction:"
				+ myTerrain.getSunlight()[0] + " " + myTerrain.getSunlight()[1]
				+ " " + myTerrain.getSunlight()[2] + " " + "<P>luminance"
				+ "<P>ambient " + ambient + "<P>diffuse" + diffuse +

				"</HTML>");
	}

	public double sunRotate = 0.0;
	public int time = 14; // 0~24, 25frame per hour in world
	public double[] lightDirAt8 = { -1, 0, 0, 1 };

	private double[] getLightDirection() {
		double angle = (time - 8) / 24 * 360.0;
		double[] lightDir = MathUtil
				.multiply(MathUtil.rotationMatrix(angle, false, false, true),
						lightDirAt8);
		return lightDir;
	}

	private void setLighting(GL2 gl) { // TODO �˴���Ҫ���� ����̫��λ����sunlight
		gl.glPushMatrix();
		/*
		if(time >= 24){
			time = 0;
		}
		*/
		time += 1/25;
		sunRotate += 0.6;
		gl.glRotated(sunRotate, 0.0, 0.0, 1.0); // ����̫������ת,working now
		gl.glShadeModel(GL2.GL_SMOOTH);
		if (debug) {
			System.out.println("LIghting lumi:ambient " + ambient + " diffuse "
					+ diffuse );
		}
		float[] a = new float[4];
		a[0] = a[1] = a[2] = ambient;
		a[3] = 1.0f;
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, a, 0); // a������,λ�ú�LIGHT0����һ����
		float[] ambientPos = new float[] { 1.0f, 0.0f, 1.0f, 1.0f }; // ���һ����ֵ,1��λ��,0�Ƿ���
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, ambientPos, 0);

		float[] d = new float[4];
		d[0] = d[1] = d[2] = diffuse;
		d[3] = 1.0f;
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, d, 0);
		float[] diffusePos = new float[] { 1.0f, 5.0f, 5.0f, 0.0f }; // ������
		diffusePos[0] = myTerrain.getSunlight()[0];
		diffusePos[1] = myTerrain.getSunlight()[1];
		diffusePos[2] = myTerrain.getSunlight()[2];
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, diffusePos, 0);
		gl.glPopMatrix();
	}

	// �����Ļ�ͼ�ķ���������terrian�ڲ�����tree֮��ķ���������ֻҪ����terrian�ͺ���
	private void drawShape(GL2 gl) {
		// myTerrain.triangleTest(gl);
		drawCoor(gl);
		myTerrain.draw(gl, groundTexture, treeTexture, roadTexture);

	}

	private void drawCoor(GL2 gl) {
		gl.glColor3f(0.0f, 0.0f, 1.0f);// blue
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glBegin(gl.GL_LINES);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(30.0, 0.0, 0.0);
		gl.glColor3d(0.0, 1.0, 0.0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 30, 0);
		gl.glColor3d(0.0, 0.0, 1.0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 30);
		gl.glEnd();
	}

	Texture groundTexture;
	Texture treeTexture;
	Texture leafTexture;
	Texture roadTexture;

	private void setTexture(GL2 gl) {
		// Load texture from image
		try {
			// Create a OpenGL Texture object from (URL, mipmap, file suffix)
			// Use URL so that can read from JAR and disk file.
			groundTexture = TextureIO.newTexture(getClass().getClassLoader()
					.getResource("ground.jpg"), // relative to project root
					false, ".jpg");
			treeTexture = TextureIO.newTexture(getClass().getClassLoader()
					.getResource("tree.jpg"), // relative to project root
					false, ".jpg");
			roadTexture = TextureIO.newTexture(getClass().getClassLoader()
					.getResource("road.jpg"), // relative to project root
					false, ".jpg");

			// Use linear filter for texture if image is larger than the
			// original texture
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			// Use linear filter for texture if image is smaller than the
			// original texture
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GLAutoDrawable arg0) { // ��ȡGL����
		GL2 gl = arg0.getGL().getGL2();
		gl.glLoadIdentity();
		if (lightEnable) {

		}
		// ��������ӳ��
		gl.glEnable(GL2.GL_TEXTURE_2D);
		// ���û��
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL.GL_ONE);
		// ������ɫ���
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		// gl.glColorMaterial( gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT_AND_DIFFUSE
		// );
		// ������Ӱƽ�� /���ø�˹ģ��
		gl.glShadeModel(GL2.GL_SMOOTH);
		// ���ñ�����ɫΪ��ɫ
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// ������Ȼ���
		gl.glClearDepth(1.0f);
		// ������Ȳ���
		gl.glEnable(GL.GL_DEPTH_TEST);
		// ��������Ȳ��Ե�����
		gl.glDepthFunc(GL.GL_LEQUAL);
		// ����ϵͳ��͸�ӽ�������
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		// Really Nice Perspective Calculations
		setTexture(gl);

	}

	/**
	 * ���ڱ任��С
	 */
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width,
			int height) {
		final GLU glu = new GLU();
		if (debug) {
			System.out.println("reshape method working");
		}

		final GL2 gl = glDrawable.getGL().getGL2();

		// enable depth testing
		gl.glEnable(GL.GL_DEPTH_TEST);
		if (lightEnable) {
			// enable lighting, turn on light 0
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_LIGHT0);
			gl.glEnable(GL2.GL_LIGHT1);
			// normalise normals (!)
			// this is necessary to make lighting work properly
			gl.glEnable(GL2.GL_NORMALIZE);
		}
		// ��ֹ�����
		if (height <= 0) // avoid a divide by zero error!
			height = 1;
		final float h = (float) width / (float) height;
		// �����Ӵ��Ĵ�С
		gl.glViewport(0, 0, width, height);
		// ѡ��ͶӰ���� ,ͶӰ������Ϊ���ǵĳ�������͸�ӡ�
		gl.glMatrixMode(GL2.GL_PROJECTION);
		// ����ͶӰ����;
		gl.glLoadIdentity();
		// �����ӿڵĴ�С
		// glu.gluOrtho2D(1.0, 1.0, 1.0, 1.0);�����ƽ�еģ�ô��͸��
		glu.gluPerspective(45.0f, h, 1.0, 50.0); // --�۾������ĽǶ� ������������Զ
		// TODO ���֪��1.0����������ռ�Ĵ�С�ģ�Ҫ���ٲŻᳬ�����أ�
		// ����ģ�͹۲����ģ�͹۲�����д�������ǵ�����ѶϢ��
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void rotateRight() {
		if (debug) {
			System.out.println("keyboard: Rotata Right");
		}
		yrotate += 10;

	}

	public void rotateLeft() {
		if (debug) {
			System.out.println("keyboard: Rotate LEft");
		}
		yrotate += -10;
		if (flipped == true) {
			flipped = false;
		} else {
			flipped = true;
		}
	}

	public void stepLeft() {
		if (debug) {
			System.out.println("keyboard: Step Left");
		}
		xtrans += -0.1;
	}

	public void stepRight() {
		if (debug) {
			System.out.println("keyboard: Step Right");
		}
		xtrans += 0.1;
	}

	/**
	 * The camera should move up and down following the terrain. So if you move
	 * it forward up a hill, the camera should move up the hill.
	 * ��ͷҪ����ɽ���ƶ�(��Զ��ɽ���Ϸ�һ������,�������ɽ��С��ĳ������,���Զ���̧)
	 */
	public void stepForward() {
		if (debug) {
			System.out.println("keyboard: Step Forward");
		}
		ztrans += 0.1;

	}

	public void stepBackward() {
		if (debug) {
			System.out.println("keyboard: Step Backward");
		}
		ztrans += -0.1;
	}

	public void ambientUp() {
		ambient += 0.1f;
	}

	public void ambientDown() {
		ambient += -0.1f;
	}

	public void diffuseUp() {
		diffuse += 0.1f;
	}

	public void diffuseDown() {
		diffuse += -0.1f;
	}

	public void specularUp() {
		specular += 0.1f;
	}

	public void specularDown() {
		specular += -0.1f;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	public double avatarAngle = 0.0;
	public double angleStep = 10.0;

	public void avatarStepForward() {
		myTerrain.getAvatar().stepForawrd(avatarAngle);
	}

	public void avatarStepBorward() {
		myTerrain.getAvatar().stepBackward(avatarAngle);
	}

	public void avatarRotateCCW() {
		avatarAngle += angleStep;
	}

	public void avatarRotateCW() {
		avatarAngle -= angleStep;
	}

}
