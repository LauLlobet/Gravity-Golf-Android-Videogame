package milkyway.logica;

import java.util.Iterator;
import java.util.Vector;

import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.animaciones.AniEscalar;
import milkyway.animaciones.AniMoverAcelerado;
import milkyway.animaciones.Animacion;
import milkyway.elementos.*;
import milkyway.logica.*;


import milkyway.logica.ResManager;
import milkyway.elementos.Polvo;


public class NubePolvo implements LoadSaveXML{
	
	private String _polvo_textura_location =  XMLKeys.emptyString ;
	private double _radio_polvo = XMLKeys.emptyDouble;
	private double _separacion_polvos = XMLKeys.emptyDouble;
	private double _distancia_ultimo_polvo = 0;
	private int _numero_polvos = XMLKeys.emptyInt;
	
	//private Polvo _prototipo; // es pot fer servir el patro protype per a crear els numerosos polvos
	private Vector _lista_polvos;
	
	public NubePolvo(){
		_lista_polvos = new Vector();
	}
	
	
	private boolean atributosIncompletosPolvoFactory(){
		if(_polvo_textura_location == XMLKeys.emptyString || _radio_polvo == XMLKeys.emptyDouble || _separacion_polvos == XMLKeys.emptyDouble || _numero_polvos == XMLKeys.emptyInt)
			return true;
		else
			return false;
	}
	
	public void initDefault() {
		setRadio(ResManager.getInstancia().getVariableDouble(XMLKeys.radioPolvo));

		setSeparacion(ResManager.getInstancia().getVariableDouble(XMLKeys.separacionPolvos));
		
		setNumeroPolvos(ResManager.getInstancia().getVariableInt(XMLKeys.numeroPolvos));
		
		setTextura(ResManager.getInstancia().getVariableString(XMLKeys.polvoTextura));

		_distancia_ultimo_polvo = _separacion_polvos;
		
		
	}
	
	
	public int parse(NBEasyXML xml,int idx){
		IO.ConsoleLog.println("parseando parsePolvoNuve");
		int didx = xml.findChildElement(idx); //idx apunta a Movil, didx apunta al primer atribut de elemento

		while(atributosIncompletosPolvoFactory()){
			int id = XMLKeys.identify(xml.getName(didx));
			switch(id){
				case XMLKeys.polvoTextura:
					setTextura(xml.getContent(didx));
					didx = xml.findNextPeer(didx);
				break;
				case XMLKeys.radioPolvo:
					setRadio(xml.getDoubleContent(didx));
					didx = xml.findNextPeer(didx);
				break;
				case XMLKeys.separacionPolvos:
					setSeparacion(xml.getDoubleContent(didx));
					didx = xml.findNextPeer(didx);
				break;
				case XMLKeys.numeroPolvos:
					setNumeroPolvos(xml.getIntContent(didx));
					didx = xml.findNextPeer(didx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en PolvoFactory no reconocida");
			}
			_distancia_ultimo_polvo = _separacion_polvos;
		}
		
		return xml.findNextPeer(idx);
	}
	
	public String save(){
		return "\t\t<"+XMLKeys.id2str(XMLKeys.nubePolvo)+">\n"+
				"\t\t\t<"+XMLKeys.id2str(XMLKeys.texturaLocation)+">"+getTexturaLocation()+"</"+XMLKeys.id2str(XMLKeys.texturaLocation)+">\n"+
				"\t\t\t<"+XMLKeys.id2str(XMLKeys.radio)+">"+getRadioPolvo()+"</"+XMLKeys.id2str(XMLKeys.radio)+">\n"+
				"\t\t\t<"+XMLKeys.id2str(XMLKeys.separacionPolvos)+">"+getSeparacion()+"</"+XMLKeys.id2str(XMLKeys.separacionPolvos)+">\n"+
				"\t\t\t<"+XMLKeys.id2str(XMLKeys.numeroPolvos)+">"+getNumeroPolvos()+"</"+XMLKeys.id2str(XMLKeys.numeroPolvos)+">\n"+
				"\t\t</"+XMLKeys.id2str(XMLKeys.nubePolvo)+">\n";
	}
	
	public String getTexturaLocation(){
		return _polvo_textura_location;
	}
	private void setRadio(double r){
		_radio_polvo = r;
	}
	
	public void setSeparacion(double sep){
		_separacion_polvos = sep;
	}
	
	public void setNumeroPolvos(int n){
		_numero_polvos = n;
	}
	
	public double getRadioPolvo(){
		return _radio_polvo;
	}
	
	public double getSeparacion(){
		return _separacion_polvos;
	}
	
	public int getNumeroPolvos(){
		return _numero_polvos;
	}
	
	public boolean disminuyeSeparacion(double dsep){ // retorna true si se ha recorrido una separacion es 
		_distancia_ultimo_polvo -= dsep;
		if(_distancia_ultimo_polvo <= 0 ){
			_distancia_ultimo_polvo = _separacion_polvos;
			return true;
		}
		return false;
	}
	
	private void setTextura(String textura_location){
		_polvo_textura_location = textura_location;
	}
	
	public Polvo createPolvo(double x, double y,double vx, double vy, Mesa m){
		
		Polvo p = new Polvo(_polvo_textura_location);
		p.setXY(x, y);
		p.setRadio(_radio_polvo);
		
		_lista_polvos.add(p);
		m.nuevoElemento(p);
		Animacion a = new AniMoverAcelerado(vx,vy,0,0,40);
		m.nuevaAnimacion(p,a);
		
		if(_lista_polvos.size()>_numero_polvos){
			Polvo last = (Polvo)_lista_polvos.firstElement();
			_lista_polvos.remove(last);
			m.eliminaElemento(last);

		}
		return p;
		
	}



	

		

}
