package milkyway.elementos;

import java.util.Iterator;
import java.util.Vector;
import java.util.Enumeration;

import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.*;
import milkyway.logica.*;


import milkyway.elementos.Rectangulo;
import milkyway.fisica.efectos.EfectoInercia;
import milkyway.fisica.efectos.EfectoNubePolvo;
import milkyway.fisica.efectos.EfectoParada;

public class ZonaInercia extends Rectangulo implements AfectaMovil, LoadSaveXML{

	private Vector _efectos_cuartos;
		
	public ZonaInercia()
	{
		super();
		_efectos_cuartos =new Vector();
		/*_efectos.add(new EfectoInercia());
		_efectos.add(new EfectoParada());*/
	}
	
	public int parse(NBEasyXML xml, int idx) {
		IO.ConsoleLog.println("parseando ZonaInercia");
		int didx = xml.findChildElement(idx); //idx apunta a Movil, didx apunta al primer atribut de elemento
		int cidx = super.parse(xml,didx); //cidx apunta al nextPeer de l'ultim atribut de masa 
	

		while(cidx != -1){
			int id = XMLKeys.identify(xml.getName(cidx));
			switch(id){
				case XMLKeys.efectoInercia:
					EfectoInercia e = new EfectoInercia(this);
					e.parse(xml,cidx);
					_efectos_cuartos.add(e);
					cidx = xml.findNextPeer(cidx);
				break;
				case XMLKeys.efectoParada:
					_efectos_cuartos.add(new EfectoParada(this));
					cidx = xml.findNextPeer(cidx);
				break;
				case XMLKeys.efectoNubePolvo:
					_efectos_cuartos.add(new EfectoNubePolvo(this));
					cidx = xml.findNextPeer(cidx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en parseZonaInercia no reconocida");
			}
		}
		return xml.findNextPeer(idx);
	}
	
	public String save(){ // TODO: hauria de ser un for each efecto  out+= efecto.save() i eliminar getEfectoRebote
		return "\t<"+XMLKeys.id2str(XMLKeys.zonaInercia)+">\n"+
				super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.efectoNubePolvo)+"/>\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.efectoParada)+"/>\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.efectoInercia)+">\n"+getEfectoInercia().save()+"\t\t</"+XMLKeys.id2str(XMLKeys.efectoInercia)+">\n"+
				"\t</"+XMLKeys.id2str(XMLKeys.zonaInercia)+">\n";
	}
	
	public EfectoInercia getEfectoInercia(){
		Iterator it = _efectos_cuartos.iterator();
		while(it.hasNext()){
			Object e = it.next();
			if(e instanceof EfectoInercia)
				return (EfectoInercia)e;
		}
		IO.ConsoleLog.println("getEfectoInercia no ha tornat cap resultat");
		return null;
	}
	
	public Enumeration getEfectos()
	{
		return null;
	}
	
	public Enumeration getEfectosSegundos(double x, double y)
	{
		return  null;
	}
	
	public Enumeration getEfectosTerceros(double x, double y)
	{
		return  null;
	}

	public Enumeration getEfectosCuartos()
	{
		return  _efectos_cuartos.elements();
	}


	
}
