package milkyway.elementos;

import java.util.Iterator;
import java.util.Vector;

import milkyway.elementos.Movil;
import milkyway.elementos.Rectangulo;

public class ListaTiradas extends Rectangulo{
	
	private Vector _lista_moviles;
	static private double _separacio = 10;//TODO: XML variables
	private double _posicio_ultim;
	
	public ListaTiradas(){
		super();
		_posicio_ultim = _separacio;
		setXY(50,50);
		setXd(300);
		setYd(40);
		_lista_moviles = new Vector();
	}
	
	public void removeAllElements(){
		_lista_moviles.removeAllElements();
		_posicio_ultim = _separacio;
	}
	public void add(Object o){
		Movil m = (Movil) o;
		m.guardaPosicion();
		m.setXY( getX() + _posicio_ultim + m.getRadio() , getY()+20);
		
		_posicio_ultim += m.getRadio() + _separacio; 
		
		_lista_moviles.add(m);
	}

	public int size(){
		return _lista_moviles.size();
	}
	
	public Movil popMovil(){
		Movil m = (Movil)_lista_moviles.firstElement();
		m.restauraPosicion();
		return m;
	}
	
	public void remove(Object o){
		_lista_moviles.remove(o);
	}

	public Vector getListaMoviles() {
		// TODO Auto-generated method stub
		return _lista_moviles;
	}
	
}
