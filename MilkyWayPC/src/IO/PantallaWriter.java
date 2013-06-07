package IO;

import java.io.*;

import org.xml.sax.InputSource;

public class PantallaWriter {
	
	private static PantallaWriter _instancia = null;
	
	public static PantallaWriter getInstancia(){
		if(_instancia == null)
			_instancia = new PantallaWriter();
		return _instancia;
	}
	
	protected PantallaWriter(){
		
	}
	
	
	public static void wirtePantalla(String pantalla){
		  try{
			  
			  BufferedWriter output = new BufferedWriter(new FileWriter("res/xml/1.xml"));
			  output.write(pantalla);
			  output.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}

	public InputSource getNewPantallaFIS() {
		InputSource is = null;
		try{
			is = new InputSource(new FileInputStream(new File("res/xml/1.xml")));
		}catch(Exception e){
			IO.ConsoleLog.println("file not found"+ "res/xml/1.xml");
		}
		
		return is;
	}
}
