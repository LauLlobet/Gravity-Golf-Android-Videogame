package milkyway.elementos;

import java.util.Enumeration;
import java.util.Vector;

import milkyway.XMLUtils.LoadSaveXML;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.EfectoFriccion;

public class ImagenFriccion extends Imagen implements AfectaMovil{

	private Vector efectos_terceros;
	
	public ImagenFriccion(){
		super();
		efectos_terceros =new Vector();
		EfectoFriccion fric = new EfectoFriccion(this);
		fric.setFriccion(0.3);
		efectos_terceros.add(fric);
	}
	
	public Enumeration getEfectos() {
		return null;
	}

	public Enumeration getEfectosSegundos(double x, double y) {
		return null;
	}

	public Enumeration getEfectosTerceros(double x, double y) {
		return efectos_terceros.elements();
	}

	public Enumeration getEfectosCuartos() {
		return null;
	}



}
