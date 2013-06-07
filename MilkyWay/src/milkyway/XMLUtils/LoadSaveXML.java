package milkyway.XMLUtils;

import milkyway.XMLUtils.NBEasyXML;
import milkyway.elementos.Movil;
import milkyway.fisica.AfectaMovil;

public interface LoadSaveXML {

	public int parse(NBEasyXML xml,int idx);
	
	public String save();
	
}
