package milkyway.fisica;

import java.util.Iterator;
import java.util.Vector;

import milkyway.XMLUtils.XMLKeys;
import milkyway.geometria.Vector2D;
import milkyway.logica.ResManager;



public class DetectorTemblores {
	private double _apertura_maxima;
	private double _similitud_velocidades;
	private double _velocidad_minima_parado;
	private double _velocidad_minima_temblor; // velocidad minima para descartar temblor
	private int _longitud_buffer;
	private Vector _anteriores_dxy;
	
	public DetectorTemblores(){
		_apertura_maxima = ResManager.getInstancia().getVariableDouble(XMLKeys.detectorTembloresApertura);
		_similitud_velocidades = ResManager.getInstancia().getVariableDouble(XMLKeys.detectorTembloresSimilitudVeclocidades);
		_velocidad_minima_parado = ResManager.getInstancia().getVariableDouble(XMLKeys.detectorTembloresVelocidadMinimaParado);
		_velocidad_minima_temblor = ResManager.getInstancia().getVariableDouble(XMLKeys.detectorTembloresVelocidadMinimaTemblor);
		_longitud_buffer = ResManager.getInstancia().getVariableInt(XMLKeys.detectorTembloresLongitudBuffer);
		_anteriores_dxy = new Vector();
	}
	
	public void nextD(Vector2D dxy){
		//queremos solo vectores diferenciales donde su punto inicial sea el 0,0
		if(dxy.getXo()!=0 || dxy.getYo()!= 0){
			IO.ConsoleLog.println("Eliminador de temblores queremos solo vectores diferenciales donde su punto inicial sea el 0,0 ");
			return;
		}
		//añadimos al principio del buffer i borramos al final del buffer	
		_anteriores_dxy.add(dxy);
		if(_anteriores_dxy.size()>_longitud_buffer)
			_anteriores_dxy.remove(0);
	}
	public boolean isTemblando(){
		if(_anteriores_dxy.size()<_longitud_buffer)
			return false;
		//clonamos la lista
		Vector lista = (Vector) _anteriores_dxy.clone(); 
		//preparamos las variables
		Vector2D ultimo = (Vector2D) lista.lastElement();
		lista.remove(ultimo);
		//llamamos a la funcion recursiva
		//IO.ConsoleLog.println("_____TEMBLOR___");
		/*Iterator it = _anteriores_dxy.iterator();
		while(it.hasNext())
			IO.ConsoleLog.println((Vector2D)it.next());
		*/
		boolean isTemblando = areOpuestos(ultimo,lista);
		/*if(isTemblando){
			Iterator it = _anteriores_dxy.iterator();
			while(it.hasNext())
				IO.ConsoleLog.println((Vector2D)it.next());
		}*/
		return isTemblando;
	}
	
	public boolean isParado(){
		if(_anteriores_dxy.size()<_longitud_buffer)
			return false;
		//clonamos la lista
		Vector lista = (Vector) _anteriores_dxy.clone(); 
		//preparamos las variables
		Vector2D ultimo = (Vector2D) lista.lastElement();
		lista.remove(ultimo);
		//llamamos a la funcion recursiva
		return isParado(ultimo,lista);
	}
	
	/*
	 * funcion que retorna true si los vectores de la lista son opuestos consecutivamente
	 */
	private boolean areOpuestos(Vector2D ultimo,Vector lista_vectores){
		
		if (lista_vectores.size()==0)
			return true;
		if (ultimo.getModulo()<_velocidad_minima_parado) // caso quieto, si la velocidad es muy pequeña, no tiembla
			return false;
		//IO.ConsoleLog.println("Modulo velocidad:"+ultimo.getModulo());
		//IO.ConsoleLog.println("__areOpuestos__");
		//hacemos pop
		Vector2D penultimo =(Vector2D) lista_vectores.lastElement();
		lista_vectores.remove(penultimo);
		
		//si las dos ultimas velocidades son muy similares se busca en la siguiente
		if(Math.abs(ultimo.getXd()-penultimo.getXd())+Math.abs(ultimo.getDY()-penultimo.getDY())<_similitud_velocidades){
			//IO.ConsoleLog.println("dos angulos iguales, saltamos");
			return areOpuestos(penultimo,lista_vectores);
		}
		
		//rotamos 180
		Vector2D penultimo_rotado = new Vector2D(0,0,penultimo.getXd(),penultimo.getYd());//clonamos
		penultimo_rotado.rotateFrom(Math.PI, 0, 0);
		//miramos si el angulo que forman es mayor que la apertura maxima
		//IO.ConsoleLog.println("anguloultim:"+ultimo.getAngle()+"angulopenultimo"+penultimo.getAngle());
		//IO.ConsoleLog.println("anguloultim:"+ultimo.getAngle()+"angulopenultimorotado"+penultimo_rotado.getAngle());
		
		Double angulo = (ultimo.getAngle()-penultimo_rotado.getAngle())%(2*Math.PI); // que no sea un angulo de mas de una vuelta
		if(angulo>Math.PI) // que si es mayor que 180 se convierta en negativo
			angulo = (2*Math.PI)-angulo;
		angulo = Math.abs(angulo);	//que sea positivo
		
		//IO.ConsoleLog.println("angulo:"+angulo+"limite"+_apertura_maxima);
		if(angulo <_apertura_maxima)
			return areOpuestos(penultimo,lista_vectores);
		else
			return false;
	}
	public boolean isParado(Vector2D ultimo,Vector lista_vectores){
		if (lista_vectores.size()==0)
			return true; // todas son minimas
		//hacemos pop
		Vector2D penultimo =(Vector2D) lista_vectores.lastElement();
		lista_vectores.remove(penultimo);
		
		//miramos si la velocidad es inferior al limite
		if(ultimo.getModulo()<_velocidad_minima_parado)
			return isParado(penultimo,lista_vectores);
		else
			return false;
	}
	public void restart() {
		_anteriores_dxy.removeAllElements();
	}
	
}
