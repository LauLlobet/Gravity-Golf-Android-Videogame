package milkyway.elementos;



import java.awt.*;
import java.util.*;

import milkyway.*;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.fisica.*;
import milkyway.fisica.efectos.*;
import milkyway.geometria.*;
import milkyway.logica.*;
import milkyway.elementos.Masa;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.EfectoGravedad;
import milkyway.fisica.efectos.EfectoRebote;

public class Planeta extends Masa implements AfectaMovil,LoadSaveXML {
	
	//private double _radio=50;
	private double _factorRadio=0.3; 
	
	private Vector _efectos_primeros;
	private Vector _efectos_segundos;
	
	public double energia_inicial = 1200;
	
	public Planeta() {
		super();
		_efectos_primeros=new Vector();
		_efectos_segundos=new Vector();
		/*
		EfectoNotInPlanet c=new EfectoNotInPlanet();
		_efectos.add(c);
		_efectos.add(a);
		_efectos.add(b);*/
		
	}
	
	public Planeta(Object o){
		_efectos_primeros=new Vector();
		_efectos_segundos=new Vector();
		EfectoGravedad grav =new EfectoGravedad(this);
		EfectoRebote reb = new EfectoRebote(this,-0.4,-0.4);
		_efectos_primeros.add(grav);
		_efectos_segundos.add(reb);
	}

	public Planeta(int masa,int x,int y, int radio){
			
			_efectos_primeros=new Vector();
			_efectos_segundos=new Vector();
			EfectoGravedad grav =new EfectoGravedad(this);//-0.4 avans
			EfectoRebote reb = new EfectoRebote(this,0,0);
			_efectos_primeros.add(grav);
			_efectos_segundos.add(reb);
		
			setMasa(masa);
			setXY(x,y);
			setRadio(radio);
			
			this.setEnergia(energia_inicial);
	}
	
	public int parse(NBEasyXML xml,int idx){
		IO.ConsoleLog.println("parseando planeta");
		int didx = xml.findChildElement(idx); //idx apunta a Movil, didx apunta al primer atribut de elemento
		int cidx = super.parse(xml,didx); //cidx apunta al nextPeer de l'ultim atribut de masa 
	

		while(cidx != -1){
			int id = XMLKeys.identify(xml.getName(cidx));
			switch(id){
				case XMLKeys.efectoGravedad:
					_efectos_primeros.add(new EfectoGravedad(this));
					cidx = xml.findNextPeer(cidx);
				break;
				case XMLKeys.efectoRebote:
					EfectoRebote e = new EfectoRebote(this);
					e.parse(xml,cidx); 
					_efectos_segundos.add(e);
					cidx = xml.findNextPeer(cidx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en Planeta no reconocida");
			}
				
		}
		
		this.setEnergia(energia_inicial);
		
		return xml.findNextPeer(idx);
	}
	
	public String save(){ // TODO: hauria de ser un for each efecto  out+= efecto.save() i eliminar getEfectoRebote
		return "\t<"+XMLKeys.id2str(XMLKeys.planeta)+">\n"+
				super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.efectoGravedad)+"/>\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.efectoRebote)+">\n"+getEfectoRebote().save()+"\t\t</"+XMLKeys.id2str(XMLKeys.efectoRebote)+">\n"+
				"\t</"+XMLKeys.id2str(XMLKeys.planeta)+">\n";
	}
	
	public EfectoRebote getEfectoRebote(){
		Iterator it = _efectos_segundos.iterator();
		while(it.hasNext()){
			Object e = it.next();
			if(e instanceof EfectoRebote)
				return (EfectoRebote)e;
		}
		IO.ConsoleLog.println("getEfectoRebote no ha tornat cap resultat");
		return null;
	}
	
	public Enumeration getEfectos()
	{
		return  _efectos_primeros.elements();
	}
	
	public Enumeration getEfectosSegundos(double x, double y)
	{
		return  _efectos_segundos.elements();
	}
	
	public Enumeration getEfectosTerceros(double x, double y)
	{
		return  null;
	}
	
	public Enumeration getEfectosCuartos()
	{
		return  null;
	}

	public void setDEnergia(double de){
		
		super.setDEnergia(de);
		if(getEnergia( )<= 0){
			desaparece();
		}
	}
	
}
