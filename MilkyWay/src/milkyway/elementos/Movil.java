package milkyway.elementos;

import java.util.Enumeration;
import java.util.Vector;

import milkyway.*;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.efectos.EfectoGravedad;
import milkyway.fisica.efectos.EfectoRebote;
import milkyway.geometria.*;
import milkyway.logica.NubePolvo;
import milkyway.logica.ResManager;

import milkyway.elementos.Masa;
import milkyway.elementos.Movil;
import milkyway.elementos.Trajectoria;


public class Movil extends Masa implements LoadSaveXML, AfectaMovil {

		//tiene una velocidad propia
		private double _vx= XMLKeys.emptyDouble;
		private double _vy= XMLKeys.emptyDouble;
		// tiene una cola
		private double _colaLengthFactor= XMLKeys.emptyDouble;
		
		private double _fx= 0;
		private double _fy= 0;
        
        private Movil _anterior; 
        
        private NubePolvo _nube = null;
        
        public double _tirachinas_x = 0;//TODO: set private
        public double _tirachinas_y = 0;
        
        private Trajectoria _trajectoria;
		
        private double _x_guardada = 0;
        private double _y_guardada = 0;
        
        private Vector _efectos_segundos;
        private Vector _efectos_primeros;
        
        //veure efecto rebote, es per a no calcular la parella de rebots dos cops un per cada movil
    	private Vector _movils_ja_rebotats; 
        
		public Movil(){
			super();
			
		/*	  //[CF]cargamos la textura del movil // TODO: XML cargar texturas
            _textura = Toolkit.getDefaultToolkit().getImage( "movil.png" );
           _textura_cola = Toolkit.getDefaultToolkit().getImage( "movil_cola.png" );
            MediaTracker tracker = new MediaTracker( ms );
            tracker.addImage(_textura,1 );
            tracker.addImage(_textura_cola,2 );
            try {
                if( !tracker.waitForID( 1,10000 ) ) {
                        IO.ConsoleLog.println( "Error en la carga de la imagen" );
                        System.exit( 1 ); 
                }
                if( !tracker.waitForID( 2,10000 ) ) {
                        IO.ConsoleLog.println( "Error en la carga de la imagen" );
                        System.exit( 1 ); 
                }
            } catch( InterruptedException e ) {
                    IO.ConsoleLog.println( e );
            }*/
			_efectos_segundos = new Vector();
			_efectos_primeros = new Vector();
			EfectoRebote e = new EfectoRebote(this,-0.02,-0.02);
			_efectos_segundos.add(e);
			_efectos_primeros.add(new EfectoGravedad(this));
			_movils_ja_rebotats = new Vector();
			
			
		}
		
		private boolean atributosIncompletosMovil(){
			if(_vx==XMLKeys.emptyDouble || _vy==XMLKeys.emptyDouble || _colaLengthFactor ==XMLKeys.emptyDouble )
				return true;
			return false;
		}
		
		public int parse(NBEasyXML xml,int idx){
			IO.ConsoleLog.println("parseando movil");
			
			int didx = xml.findChildElement(idx); //idx apunta a Movil, didx apunta al primer atribut de elemento
			int cidx = super.parse(xml,didx); //cidx apunta al nextPeer de l'ultim atribut de masa 

			while(atributosIncompletosMovil()){
				int id = XMLKeys.identify(xml.getName(cidx));
				switch(id){
					case XMLKeys.vx:
						setVx(xml.getDoubleContent(cidx));
						cidx = xml.findNextPeer(cidx);
					break;
					case XMLKeys.vy:
						setVy(xml.getDoubleContent(cidx));
						cidx = xml.findNextPeer(cidx);
					break;
					case XMLKeys.cola_length:
						setColaLengthFactor(xml.getDoubleContent(cidx));
						cidx = xml.findNextPeer(cidx);
					break;
					case XMLKeys.nubePolvo:
						_nube = new NubePolvo();
						cidx = _nube.parse(xml,cidx);
					break;
					default:
						IO.ConsoleLog.println("XML tag en Movil no reconocida");
					
					}	
			}
			
			if(_nube == null){
				_nube = new NubePolvo();
				_nube.initDefault();
			}
				
			

			
			
			_anterior = new Movil();
			_anterior.setAllAs(this);
			
			guardaPosicion();
			
			return xml.findNextPeer(idx);
		}
		
