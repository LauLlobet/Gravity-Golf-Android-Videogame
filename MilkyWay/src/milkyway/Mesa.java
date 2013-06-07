package milkyway;

import java.awt.*;
import java.util.*;

import milkyway.animaciones.*;
import milkyway.elementos.*;
import milkyway.fisica.*;
import milkyway.logica.EstadoGeneral;
import milkyway.logica.LogicaEditor;
import milkyway.logica.LogicaGeneral;
import milkyway.logica.LogicaJuego;
import milkyway.logica.Pantalla;
import milkyway.logica.VectorElementos;
import milkyway.animaciones.Animacion;
import milkyway.animaciones.Animaciones;
import milkyway.elementos.Diana;
import milkyway.elementos.Elemento;
import milkyway.elementos.ListaTiradas;
import milkyway.elementos.MarcaTirada;
import milkyway.elementos.Movil;
import milkyway.fisica.FisicaMecanica;
import milkyway.geometria.Punto;


public class Mesa
{
	private static Mesa _instancia;
	
	private LogicaGeneral _logica;
	private LogicaEditor _editor;
	private LogicaJuego _juego;
	
	private Animaciones _animaciones;
	private FisicaMecanica _fismec;
    
    private Vector _lista_moviles_accion; // los moviles tambe estaran dins d'elementos
    private ListaTiradas _lista_moviles_tiradas; // los moviles tambe estaran dins d'elementos
	private VectorElementos _lista_elementos; // elementos son pintables
	
	private Pantalla _pantalla_actual;
	
	private float _milliseconds_per_tic = 5;
	private float _last_tic = 0;
	public float  _max_tic = -1;
	public float  _max_tic_temp = -1;
	public float  _mtt_counter = 0;
	public float  _mtt_refresh = 2000;
	public float  _min_tic = 99999999;
	
	public boolean fondo_invalidate = true;
	
	
	//tamaño de pantalla y zoom
	private double _escalat_pantalla = 1;
	private double _escalat_zoom = 1;
	private double _escalat_total = 1;
	
	private double _offsetX_base = 0;
	private double _offsetY_base = 0;
	private double _offsetX = 0;
	private double _offsetY = 0;
	
	public double pantalla_x = 500;
	public double pantalla_y = 800;
	
	public double centre_x = pantalla_x/2;
	double centre_y = pantalla_y/2;
	
	double m = 100;
	double d = 400;
	double mlx = pantalla_x - m;
	double mly = pantalla_y -m;
	
	//per a no destruir panetas cuan es calculen les trajectories
	
	boolean _simulando = false;
	
	public double marge_so = 0;

	public boolean _debug_flag = false;
	
	public static Mesa getInstancia(){
		if(_instancia == null)
			_instancia = new Mesa();
		return _instancia;
	}
	
	
	
	protected Mesa()
	{
		// Iniciamos estructuras de datos vacias
		_logica = new LogicaGeneral();
		_editor = new LogicaEditor();
		_juego = new LogicaJuego(); 
		
		_fismec = new FisicaMecanica();
		_animaciones = new Animaciones();
		
		_lista_moviles_accion = new Vector();
		_lista_moviles_tiradas = new ListaTiradas();
		
		_lista_elementos = new VectorElementos();
		
	}
	
	public Vector getMoviles(){
		return _lista_moviles_accion;
	}
	
	public VectorElementos getElementos(){
		return _lista_elementos;
	}
	

	public LogicaGeneral getLogicaGeneral(){
		return _logica;
	}
	
	public LogicaEditor getLogicaEditor(){
		return _editor;
	}
	public LogicaJuego getLogicaJuego(){
		return _juego;
	}
	//Funcions cridades en la instanciacio



	public void cargarPantalla(Pantalla pantalla){ //carga una pantalla desde 0 
		limpiaMesa();
		Iterator it = pantalla.iterator();
		while(it.hasNext())
			nuevoElemento((Elemento)it.next());
		nuevoElemento(_lista_moviles_tiradas);
		_pantalla_actual = pantalla;
		
	}
	
