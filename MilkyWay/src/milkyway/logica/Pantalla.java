package milkyway.logica;

import java.util.Iterator;
import java.util.Vector;

import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.*;


import milkyway.elementos.Diana;
import milkyway.elementos.Movil;
import milkyway.elementos.Planeta;
import milkyway.elementos.Trajectoria;
import milkyway.elementos.ZonaInercia;

public class Pantalla extends Vector implements LoadSaveXML{

	public Pantalla(){

	}
	
	public int parse(NBEasyXML xml,int position){
		int idx = xml.findChildElement(position);
		
		while(idx != -1){
			int id = XMLKeys.identify(xml.getName(idx));
			switch(id){
				case XMLKeys.movil:
					Movil m = new Movil();
					idx = m.parse(xml,idx);
					m.setTrajectoria(new Trajectoria()); //TODO: a incorporar a pantalla desde un xml , cuestionar si val la pena el linkatge amb movil (podria ser un element solt?)
					this.add(m);
				break;
				case XMLKeys.diana:
					Diana d = new Diana();
					idx = d.parse(xml,idx);
					this.add(d);
				break;
				case XMLKeys.planeta:
					Planeta p = new Planeta();
					idx = p.parse(xml,idx);
					this.add(p);
				break;
				case XMLKeys.zonaInercia:
					ZonaInercia z = new ZonaInercia();
					idx = z.parse(xml,idx);
					this.add(z);
				break;
				default:
					IO.ConsoleLog.println("XML Elemento child de Pantalla no reconocido");
				
				}

				
			
		}
		return xml.findNextPeer(position);
		
	}

	public String save(){
		Iterator it = this.iterator();
		String out = "<Pantallas><"+XMLKeys.id2str(XMLKeys.pantalla)+">\n";
		while(it.hasNext()){
			out+= ((LoadSaveXML)it.next()).save();
		}
		out += "</"+XMLKeys.id2str(XMLKeys.pantalla)+"></Pantallas>\n";
		return out;
	}
	
}

