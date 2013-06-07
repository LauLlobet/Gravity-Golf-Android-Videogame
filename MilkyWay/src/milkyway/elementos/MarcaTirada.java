package milkyway.elementos;

import java.util.Iterator;

import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.geometria.Punto;
import milkyway.logica.*;


import milkyway.elementos.Circular;
import milkyway.logica.ResManager;


public class MarcaTirada extends Circular implements LoadSaveXML{
	
	private String _diana_textura_location =  XMLKeys.emptyString ;
	
	public MarcaTirada(double x, double y,double radio) {
		super(x,y,radio);
	
		String s = ResManager.getInstancia().getVariableString(XMLKeys.marcaTextura);
		setTextura(s);
	}	
	
	public String getTexturaLocation(){
		return _diana_textura_location;
	}
	
	private void setTextura(String textura_location){
		
		_diana_textura_location = textura_location;
	}

		
}
