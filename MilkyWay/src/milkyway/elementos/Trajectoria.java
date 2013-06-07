package milkyway.elementos;

import java.util.Iterator;
import java.util.Vector;

import milkyway.Mesa;
import milkyway.fisica.FisicaMecanica;
import milkyway.geometria.Punto;
import milkyway.geometria.Vector2D;

import milkyway.elementos.Circular;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;

public class Trajectoria extends Elemento{

		private Vector _posiciones; //TODO: Factor Vdedo i pintat de tirachinas dins de Trajectoria movil xml i amb llindar + dx*fvd+llindar 
	
		private Vector _elementos;
		private Movil _movil;
		private Vector _moviles;
		
		private FisicaMecanica _fisica;
		
		private boolean do_paint = true; 
		
		public Trajectoria(){
			super();
			_posiciones = new Vector();
			_moviles = new Vector();
			_movil = new Movil();
			_moviles.add(_movil);
			_fisica = new FisicaMecanica();
			
		}
		
		public void refresca(Movil m,double vx,double vy,Vector elementos, int iteracions){
			
			_movil.setAllAs(m);	
			_movil.setNube(null); //com que movil no tindra nube no es calculara en la simulacio 
			_movil.setV(vx, vy);
			
			setXY(_movil.getX(),_movil.getY());
			_posiciones.removeAllElements();
			
			Mesa.getInstancia().setSimulando(true); // per a no calcular les destruccions d'elementos
			
			for(int i = 0; i<iteracions;i++){
				_fisica.calculaDTFisicas(_moviles, elementos);
				this.addPunto(new Punto(_movil.getX(),_movil.getY()));
			}
			

			Mesa.getInstancia().setSimulando(false);
			
		}


		@Override
		public boolean isParteDentro(Circular m) {
			return false;
		}

		@Override
		public boolean isTodoDentro(Circular m) {
			return false;
		}

		@Override
		public double tangenteSeguraSuperficie(Circular m) {
			return 0;
		}

		
		public boolean isDo_paint() {
			return do_paint;
		}

		public void setDo_paint(boolean do_paint) {
			this.do_paint = do_paint;
		}

		public Iterator getElementsIterator() {
			return _posiciones.iterator();
		}
		
		public void addPunto(Punto p){
			_posiciones.add(p);
		}

		public Vector getPosiciones() {
		
			return _posiciones;
		}

		public double distanciaaMargen(Circular c, double normal, double margen) {
			
			return 0;
		}
		
}