		public String save(){
			return "\t<"+XMLKeys.id2str(XMLKeys.movil)+">\n"+
					super.save()+
					"\t\t<"+XMLKeys.id2str(XMLKeys.vx)+">"+getVx()+"</"+XMLKeys.id2str(XMLKeys.vx)+">\n"+
					"\t\t<"+XMLKeys.id2str(XMLKeys.vy)+">"+getVy()+"</"+XMLKeys.id2str(XMLKeys.vy)+">\n"+
					"\t\t<"+XMLKeys.id2str(XMLKeys.cola_length)+">"+getColaLength()+"</"+XMLKeys.id2str(XMLKeys.cola_length)+">\n"+
					_nube.save()+
					"\t</"+XMLKeys.id2str(XMLKeys.movil)+">\n";
		}
		
		public void setColaLengthFactor(double clf){
			_colaLengthFactor = clf;
		}
		public double getColaLength(){
			return _colaLengthFactor;
		}

		public void setV(double vx,double vy)
		{
			this._vx=vx;
			this._vy=vy;
		}
		public void setDV(double dvx,double dvy)
		{

			this._vx+=dvx;
			this._vy+=dvy;
		}
		
		public void setF(double fx,double fy)
		{
			this._fx=fx;
			this._fy=fy;
		}
		public void setDF(double dvx,double dvy)
		{

			this._fx+=dvx;
			this._fy+=dvy;
		}
		
		public void setVx(double vx){
			 _vx = vx;
		}
		
		public void setVy(double vy){
			 _vy = vy;
		}
		
		public double getVx()
		{
			return _vx;
		}
		public double getVy()
		{
			return _vy;
		}

		public double getFx()
		{
			return _fx;
		}
		public double getFy()
		{
			return _fy;
		}
		
		public void setX(double x){
			if(_anterior != null ) setXAnterior(getX());
			super.setX(x);
	
		}
		public void setY(double y){
			if(_anterior != null ) setYAnterior(getY());
			super.setY(y);
		}
		
		public void setTirachinas(double x, double y){
			_tirachinas_x = x;
			_tirachinas_y = y;
		}

		public void setXAnterior(double x){
			_anterior.setX(x);
		}
		public void setYAnterior(double y){
			_anterior.setY(y);
		}
		
		
		public Movil getAnterior(){
			return _anterior;
		}
		
		public NubePolvo getNube(){
			return _nube;
		}
		
		public double getTirachinasX(){
			return _tirachinas_x;
		}
		public double getTirachinasY(){
			return _tirachinas_y;
		}
		
		public double getTirachinasXRelatiu(){
			return Mesa.getInstancia().getoffsetX()+ _tirachinas_x*Mesa.getInstancia().getescalat();
		}
		public double getTirachinasYRelatiu(){
			return Mesa.getInstancia().getoffsetY()+ _tirachinas_y*Mesa.getInstancia().getescalat();
		}
		
		public void guardaPosicion(){
			_x_guardada = getX();
			_y_guardada = getY();
		}
		
		public void restauraPosicion(){
			setXY(_x_guardada,_y_guardada);
		}
		
		public String toString(){
			return "x:"+getX()+"y:"+getY()+"vx:"+getVx()+"vy:"+getVy();
		}

		public void setAllAs(Movil m) {
			
			if( _anterior != null){
				_anterior.setXY(m._anterior.getX(), m._anterior.getY());
				_anterior.setV(m._anterior.getVx(), m._anterior.getVy());
				_anterior.setRadio(m._anterior.getRadio());
				_anterior.setMasa(m._anterior.getMasa());
			}else{
				_anterior = new Movil();
				_anterior.setXY(m.getX(), m.getY());
				_anterior.setV(m.getVx(), m.getVy());
				_anterior.setRadio(m.getRadio());
				_anterior.setMasa(m.getMasa());
			}
			setXY(m.getX(), m.getY());
			setV(m.getVx(), m.getVy());
			setRadio(m.getRadio());
			setMasa(m.getMasa());
		}

		public Trajectoria getTrajectoria() {
			return _trajectoria;
		}

		public void setTrajectoria(Trajectoria _trajectoria) {
			this._trajectoria = _trajectoria;
		}

		public void setNube(NubePolvo n) {
			_nube = n;
			
		}

		
		public Enumeration getEfectos()
		{
			return  _efectos_primeros.elements();
		}
		

		public Enumeration getEfectosSegundos(double x, double y)
		{
			return  _efectos_segundos.elements();
		}
		
		@Override
		public Enumeration getEfectosTerceros(double x, double y) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration getEfectosCuartos() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public boolean isInMovilsJaRebotats(Movil m){
			
			return _movils_ja_rebotats.contains(m);
		}
		
		public void emptyRebotats(){
			_movils_ja_rebotats.removeAllElements();
		}
		
		public boolean hanRebotado( Movil m){
			return ( this._movils_ja_rebotats.contains(m) || m.isInMovilsJaRebotats(this) );
		}

		public void setHanRebotat(Movil m_otro) {
			_movils_ja_rebotats.add(m_otro);
		}
		

}
