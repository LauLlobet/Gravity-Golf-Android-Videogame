package milkyway.elementos;



import java.util.*;

import milkyway.*;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.geometria.Vector2D;
import milkyway.elementos.Circular;

public class Masa extends Circular implements LoadSaveXML{
	private double _masa = XMLKeys.emptyDouble; 
	
	public Masa() {
	}
	
	private boolean atributosIncompletosMasa(){
		if(_masa == XMLKeys.emptyDouble)
			return true;
		return false;
	}
	public int parse(NBEasyXML xml,int idx){
		//idx apunta al primer child de movil/planeta, es el primer atribut d'elemento
		int didx = super.parse(xml,idx); //didx apunta al primer atribut de masa
		
		while(atributosIncompletosMasa()){
			int id = XMLKeys.identify(xml.getName(didx));
			switch(id){
				case XMLKeys.masa:
					setMasa(xml.getDoubleContent(didx));
					didx = xml.findNextPeer(didx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en Masa no reconocida");
				
				}	
		}
		return didx;
	}
	
	public String save(){
		return super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.masa)+">"+getMasa()+"</"+XMLKeys.id2str(XMLKeys.masa)+">\n";
	}
	
	public void setMasa(double masa)
	{
		_masa = masa;
	}
	
	public double getMasa()
	{
		return _masa;
	}

}
