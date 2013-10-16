package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Keyboard implements KeyListener{


	Camera myCamera;
	/**
	 * ���Ƽ��̣��������ҷֱ����ǰ�� ���� ��ƽ�� ��ƽ��
	 */
	public Keyboard (Camera camera){
		myCamera = camera;
	}
	
	/**
	 * ��������teapot.class
	 */
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		 switch (e.getKeyCode()) {
	        case KeyEvent.VK_SPACE:
	           // myWireframe = !myWireframe;
	            break;

	        case KeyEvent.VK_LEFT:
	        	myCamera.stepLeft();
	            break;

	        case KeyEvent.VK_RIGHT:
	        	myCamera.stepRight();
	            break;

	        case KeyEvent.VK_UP:
	        	myCamera.stepForward();
	            break;

	        case KeyEvent.VK_DOWN:
	        	myCamera.stepBackward();
	            break;
	        
			case KeyEvent.VK_PAGE_DOWN:
				myCamera.rotateLeft();
	        	break;
	    
			case KeyEvent.VK_PAGE_UP:
				myCamera.rotateRight();
				break;
			case KeyEvent.VK_W:
				myCamera.avatarStepForward();
				break;
			case KeyEvent.VK_S:
				myCamera.avatarStepBorward();
				break;
			case KeyEvent.VK_A:
				myCamera.avatarRotateCCW();
				break;
			case KeyEvent.VK_D:
				myCamera.avatarRotateCW();
				break;
				
				
			case KeyEvent.VK_Z:
				myCamera.ambientUp();
				break;
			case KeyEvent.VK_X:
				myCamera.ambientDown();
				break;
			case KeyEvent.VK_C:
				myCamera.diffuseUp();
				break;
			case KeyEvent.VK_V:
				myCamera.diffuseDown();
				break;
			case KeyEvent.VK_B:
				myCamera.specularUp();
				break;
			case KeyEvent.VK_N:
				myCamera.specularDown();
				break;
		 }		
	}
}
