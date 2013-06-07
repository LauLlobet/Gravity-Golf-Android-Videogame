package IO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import milkyway.logica.LogicaGeneral;


public class InputAdapter extends MouseInputAdapter implements KeyListener{
    
	

	LogicaGeneral _logica;
	public InputAdapter(LogicaGeneral logica){
		_logica = logica;
	}
	
	public void keyTyped(KeyEvent e) {
        _logica.keyTyped(e.getKeyChar()); 
    }

    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    
	
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		_logica.fingerPressed(e.getX(),e.getY()); 
	}

	public void mouseReleased(MouseEvent e) {
		_logica.fingerRelased(e.getX(),e.getY()); 
	}

	public void mouseDragged(MouseEvent e) {
		  _logica.fingerDraged(e.getX(),e.getY());
	}
	
	public void mouseMoved(MouseEvent e) {
	
	}

	  
	  
}
