package milkyway.animaciones;

/**
 * Animaciones es la clase que se encarga de gestionar las animaciones que se den en la mesa.
 * 
 * @author U25907
 *
 */
import java.util.Hashtable;
import java.util.Enumeration;

import milkyway.elementos.Elemento;

import milkyway.animaciones.Animacion;

public class Animaciones {
	// Atributos
	Hashtable animaciones;
	
	// M�todos
	public Animaciones(){
		animaciones = new Hashtable();
	}
	
	public void nuevaAnimacion(Elemento e,Animacion a){
		//----- falta comprobar el caso de que el elemento e ya tenga una animaci�n asociada
		animaciones.put(e,a);
	}
	
	/**
	 * M�todo que realiza la animaci�n
	 *
	 */
	public void anima(){
		Elemento e_tmp; //elemento animado
		Animacion a_tmp; //animacion de un elemento
		boolean finalizada; //dice si la animacion ha acabado
		
		//recorremos el hash elemento a elemento y animamos
		for (Enumeration lista_elementos = animaciones.keys(); lista_elementos.hasMoreElements() ;) {
	        e_tmp = (Elemento)lista_elementos.nextElement();
	        a_tmp = (Animacion)animaciones.get(e_tmp);
	        finalizada = a_tmp.aplicaAnimacion(e_tmp);
	        
	        //miramos si la pila de pasos de la animacion est� ya vac�a (animaci�n acabada)
	        //en caso afirmativo borramos el elemento de la tabla de hash
	        if (finalizada){
	        	animaciones.remove(e_tmp);
	        }
	     }//for
	}

	public void eliminaAnimacion(Elemento e) {
		animaciones.remove(e);
		
	}
}
