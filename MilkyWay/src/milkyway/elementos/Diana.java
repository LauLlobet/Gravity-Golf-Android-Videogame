package milkyway.elementos;


import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.logica.*;

import milkyway.elementos.Circular;


public class Diana extends Circular implements LoadSaveXML{
	
	private String _diana_textura_location =  XMLKeys.emptyString ;
	
	private int _diana_final = XMLKeys.emptyInt;
	
	public Diana() {
		super();
	}
	
	private boolean atributosIncompletosDiana(){
		if(_diana_textura_location == XMLKeys.emptyString || _diana_final == XMLKeys.emptyInt)
			return true;
		else
			return false;
	}
	
	public int parse(NBEasyXML xml,int idx){
		IO.ConsoleLog.println("parseando diana");
		int didx = xml.findChildElement(idx); //idx apunta a Movil, didx apunta al primer atribut de elemento
		int cidx = super.parse(xml,didx); //cidx apunta al nextPeer de l'ultim atribut de masa 
	

		while(atributosIncompletosDiana()){
			int id = XMLKeys.identify(xml.getName(cidx));
			switch(id){
				case XMLKeys.texturaLocation:
					setTextura(xml.getContent(cidx));
					cidx = xml.findNextPeer(cidx);
				break;
				case XMLKeys.dianaFinal:
					set_diana_final(xml.getBooleanContent(cidx));
					cidx = xml.findNextPeer(cidx);
				break;
				
				default:
					IO.ConsoleLog.println("XML tag en Diana no reconocida");
			}
				
		}
		
		return xml.findNextPeer(idx);
	}
	public String save(){
		return "\t<"+XMLKeys.id2str(XMLKeys.diana)+">\n"+
				super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.texturaLocation)+">"+getTexturaLocation()+"</"+XMLKeys.id2str(XMLKeys.texturaLocation)+">\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.dianaFinal)+">"+is_diana_final()+"</"+XMLKeys.id2str(XMLKeys.dianaFinal)+">\n"+
				"\t</"+XMLKeys.id2str(XMLKeys.diana)+">\n";

	}
	
	public String getTexturaLocation(){
		return _diana_textura_location;
	}
	public void setTextura(String textura_location){
		
		_diana_textura_location = textura_location;
	}
		

	public boolean is_diana_final() {
		return ( _diana_final == 1 );
	}

	public void set_diana_final(boolean _diana_final) {
		if(_diana_final)
			this._diana_final = 1;
		else
			this._diana_final = 0;
	}

}
