package milkyway.elementos;

import java.util.Enumeration;
import java.util.Vector;

import milkyway.XMLUtils.LoadSaveXML;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.EfectoFriccion;
import milkyway.fisica.efectos.EfectoRebote;

public class ImagenRebote extends Imagen implements AfectaMovil{

	private Vector efectos_segundos;
	
	public ImagenRebote(){
		super();
		efectos_segundos =new Vector();
		EfectoRebote reb = new EfectoRebote(this,-0.4,-0.4);
		efectos_segundos.add(reb);
	}
	
	public Enumeration getEfectos() {
		return null;
	}

	public Enumeration getEfectosSegundos(double x, double y) {
		return efectos_segundos.elements();
	}

	public Enumeration getEfectosTerceros(double x, double y) {
		return null;
	}

	public Enumeration getEfectosCuartos() {
		return null;
	}



}
