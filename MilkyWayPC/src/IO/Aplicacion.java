package IO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.*;

import milkyway.Mesa;
import milkyway.geometria.Vector2D;
import milkyway.logica.ResManager;

import org.xml.sax.InputSource;




public class Aplicacion
{
	Mesa m;
	public Aplicacion()
	{
		JFrame f = new JFrame("Planets");
	    f.setSize(500,830);
	    	    
		try {
			
			ResManager.getInstancia().setLoadVariables(new InputSource(new FileInputStream(new File("res/xml/variables.xml"))));
			ResManager.getInstancia().setPantallas(new InputSource(new FileInputStream(new File("res/xml/1.xml"))));
			
		} catch (FileNotFoundException e1) {
			IO.ConsoleLog.println("variables.xml o pantallas.xml no trobat");
			e1.printStackTrace();
		}
	    m = Mesa.getInstancia();
	    
	    JComponentMesa JCMesa = new JComponentMesa(f);
	    
	    
	    InputAdapter _input = new InputAdapter(m.getLogicaGeneral());
	    
	    JCMesa.addMouseListener(_input);
	    JCMesa.addMouseMotionListener(_input);
	    JCMesa.setFocusable(true);
	    JCMesa.addKeyListener(_input);
	    
	    
	    Container content = f.getContentPane();
	 	content.add(JCMesa);
	   
	 	
	    f.addWindowListener(new ExitListener());
	    f.setVisible(true);
	  
	    long now = System.currentTimeMillis();
	    
	    
	    int it_count = 0;
	    int _fraccio_ms = 8; 
	    
        while (true){
            m.doAnimaciones();
            m.doLogica();
            JCMesa.repaint();
            
            
            //IO.ConsoleLog.println("millis:"+ (now - System.currentTimeMillis()) );
            now = System.currentTimeMillis();
            
            it_count++;
            
            if( it_count == _fraccio_ms){
            	it_count = 0;
	            try{
	                Thread.sleep(1);
	            }catch(InterruptedException e){}
            }
        }

	}
	public static void main(String argv[])
	{
		Aplicacion a = new Aplicacion();
	}

	
}
