package milkyway.animaciones;

/**
 * Define el paso de una animaci�n. Cada paso tiene los mismos atributos que puede tener
 * un elemento.
 * 
 *
 */

import milkyway.elementos.Elemento;
import milkyway.animaciones.Paso;

public class Paso {
	double x,y;
	double tamano, angulo, alpha;
	
	Paso(double x,double y, double tamano, double angulo, double alpha){
		this.x = x;
		this.y = y;
		this.tamano = tamano;
		this.angulo = angulo;
		this.alpha = alpha;
	}
	
	//Junta dos pasos en uno
	public void juntaPaso(Paso p){
		this.x += p.x;
		this.y += p.y;
		this.tamano += p.tamano;
		this.angulo += p.angulo;
		this.alpha += p.alpha;
	}
	
	//M�todo que aplica el paso de la animaci�n en el elemento recibido
	public void aplicaPaso(Elemento e){
		e.setDXY(x,y);
		e.setDAngulo(angulo);
		//TODO: set alpha i tamano
	}
}