	public void resetearPantalla(Pantalla pantalla){// seteiga la pantalla a inici
		
		/*Iterator it = _pantalla_actual.iterator();
		while(it.hasNext())
			eliminaElemento(((Elemento)it.next()));
		
		it = pantalla.iterator();
		while(it.hasNext())
			nuevoElemento((Elemento)it.next());
		_pantalla_actual = pantalla;*/
		
		Vector to_delete = new Vector();
		Iterator it = _lista_elementos.iterator();
		while(it.hasNext()){
			Elemento i = (Elemento)it.next();
			if( !(i instanceof MarcaTirada)  && !(i instanceof Diana) && !(i instanceof ListaTiradas) ) // llista d'elements que no es borren entre pantalles
				to_delete.add(i);
		}
		
		it = to_delete.iterator();
		while(it.hasNext())
			eliminaElemento((Elemento)it.next());
		
		it = pantalla.iterator();
		while(it.hasNext()){
			Elemento i = (Elemento)it.next();
			if(! (i instanceof Diana ) && !(i instanceof Movil))
				nuevoElemento(i);
		}
		
		if(_lista_moviles_tiradas.size()>0){
			_lista_moviles_accion.removeAllElements();
			Movil siguiente = _lista_moviles_tiradas.popMovil();
			
			nuevoElemento(siguiente);
			_lista_moviles_tiradas.remove(siguiente);
		}
		
		
		_pantalla_actual = pantalla;
	}
	
	public ListaTiradas getMovilesTiradas(){
		return _lista_moviles_tiradas;
	}
	public void salvarPantallaActual(){

		String pantalla = _pantalla_actual.save();
		IO.PantallaWriter.wirtePantalla(pantalla);
		
		
		
	}
	
    //metodes de afegir elements
    public void nuevoElemento(Elemento e){
        if (e instanceof Movil){
        	//if(_lista_moviles_accion.size() == 0){
        		_lista_moviles_accion.add(e);
        	/*	 this._lista_elementos.add(e);
        	}else{
        		_lista_moviles_tiradas.add(e);
        	}
        }else*/}
        	 this._lista_elementos.add(e);
    }
    
    public void eliminaElemento(Elemento e){
        _lista_elementos.remove(e);
        _animaciones.eliminaAnimacion(e);
        if (e instanceof Movil) _lista_moviles_accion.remove(e);
    }
    
    public void nuevaAnimacion(Elemento e, Animacion a){
    	_animaciones.nuevaAnimacion(e, a);
    }
  /*  
    public Planeta nuevoPlaneta(int x, int y, double masa, double radio){
        Planeta p = new Planeta(x,y,0.0,0.0,masa,radio,this);
        this._lista_elementos.add(p);
        return p;
    }
    
    public Movil nuevoMovil(int x, int y, double masa, double radio){
    	Movil m = new Movil(x,y,10d,10d,0d,20d,0d,0d,this);
        this._lista_elementos.add(m);
        this._lista_moviles.add(m);
        return m;
    }*/
    
    
    //metodo que limpia la mesa
    public void limpiaMesa(){
    	Elemento e;
    	int cantidad_elementos = _lista_elementos.size();
        for (int i=cantidad_elementos-1;i!=-1;i--) {
        	e = (Elemento)_lista_elementos.get(i);
            _lista_elementos.remove(i);
            if (e instanceof Movil) 
            	_lista_moviles_accion.remove(e);
        }
        _lista_moviles_tiradas.removeAllElements();
    }
    
    public Vector getDianas(){
    	Iterator elementos = this._lista_elementos.iterator();
    	Vector ans = new Vector();
    	Object tmp;
    	while (elementos.hasNext()) {
    		tmp = elementos.next();
    		if( tmp instanceof Diana)
    			ans.add(tmp);
    	}
    	return ans;
    }
  
    //metodes de gestio del rellotge
    
    public void doFisica(){
        _fismec.calculaDT(_lista_moviles_accion,_lista_elementos );

       
        
    }
    public void doLogica(){
    	_logica.doLogica();
        calculaZoom();
        return;
    }
    
