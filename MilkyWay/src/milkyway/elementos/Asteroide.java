package milkyway.elementos;

import java.util.Enumeration;
import java.util.Vector;

import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.EfectoFriccion;

public class Asteroide extends Circular implements AfectaMovil {

	private double _friccion;
	private double _energia;
	private Vector efectos_terceros;
	
	public Asteroide(double friccion, int radio){
		efectos_terceros =new Vector();
		EfectoFriccion fric = new EfectoFriccion(this);//TODO: meter friccion en efecto friccion
		fric.setFriccion(0.3);
		efectos_terceros.add(fric);
		setRadio(radio);
	}
	
	
	@Override
	public Enumeration getEfectos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getEfectosSegundos(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getEfectosTerceros(double x, double y) {
		return efectos_terceros.elements();
	}

	@Override
	public Enumeration getEfectosCuartos() {
		// TODO Auto-generated method stub
		return null;
	}



	public double getFriccion() {
		return _friccion;
	}


	public void setFriccion(double _friccion) {
		this._friccion = _friccion;
	}

	public void setDEnergia(double de){
		
		super.setDEnergia(de);
		if(getEnergia( )<= 0){
			desaparece();
		}
	}
	
}
