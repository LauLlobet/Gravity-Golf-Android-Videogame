package milkyway.elementos;

/**
 * Clase que define las caracter�sticas de todo elemento de la mesa
 * 
 *
 */
import java.awt.*;
import java.util.Vector;

import milkyway.elementos.Circular;
import milkyway.logica.VectorElementos;

import milkyway.*;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;



// La classe elemento �s la superclase de todos los elementos de la mesa
//estos como minimo tienen una id y unas coordenadas de posici�n
public abstract class Elemento implements LoadSaveXML{
	// Atributos
	//int _id;

	//private Mesa _mesa;
	
	private double _x = XMLKeys.emptyDouble;
	private double _y = XMLKeys.emptyDouble;
	private double _angulo = XMLKeys.emptyDouble;
    private double _alpha = XMLKeys.emptyDouble;
    private double _energia = 9999;
	//private Color color;

    public double getEnergia() {
		return _energia;
	}
	public void setEnergia(double _energia) {
		this._energia = _energia;
	}

	private VectorElementos _contenedorElementos;
	
	/*Elemento(Mesa m){
		this._mesa = m;
	}*/
    
    Elemento(){
    	
    }
	private boolean atributosIncompletosElemento(){
		if(_x == XMLKeys.emptyDouble || _y == XMLKeys.emptyDouble)
			return true;
		else
			return false;
	}
	
	public void setAngulo(double angulo){
		this._angulo = angulo;		
	}
        
    public void setAlpha(double alpha){
            this._alpha = alpha;
    }
	
	public void setXY(double x,double y){
		setX(x);
		setY(y);
	}
	
	public void setX(double x){
		this._x = x;
	}
	
	public void setY(double y){
		this._y = y;
	}
	
	public void setDXY(double dx,double dy){
		setX( getX() + dx);
		setY( getY() + dy) ;
	}
	
	
	public void setDAngulo(double dangulo){
		this._angulo += dangulo;
	}
    
    public void setDAlpha(double dalpha){
            this._alpha += dalpha;
    }
	
	public double getX(){
		return _x;
	}
	
	public double getY(){
		return _y;
	}
	
	
	
	public double getXRelatiu(){
		return Mesa.getInstancia().getoffsetX()+ _x*Mesa.getInstancia().getescalat();
		
	}
	
	public double getYRelatiu(){
		return Mesa.getInstancia().getoffsetY()+ _y*Mesa.getInstancia().getescalat();
	}
	
	

	public double getAngulo(){
		return _angulo;
	}
        
    public double getAlpha(){
            return _alpha;
    }
	
/*	public double getId(){
		return _id;
	}*/

	public int parse(NBEasyXML xml, int idx) {

		while(atributosIncompletosElemento()){
			int id = XMLKeys.identify(xml.getName(idx));
			switch(id){
				case XMLKeys.x:
					 setX(xml.getDoubleContent(idx));
					idx = xml.findNextPeer(idx);
				break;
				case XMLKeys.y:
					setY(xml.getDoubleContent(idx));
					idx = xml.findNextPeer(idx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en Elemento no reconocida");
				
				}	
		}
		return idx;
	}
	
	public String save(){
		return "\t\t<"+XMLKeys.id2str(XMLKeys.x)+">"+getX()+"</"+XMLKeys.id2str(XMLKeys.x)+">\n"+
				"\t\t<"+XMLKeys.id2str(XMLKeys.y)+">"+getY()+"</"+XMLKeys.id2str(XMLKeys.y)+">\n";
	}
	
	
	// GEOMETRIA 
	
	abstract public boolean isParteDentro(Circular m); 
	
	abstract public boolean isTodoDentro(Circular m); 

	public boolean isParteFuera(Circular m){
		return ( isParteDentro(m) && !isTodoDentro(m));
	}
	
	public boolean isTodoFuera(Circular m){
		return ( !isParteDentro(m) && !isTodoDentro(m));
	}
	
	public boolean isIntersectsSuperficie(Circular m){
		return (isParteDentro(m) && !isTodoDentro(m));
	}
	
	abstract public double tangenteSeguraSuperficie(Circular m);
	
	abstract public double distanciaaMargen(Circular c, double normal, double margen);
	
	public double tangenteSuperficie(Circular m){
		if(isTodoFuera(m) || isTodoDentro(m)){
			//IO.ConsoleLog.println("ELEMENTO Error, buscant una tangent cuan no hi ha colisio");
			return 0;
		}
		return tangenteSeguraSuperficie(m);
	}

	public void setContenedorElementos(VectorElementos _contenedorElementos) {
		this._contenedorElementos = _contenedorElementos;
	}
	
	public void desaparece(){
		_contenedorElementos.remove(this);
	}
	
	public void setDEnergia(double de){
	
		_energia+=de;
		IO.ConsoleLog.println("De:"+de + "ef:"+_energia);
	}
	/*abstract public double expulsarDentro(Movil m);
	abstract public double expulsarFuera(Movil m);*/
	
}