    public void doAnimaciones(){
    	_animaciones.anima();
    }
    
    
    private void calculaZoom(){
    	_escalat_zoom = 1;
		_offsetX = _offsetX_base;
		_offsetY = _offsetY_base; 
	
		
		double x;	// x real
		double xp; // x pixel
		double y;
		double yp;
		
		double Xescalat_zoom = 1;
		double Yescalat_zoom = 1;
		
		if(getMoviles().size() == 0)
			return;
		
		Movil mov =(Movil)getMoviles().firstElement();
		xp = x = mov.getX();
		yp = y = mov.getY(); 
		
		if( x < m )
			xp = funcioCuadMargeInferior(m, d, x);
		if( x > mlx )
			xp = funcioCuadMargeSuperior(m, mlx, d, x);
		if( y < m )
			yp = funcioCuadMargeInferior(m, d, y);
		if( y > mly )
			yp = funcioCuadMargeSuperior(m, mly, d, y);
		
		Xescalat_zoom = ( 250 - xp)/( 250 - x);
		Yescalat_zoom = ( 400 - yp)/( 400 - y);
		
		if(Xescalat_zoom < Yescalat_zoom)
			_escalat_zoom = Xescalat_zoom;
		else
			_escalat_zoom = Yescalat_zoom; 		
		
		_offsetX = 250 - _escalat_zoom * 250;
		_offsetY = 400 - _escalat_zoom * 400;
		
		
		_escalat_total = _escalat_zoom * _escalat_pantalla;
		
		_offsetX = _offsetX * _escalat_pantalla;
		_offsetY = _offsetY * _escalat_pantalla;
    }
    
    private double funcioCuadMargeInferior(double m, double l, double x){
    	double a = m/((l)*(l));
    	return a * (x+(l-m)) * (x+(l-m));
    }
    
    private double funcioCuadMargeSuperior(double m,double ml, double l, double x){
    	double a = - m/((l)*(l));
    	return a * (x-(ml+l)) * (x-(ml+l))+ml+m;
    }
    
    public Punto pixelToReal(double x, double y){
    	
    	return new Punto((x-_offsetX)/_escalat_pantalla,(y-_offsetY)/_escalat_pantalla);
    }
    


	public void setScreenSize(int x, int y) {
		
		double proporcio = (double)x/(double)y;
		
		if(proporcio > 500f/800f) //sobra x, y es el referent
			_escalat_pantalla = (double)y/(double)800f;
		else
			_escalat_pantalla = (double)x/(double)500f;
		
	}
	
    
    public double getescalat(){
    	return _escalat_total;
    }

    public double getoffsetX(){
    	return _offsetX;
    }
    
    public double getoffsetY(){
    	return _offsetY;
    }
    
	public void setFisicaOff(){
    	_fismec.setFisicaOff();
    }
    
    public void doTimed(float lapse){ // NOMES ES CRIDA EN LA VERSIO ANDROID !
    	
   
    	float total_time = lapse + _last_tic;
    	float next_last_tic = total_time % _milliseconds_per_tic;
    	
    	float ftics = (total_time - next_last_tic)/ _milliseconds_per_tic;
     	int tics = (int)ftics;
     	
     	_last_tic = next_last_tic;
     	
     	for(int i=0;i<tics;i++){
     		doLogica();
     		doAnimaciones();
     	}
     	
     	if(lapse < _min_tic)
     		_min_tic = lapse;
     	if(lapse > _max_tic)
     		_max_tic = lapse;
     	if(lapse > _max_tic_temp)
     		_max_tic_temp = lapse;
     	
     	_mtt_counter += lapse;
     	if(_mtt_counter > _mtt_refresh){
     		_max_tic_temp = -1;
     		_mtt_counter = 0;
     	}
    	
    	
     //	IO.ConsoleLog.println("lapse:"+lapse);
    }
    

    
    public boolean isParado(){
    	return _fismec.is_movil_parado();
    }
    
    public boolean isTemblando(){
    	return _fismec.is_movil_temblando();
    	
    }
    
    public void setParado(boolean b){
    	 _fismec.set_is_movil_parado(b);
    }
    
    public void setTemblando(boolean b){
    	 _fismec.set_is_movil_temblando(b);
    	
    }

    public boolean isSimulando(){
    	return _simulando;
    }
    
    public void setSimulando(boolean sim){
    	_simulando = sim;
    }
    
}
