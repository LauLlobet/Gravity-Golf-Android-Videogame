package IO;

import java.io.*;

import org.xml.sax.InputSource;

import milkyway.Mesa;

import android.content.Context;
import android.content.res.AssetManager;

public class PantallaWriter {
	
	private static PantallaWriter _instancia = null;
	Context _context;
	
	public static PantallaWriter getInstancia(){
		if(_instancia == null)
			_instancia = new PantallaWriter();
		return _instancia;
	}
	
	protected PantallaWriter(){
		
	}
	
	public void setContext(Context c){
		_context = c;
	}
	
	public static void wirtePantalla(String pantalla){
		  try{
			  
			 /* BufferedWriter output = new BufferedWriter(new FileWriter("res/xml/1.xml"));
			  output.write(pantalla);
			  output.close();*/
			  
			  IO.ConsoleLog.println("NO ES GUARDEN LES PANTALLES EN LA VERSIO ANDROID");
			  
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}
	
	public InputSource getNewPantallaFIS() {
		
		AssetManager mgr =  _context.getAssets();
		InputSource is = null;
		try{
			is =  new InputSource(mgr.open("pantallas.xml"));
		}catch(Exception e){
			IO.ConsoleLog.println("file not found: "+"pantallas.xml");
		}
		return is;
	}
	
}
