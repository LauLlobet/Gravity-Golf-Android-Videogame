package milkyway.elementos;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import milkyway.fisica.AfectaMovil;
import milkyway.geometria.Punto;
import milkyway.geometria.Vector2D;

public class Forma extends Elemento implements AfectaMovil,Editable{
	
	private float _distancia_entre_planetas = 0.5f;
	private float _grosor = 5;
	private float _bache = -1;
	
	private int _masa = 0;
	
	private Vector<Punto> _linea ;
	private Vector<Circular> _circulares;
	

	private Circular _tipo = null;
	
	private boolean _actualizada = false;
	
	private double pcercanox = 9999999;
	private double pcercanoy = 9999999;
	private Circular pcercano = null;
	 
	
	public Forma(){
		_linea = new Vector<Punto>();
		_circulares = new Vector<Circular>();
		
		setTipo( new Planeta(0,0,0,2) );
	}
	
	public void setTipo(Circular c){
		
		_tipo = c;
		setGrosor((float)c.getRadio());
		
	}
	
	public void addPuntoLinea(int x,int y){
		

		if(_linea.size()>0){
			Punto last = _linea.lastElement();
			if(last.getX()==x && last.getY()==y)// si igual al el ultimo pasar
				return;
		}
		
		if(_linea.size()==0){ // el primer punto de la linea sera la posicion del elemento pared
			setX(x);
			setY(y);
		}
		
		Punto p = new Punto(x,y);
		_linea.add(p);
		
	//	if(_linea.size()>1){
			recalculaPlanetas();
		//	IO.ConsoleLog.println("planetas pintats:"+ _planetas.size());
		//}
	
	}
	
	public float getDistanciaEntrePlanetas() {
		return _distancia_entre_planetas;
	}
	public void setDistanciaEntrePlanetas(float _distancia_entre_planetas) {
		this._distancia_entre_planetas = _distancia_entre_planetas;
		this._bache = -1;
		recalculaPlanetas();
	}
	public float getGrosor() {
		return _grosor;
	}
	public void setGrosor(float _grosor) {
		this._grosor = _grosor;
		this._bache = -1;
		recalculaPlanetas();
	}
	public float getBache() {
		return _bache;
	}
	public int getMasa() {
		return _masa;
	}

	public void setMasa(int _masa) {
		this._masa = _masa;
		recalculaPlanetas();
	}
	
	public boolean isActualizada() {
		return _actualizada;
	}

	public void setActualizada(boolean _actualizada) {
		this._actualizada = _actualizada;
	}

	public int getSizePuntos(){
		return _linea.size();
	}
	
	public void reformulaConBacheGrosor(double b,double r) {
		if(b>r)
			b=r;
		
		this._bache = (float)b;
		this._grosor = (float)r;
		
		this._distancia_entre_planetas = (float)(  2*Math.sqrt( r*r - ((r-b)*(r-b)) )  ) ;
		
		recalculaPlanetas();
	}
	
	private void recalculaPlanetas(){
		
		double distancia_acumulada = 0;
		double incremento = 0.5; // s'anira cap al punt desti en intervals inferiors a la distancia entre dos pixels
		
		Punto anterior ;
		Punto actual;
		Punto siguiente;
		
		setActualizada(false);
		
		if(_linea.size()==0)
			return;
		_circulares = new Vector();
		
		Iterator<Punto> it = _linea.iterator();
		anterior = it.next();
		
		Circular circular = null;
		
		if(_tipo instanceof Planeta)
			circular = new Planeta(0,(int)anterior.getX(),(int)anterior.getY(),(int)_grosor);
		if(_tipo instanceof Asteroide)
			circular = new Asteroide(((Asteroide) _tipo).getFriccion(),(int)_grosor);
		
		
		circular.setX((int)anterior.getX());
		circular.setY((int)anterior.getY());
		circular.setRadio(_grosor);
		
		_circulares.add(circular);
		
		// la distancia entre dos punts sempre sera superior a un pixel 
		// si ens anem acostant de mig pixel a mig pixel en linea recta arribarem sempre a un moment en que la distancia actual->siguiente < 0.5

		Vector2D actualAsiguiente = new Vector2D(0,0,0,0);
		
		while(it.hasNext()){
			siguiente = it.next();
			actual = anterior;
			
			actualAsiguiente.setXo(actual.getX());
			actualAsiguiente.setYo(actual.getY());
			
			actualAsiguiente.setXd(siguiente.getX());
			actualAsiguiente.setYd(siguiente.getY());
			
			while(actualAsiguiente.getModulo()>incremento){
			
				distancia_acumulada += incremento;
				actual = puntoEnLaLinea(actual,siguiente,incremento);
				
				actualAsiguiente.setXo(actual.getX());
				actualAsiguiente.setYo(actual.getY());
				
				if( distancia_acumulada > _distancia_entre_planetas ){
					
					if(_tipo instanceof Planeta)
						circular = new Planeta(0,(int)actual.getX(),(int)actual.getY(),(int)_grosor);
					if(_tipo instanceof Asteroide)
						circular = new Asteroide(((Asteroide) _tipo).getFriccion(),(int)_grosor);
					
					circular.setX((int)actual.getX());
					circular.setY((int)actual.getY());
					circular.setRadio(_grosor);
					
					_circulares.add(circular);
					distancia_acumulada = 0;
				}
			}
			anterior = siguiente;
			
		}
		
		//Iterem tots els planetas afegits donanlis una fraccio proporcional de la masa total
		if(_tipo instanceof Planeta){
			double masa_repartida = ((Planeta) _tipo).getMasa()/_circulares.size();
			Iterator itp = _circulares.iterator();
			while(itp.hasNext()){
				((Planeta)itp.next()).setMasa(masa_repartida);
			}
		}
	
	
	
	}
	
