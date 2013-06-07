package milkyway.elementos;

import IO.ConsoleLog;
import milkyway.geometria.Vector2D;

public class Imagen extends Rectangulo {

	String _id;
	
	int[][] _img_alpha;
	int[][] _colisiones;
	int[][] _circulo;
	int _radi = 0;
	
	public void carrega(String id){
		_id = id;
		int[] dim = new int[2];
		_img_alpha = null;
		IO.ImageReader.readImage(_id,dim,_img_alpha);
		setXd(dim[0]);
		setYd(dim[1]);
		_img_alpha = new int[dim[0]][dim[1]];
		IO.ImageReader.readImage(_id,dim,_img_alpha);
		
	}
	
	public void dibujaCirculo(int radi){
		_circulo = new int[2*radi][2*radi];
		Vector2D dist = null;
		
		for(int y=0;y<2*radi;y++){
			for(int x=0;x<2*radi;x++){
				
				dist = new Vector2D(radi,radi,x,y);
				if(dist.getModulo()<=radi)
					_circulo[x][y]=1;
				else
					_circulo[x][y]=0;
			}
		}
	}
	
	
	public void expandeixPerColisions(int radi){
		
		_radi = radi;
		_colisiones = new int[ (int)getXd() + 2*radi][ (int)getYd() + 2*radi];
		
		int x,y,color;
		
		for(y=0;y<getYd()+ 2*radi;y++){
			for(x=0;x<getXd()+ 2*radi;x++){
				_colisiones[x][y]=0;
			}
		}
			
		
		
		dibujaCirculo(_radi);
		 

		for(y=0;y<getYd();y++){
			for(x=0;x<getXd();x++){
				
				color=_img_alpha[x][y];
				if(color != 0){ // si no es transparent
					
					for(int ye=y-_radi;ye<y+_radi;ye++){
						for(int xe=x-_radi;xe<x+_radi;xe++){
							
							int xcirc = xe+_radi-x;
							int ycirc = ye+_radi-y;
							if(_circulo[xcirc][ycirc] != 0)
								setColisiones(xe, ye, 1);
							
						}
					}
					
				}
			}
		}
		
		
	}
	
	public int getColisions(int x,int y){
		
		if(x<-_radi || y<-_radi || x>=getXd()+_radi || y>=getYd()+_radi )
			return 0;
		
		return _colisiones[x+_radi][y+_radi];
	}
	public void setColisiones(int x, int y, int val){
		_colisiones[x+_radi][y+_radi] = val;
	}
	
	public boolean getAlphaAbs(int xabs,int yabs){
		
		int x = xabs - (int)getX();
		int y = yabs - (int)getY();
		
		if(x<0 || y<0 || x>=getXd() || y>=getYd() )
			return false;
		
		if(_img_alpha[x][y] != 0)
			return true;
		else
			return false;
	}
	
	public int getRadiColisio(){
		return _radi;
	}
	
	public boolean isParteDentro(Circular m){
		
		int dx = (int)m.getX()-(int)getX();
		int dy = (int)m.getY()-(int)getY();
	
		
		if(getColisions(dx, dy)!=0)
			return true;
		
		return false;
	}
	
	public boolean isParteDentro(double x, double y){
		
		int dx = (int)x -(int)getX();
		int dy = (int)y -(int)getY();
	
		
		if(getColisions(dx, dy)!=0)
			return true;
		
		return false;
	}
	
	public boolean isTodoDentro(Circular m){
		return false;
	}


	public double distanciaaMargen(Circular c, double normal, double margen) {
		
		Vector2D inc = new Vector2D(0,0,0.2,0);
		inc.rotate(normal);
		
		double x = c.getX();
		double y = c.getY();
		int d=0;
		
		for(d=0; isParteDentro(x, y);d++){
			
			if(d>100){
				ConsoleLog.println("Error: distancia margen imatge massa llarga");
				return 0;
			}
			
			x+= inc.getDX();
			y+= inc.getDY();
			
		}
		
		return d*0.2-margen;
	}
	
