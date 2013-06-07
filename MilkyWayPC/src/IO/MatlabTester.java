package IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Vector;

import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.Diana;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.fisica.FisicaMecanica;
import milkyway.geometria.Punto;
import milkyway.geometria.Vector2D;
import milkyway.logica.Pantalla;
import milkyway.logica.ResManager;

import org.xml.sax.InputSource;



public class MatlabTester {

	Movil _movil;
	Movil _movil_inicial;
	
	double _factor_v_dedo = 1;
	Vector _elementos;
	Vector _moviles;
	FisicaMecanica _fisica;
	Diana _diana;
	
	public MatlabTester(){
		recargaXml();
		_fisica = new FisicaMecanica();
	}
	
	public MatlabTester(Object o){
		recargaXmlLocal();
		_fisica = new FisicaMecanica();
	}
	public void recargaXml(){
		try {
			ResManager.getInstancia().setLoadVariables(new InputSource(new FileInputStream(new File("development/workspace/MilkyWayPC/res/xml/variables.xml"))));
			ResManager.getInstancia().setPantallas(new InputSource(new FileInputStream(new File("development/workspace/MilkyWayPC/res/xml/1.xml"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ResManager.getInstancia().parsePantallas();
		
		_factor_v_dedo = ResManager.getInstancia().getVariableDouble(XMLKeys.logicaFactorVDedo);
	
		Movil m = new Movil();
		
		_moviles = new Vector();
		_elementos = new Vector();
		
		Iterator it = ((Pantalla)ResManager.getInstancia().getPantallas().firstElement()).iterator();
		while(it.hasNext()){
			Elemento e = (Elemento)it.next();
			if(e instanceof Movil){
				_movil_inicial = (Movil)e;
			}if(e instanceof Diana){
				_diana = (Diana)e;
			}
			_elementos.add(e);
		}
		
		_movil = new Movil();
		_movil.setNube(null); //com que movil no tindra nube no es calculara en la simulacio 
	
		_moviles.add(_movil);
	
	}
	
	
	public String writeText(String input){
		String path = "nopath";  
		File f = new File("patata.xml");
		path = f.getAbsolutePath();
		
		System.out.println("MATLAB OUT ? "+path);
		return path;

	}

	public double distanciaMinima(double x, double y,int iteracions ){
		
		_movil.setAllAs(_movil_inicial);		
		
		double d_min = 999999;
		Vector2D m_to_d = new Vector2D(_movil.getX(),_movil.getY(),_diana.getX(),_diana.getY());
		double d;
		
		double vx = (_movil.getX()-x)*_factor_v_dedo;
		double vy = (_movil.getY()-y)*_factor_v_dedo;
		
		
		_movil.setV(vx, vy);
		

		for(int i = 0; i<iteracions;i++){
			_fisica.calculaDTFisicas(_moviles, _elementos);
			m_to_d.setXo(_movil.getX());
			m_to_d.setYo(_movil.getY());
			d = m_to_d.getModulo();
			if( d < d_min)
				d_min = d;
		}
		
		if(d_min <= _diana.getRadio()+_movil.getRadio())
			d_min = 0;
		
		return d_min;
	
	}
	
	
	public void recargaXmlLocal(){
		try {
			ResManager.getInstancia().setLoadVariables(new InputSource(new FileInputStream(new File("res/xml/variables.xml"))));
			ResManager.getInstancia().setPantallas(new InputSource(new FileInputStream(new File("res/xml/1.xml"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ResManager.getInstancia().parsePantallas();
		
		_factor_v_dedo = ResManager.getInstancia().getVariableDouble(XMLKeys.logicaFactorVDedo);
	
		Movil m = new Movil();
		
		_moviles = new Vector();
		_elementos = new Vector();
		
		Iterator it = ((Pantalla)ResManager.getInstancia().getPantallas().firstElement()).iterator();
		while(it.hasNext()){
			Elemento e = (Elemento)it.next();
			if(e instanceof Movil){
				_movil_inicial = (Movil)e;
			}if(e instanceof Diana){
				_diana = (Diana)e;
			}
			_elementos.add(e);
		}
		
		_movil = new Movil();
		_movil.setNube(null); //com que movil no tindra nube no es calculara en la simulacio 
	
		_moviles.add(_movil);
	
	}
}