	private Punto puntoEnLaLinea(Punto origen,Punto destino,double incremento){
		Punto segmento = new Punto(destino.getX()-origen.getX(),destino.getY()-origen.getY());
		
		
		double modulo = (new Vector2D(0,0,segmento.getX(),segmento.getY())).getModulo();
		
		double incModX = (segmento.getX()/modulo)*incremento;
		double incModY = (segmento.getY()/modulo)*incremento;
		
		Punto pel = new Punto(origen.getX()+incModX, origen.getY()+incModY);
		return pel;
	}

	
	public void setX(double x){
		
		double dx = x-getX();
		resetXY(dx,0);
		
		super.setX(x);
	}
	
	public void setY(double y){
		
		double dy = y-getY();
		resetXY(0,dy);
		
		super.setY(y);
	}
	
	private void resetXY(double dx,double dy){
		Iterator<Circular> it = getPlanetas();
	
		while(it.hasNext()){
			it.next().setDXY(dx, dy);
		}
	}
	
	private Circular getPlanetaCercano(double x, double y){
		
		Circular cercano = null;
		double distancia = 999999;
		
 
		if(pcercanox==x && pcercanoy==y){// pàra ahorrar la funcion cuando se llama muchas veces para el mismo movil
		
			return pcercano;
		}
		
		Iterator<Circular> it = getPlanetas();
		Vector2D puntoAplaneta = new Vector2D(0,0,0,0);
		puntoAplaneta.setXd(x);
		puntoAplaneta.setYd(y);
		
		while(it.hasNext()){
			
			Circular act = it.next();
			puntoAplaneta.setXo(act.getX());
			puntoAplaneta.setYo(act.getY());
			
			if(puntoAplaneta.getModulo()<distancia){
				cercano = act;
				distancia = puntoAplaneta.getModulo();
			}

		}
		
		if(cercano != null){
			pcercanox = x;
			pcercanoy = y;
			pcercano = cercano;
			
			return cercano;	
		}
		IO.ConsoleLog.println("NO HAY cercano:");
		return new Planeta(0,-1000,-1000,10);
		

	}
	
	@Override
	public boolean isParteDentro(Circular m) {
		return getPlanetaCercano(m.getX(), m.getY()).isParteDentro(m);
	}

	@Override
	public boolean isTodoDentro(Circular m) {
		return getPlanetaCercano(m.getX(), m.getY()).isTodoDentro(m);
	}

	@Override
	public double tangenteSeguraSuperficie(Circular m) {
		return getPlanetaCercano(m.getX(), m.getY()).tangenteSeguraSuperficie(m);
	}

	@Override
	public double distanciaaMargen(Circular c, double normal, double margen) {
		return getPlanetaCercano(c.getX(), c.getY()).distanciaaMargen(c,normal,margen);
	}

	public Iterator<Circular> getPlanetas() {
		
		return _circulares.iterator();
	}

	@Override
	public Enumeration getEfectos() {
		
		if( !(_tipo instanceof Planeta))
			return null;
		Vector gravedades = new Vector();
		Iterator planetas = _circulares.iterator();
		
		while(planetas.hasNext()){
			Planeta p = (Planeta)planetas.next();
			gravedades.add(p.getEfectos().nextElement());
		}
		
		return gravedades.elements();
	}

	@Override
	public Enumeration getEfectosSegundos(double x, double y) { // en el rebote siempre sera con el planeta mas cercano ya que todos tienen el mismo radio
		
		if(_tipo instanceof Planeta)
			return ((Planeta)getPlanetaCercano(x, y)).getEfectosSegundos(x,y);

		if(_tipo instanceof Asteroide)
			return ((Asteroide)getPlanetaCercano(x, y)).getEfectosSegundos(x,y);
		
		IO.ConsoleLog.println("Error de tipo en get efectos de Forma, no hay tipo");
		return(null);
	}

	@Override
	public Enumeration getEfectosTerceros(double x, double y) {
		
		if(_tipo instanceof Asteroide)
			return ((Asteroide)getPlanetaCercano(x, y)).getEfectosTerceros(x,y);
		
		
		return null;
	}

	@Override
	public Enumeration getEfectosCuartos() {
		// TODO Auto-generated method stub
		return null;
	}





	
}