	public double tangenteSeguraSuperficie(Circular m){
	
		double radio = m.getRadio(); // un interval es l'angle on hi ha espai per a sortir, aquesta funcio troba la bisectriu de l'interval mes gran
		int cx = (int)m.getX();
		int cy = (int)m.getY();
		
		double inc_angulo = Math.PI/(2*3*radio);

		double uio = -1; //ultim interval obert
		double pitso = -1; // primer interval tancat sense obertura ( cas mes comu )
		
		double imax = -1; // interval amb angle maxim
		double imaxo = -1; // obertura interval amb angle maxim
		double imaxt = -1; // tancament interval amb angle maxim
		
		Vector2D vecta = new Vector2D(0,0,radio,0);
		boolean color_anterior = getAlphaAbs(cx+(int)vecta.getDX(), cy+(int)vecta.getDY());
		
		
		for(double angulo=0; angulo<2*Math.PI ;angulo=angulo+inc_angulo){
			
			Vector2D vect = new Vector2D(0,0,radio,0);
			vect.rotate(angulo);
			boolean color = getAlphaAbs(cx+(int)vect.getDX(), cy+(int)vect.getDY());
			//color false == transparent
			
			//IO.ConsoleLog.println("color: "+color+ "color ant: "+color_anterior);
			
			
			if( color == false && color_anterior == true ){ // obertura d'interval
				uio = angulo;
			}
			
			if( color == true && color_anterior == false ){ // tancament d'interval
				
				if(uio == -1){
					pitso = angulo;
				}
				
				if( uio != -1 && imax < angulo - uio){
					imax = angulo - uio;
					imaxo = uio;
					imaxt = angulo;
				}
			}
			color_anterior = color;
		}
		
		if(uio == -1){
			IO.ConsoleLog.println("S'ha intentat trobar la tangent de coliso a una imatge sense exit");
			return 0;
		}
		
		if(pitso != -1 && imax < pitso + 2*Math.PI - uio){ // el primer que s'ha trobat es un un tancament la obertura del interval  coincideix amb l'ultim uio

			imax = pitso + 2*Math.PI - uio;
			imaxo = uio;
			imaxt = pitso + 2*Math.PI;
			
		}
		
		return imax/2 + imaxo + Math.PI/2; // s'ha de tornar una tangent a la que -pi/2 sigui la normal
	}

	public String getId() {
		return _id;
	}
	
	
	/*
	 * public double tangenteSeguraSuperficie(Circular m){
	
		double radio = m.getRadio();
		int cx = (int)m.getX();
		int cy = (int)m.getY();
		
		double inc_angulo = 3*radio*4/2*Math.PI;
		double angulo_inicio = -1;
		double angulo_fin = -1;
		boolean caso_especial = false;
		
		Vector2D vecta = new Vector2D(0,0,radio,0);
		boolean color_anterior = getAlphaAbs(cx+(int)vecta.getDX(), cy+(int)vecta.getDY());
		
		if( color_anterior == true ) // si el primer pixel no es transparent
			caso_especial = true;
		
		for(double angulo=0; angulo<2*Math.PI ;angulo=angulo+inc_angulo){
			
			Vector2D vect = new Vector2D(0,0,radio,0);
			vect.rotate(angulo);
			boolean color = getAlphaAbs(cx+(int)vect.getDX(), cy+(int)vect.getDY());
			
			if( color == true && color_anterior == false && angulo_inicio == -1){
				angulo_inicio = angulo;
			}
			
			
			if( color == false && color_anterior == true ){
				angulo_fin = angulo;
			}
		}
		
		if(angulo_fin == -1 || angulo_inicio == -1){
			IO.ConsoleLog.println("S'ha intentat trobar la tangent de coliso a una imatge sense exit");
			return 0;
		}
		
		if(caso_especial){
			if(angulo_fin < ( 2*Math.PI- angulo_inicio) )
				angulo_fin = 2*Math.PI + angulo_fin;
			else
				angulo_inicio = angulo_inicio - 2*Math.PI;
		}
		
		return ((angulo_fin+angulo_inicio)/2)+ Math.PI + Math.PI/2; // s'ha de tornar una tangent a la que -pi/2 sigui la normal
	}

	public String getId() {
		return _id;
	}*/
	
	
}
