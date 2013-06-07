package milkyway.elementos;

import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.geometria.Vector2D;

import milkyway.elementos.Circular;
import milkyway.elementos.Elemento;

public class Rectangulo extends Elemento implements LoadSaveXML{

	private double _dx = XMLKeys.emptyDouble;
	private double _dy = XMLKeys.emptyDouble;
	

	public Rectangulo() {
		super();
	}
	
	private boolean atributosIncompletosRectangulo(){
		if(_dx == XMLKeys.emptyDouble || _dy == XMLKeys.emptyDouble)
			return true;
		else
			return false;
	}
	public int parse(NBEasyXML xml, int idx) {
		int didx = super.parse(xml,idx);
		
		while(atributosIncompletosRectangulo()){
			int id = XMLKeys.identify(xml.getName(didx));
			switch(id){
				case XMLKeys.longitud:
					 setXd(xml.getDoubleContent(didx));
					 didx = xml.findNextPeer(didx);
				break;
				case XMLKeys.altura:
					 setYd(xml.getDoubleContent(didx));
					 didx = xml.findNextPeer(didx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en Rectangulo no reconocida");
				
				}	
		}
		return didx;
	}
	public String save(){
		return 	super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.longitud)+">"+getXd()+"</"+XMLKeys.id2str(XMLKeys.longitud)+">\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.altura)+">"+getYd()+"</"+XMLKeys.id2str(XMLKeys.altura)+">\n";
	}
	
	public void setXY(double x,double y){
		super.setXY(x,y);
	}

	
	public void setDXY(double x,double y){
		super.setDXY(x,y);
		_dx += x;
		_dy += y;
	}
	
	public double getXd(){
		return _dx;
	}
	
	public double getYd(){
		return _dy;
	}
	
	public double getXdRelatiu(){
		return _dx*Mesa.getInstancia().getescalat();
	}
	
	public double getYdRelatiu(){
		return _dy*Mesa.getInstancia().getescalat();
	}
	
	public void setYd(double yd){
		_dy = yd;
	}
	public void setXd(double xd){
		_dx = xd;
	}
	
	
	public boolean isParteDentro(Circular m){
		return ( m.getX() + m.getRadio() > getX() && 
				m.getY() + m.getRadio() > getY() && 
				m.getX() - m.getRadio() < getXd()+getX() &&
				m.getY() - m.getRadio() < getYd()+getY() );
	}
	
	public boolean isTodoDentro(Circular m){
		return ( m.getX() - m.getRadio() > getX() && 
				m.getY() - m.getRadio() > getY() && 
				m.getX() + m.getRadio() < getXd()+getX() &&
				m.getY() + m.getRadio() < getYd()+getY() );
	}

	public double tangenteSeguraSuperficie(Circular m){
		
		 if( m.getX() + m.getRadio() > getX() )
			 return (Math.PI/2);
		 if( m.getX() - m.getRadio() < getXd()+getX() )
			 return -(Math.PI/2);
		 if( m.getY() + m.getRadio() > getY() )
			 return Math.PI;
		 if( m.getY() - m.getRadio() < getYd()+getY() )
			 return 0;
		 return 0;
	}

	public double distanciaaMargen(Circular c, double normal,double margen) {
		return 0;
	}
	
}
