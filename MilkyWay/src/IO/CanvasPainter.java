package IO;

import java.util.Iterator;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import milkyway.Mesa;
import milkyway.elementos.Asteroide;
import milkyway.elementos.Circular;
import milkyway.elementos.Diana;
import milkyway.elementos.Elemento;
import milkyway.elementos.ListaTiradas;
import milkyway.elementos.MarcaTirada;
import milkyway.elementos.Movil;
import milkyway.elementos.Forma;
import milkyway.elementos.Planeta;
import milkyway.elementos.Polvo;
import milkyway.elementos.Rectangulo;
import milkyway.elementos.Trajectoria;
import milkyway.elementos.ZonaInercia;
import milkyway.geometria.Punto;
import milkyway.geometria.Vector2D;

public class CanvasPainter{

	Paint _gris;
	Paint _azul;
	Paint _verde;
	Paint _rojo;

	Vector<Forma> _paredes;
	Bitmap _imagenParedes;
	Canvas _canvasParedes;
	
	public CanvasPainter(){
	      
		_gris = new Paint();
		_gris.setAntiAlias(true);
		_gris.setARGB(255, 180, 180, 180);
		
		_azul = new Paint();
		_azul.setAntiAlias(true);
		_azul.setARGB(255, 0, 0, 180);
		
		_verde = new Paint();
		_verde.setAntiAlias(true);
		_verde.setARGB(255, 0, 120, 0);
		
		
		_rojo = new Paint();
		_rojo.setAntiAlias(true);
		_rojo.setARGB(255, 120, 0, 0);
		
		_paredes = new Vector<Forma>();
        _imagenParedes = Bitmap.createBitmap(500,800, Config.ARGB_8888);
        _canvasParedes = new Canvas(_imagenParedes);
	}

	public void paintComponent(Canvas g) {
	
		
        //pintar fondo
        //g.drawImage(_fondo, imageTransform, this);
		g.drawColor(Color.BLACK);
	       
		Movil m = null;
		_paredes.clear();
		
		boolean redibujarImagenParedes = false;
		
        //Pintamos todos los elementos
       for (int i=0;i<Mesa.getInstancia().getElementos().size();i++){
            Elemento e = ((Elemento)Mesa.getInstancia().getElementos().get(i));
            
            if(e instanceof Circular){
            	if( e instanceof Diana ){
            		paintDiana(g,(Diana)e);
            		continue;
            	}
            	if( e instanceof Planeta ){
            		paintPlaneta(g,(Planeta)e);
            		continue;
            	}
            	if( e instanceof Movil ){
            		m = (Movil)e;
            		continue;
            	}
            	if( e instanceof MarcaTirada ){
            		paintMarcaTirada(g,(MarcaTirada)e);
            		continue;
            	}
            	if( e instanceof Polvo ){
            		paintPolvo(g,(Polvo)e);
            		continue;
            	}
            	paintCircular(g,(Circular)e);
            	continue;
            }
            if(e instanceof Rectangulo){
            	
            	if( e instanceof ListaTiradas ){
            		paintListaTiradas(g,(ListaTiradas)e);
            		continue;
            	}
              	if( e instanceof ZonaInercia ){
            		paintZonaInercia(g,(ZonaInercia)e);
            		continue;
            	}
              	paintRectangulo(g,(Rectangulo)e);
              	continue;
            }
            if(e instanceof Trajectoria){
            	paintTrajectoria(g,(Trajectoria)e);
            }
            if(e instanceof Forma){
            	//paintPared(g, (Pared) e ); demasiado lento
            	if( !((Forma)e).isActualizada() ){
            		redibujarImagenParedes = true;
            		((Forma)e).setActualizada(true);
            	}
            	_paredes.add((Forma)e);
            }
            
        }
       
       if(redibujarImagenParedes){
    	   dibujarImagenParedes();
       }
       
       paintImagenPared(g);
       
       if(m!=null)
    	   paintMovil(g,m);

       g.drawText("minf: "+Mesa.getInstancia()._min_tic, 0, 20, _rojo);
       g.drawText("maxf: "+Mesa.getInstancia()._max_tic, 0, 10, _rojo);
       g.drawText("maxt: "+Mesa.getInstancia()._max_tic_temp, 0, 30, _rojo);
       
	}
	
	private void dibujarImagenParedes(){
		
		Iterator<Forma> paredes = _paredes.iterator();
		while(paredes.hasNext()){
			Forma paredactual = paredes.next();
			Iterator<Circular> planetas = paredactual.getPlanetas();
			while(planetas.hasNext()){
				Circular p = planetas.next();
				if(p instanceof Planeta)
					paintPlaneta(_canvasParedes, (Planeta)p);
				if(p instanceof Asteroide)
					paintCircular(_canvasParedes, (Asteroide)p);
			}
		}
	}
	
	
	private void paintImagenPared(Canvas g) {
		g.drawBitmap(_imagenParedes, 0,0, null);
		
	}
	
	private void paintTrajectoria(Canvas g, Trajectoria e) {
		if(!e.isDo_paint())
			return;
		Iterator it = e.getPosiciones().iterator();
		Punto p_anterior = new Punto(e.getX(),e.getY());
		
		while(it.hasNext()){
			Punto p = (Punto)it.next();
			
		/*	Vector2D segmento = new Vector2D(p_anterior.getX(),p_anterior.getY(),p.getX(),p.getY());
			int longitud = (int)segmento.getModulo();
			Shape linea = new  Rectangle2D.Float(0,0,longitud,2);
			AffineTransform  trans2 = AffineTransform.getTranslateInstance(p_anterior.getX(),p_anterior.getY());
			linea =  trans2.createTransformedShape(linea);

			AffineTransform  rotacionLinea = AffineTransform.getRotateInstance(segmento.getAngle(),p_anterior.getX(),p_anterior.getY());
			linea = rotacionLinea.createTransformedShape(linea);

			g.setPaint(Color.pink);
			g.fill(linea);

			p_anterior = p;*/
		}		
	}


	private void paintZonaInercia(Canvas g, ZonaInercia e) {
		paintRectangulo(g, (Rectangulo)e);
		
	}


	private void paintRectangulo(Canvas g, Rectangulo e) {
		int xCord = (int)e.getX();
		int yCord = (int)e.getY();	
		
		/*Shape square = new Rectangle2D.Double(xCord, yCord,e.getXd(), e.getYd());
		Color color= Color.orange;
		g.setPaint(color);
		g.draw(square);
		g.setPaint(Color.white);*/
	}


	private void paintListaTiradas(Canvas g, ListaTiradas e) {
		/*paintRectangulo(g, (Rectangulo)e);
		Iterator it = e.getListaMoviles().iterator();
		while(it.hasNext()){
			paintMovil(g,(Movil)it.next());
		}*/
		
	}


	private void paintCircular(Canvas g, Circular e) {
     
        g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _verde);
        
		/*double radio= e.getRadio() ;
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		Color color= Color.blue;
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);
		
		
		square = new  Ellipse2D.Float((int)(getX()),(int)(getY()),(int)(5),(int)(5));
		color= Color.orange;
		
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);*/
		
	}


	private void paintPolvo(Canvas g, Polvo e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		
		
		// AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord,yCord);
        //g.drawImage(e._textura, imageTransform, null);//TODO: pintat escalant amb el radi
		
		RectF rect = new RectF(xCord, yCord, xCord+5, yCord-5);		
		g.drawRect(rect, _verde);			
		
	/*	Shape square = new Rectangle2D.Double(xCord, yCord,5,5);
		Color color= Color.orange;
		g.setPaint(color);
		g.draw(square);
		g.setPaint(Color.white);*/
		
	}


	private void paintMarcaTirada(Canvas g, MarcaTirada e) {
		/*int xCord = (int)e.getX();
		int yCord = (int)e.getY();	
		
		 AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord-3,yCord-3);
        g.drawImage((Image)_texture_hash.get(e.getTexturaLocation()), imageTransform, null);//TODO: pintat escalant amb el radi
        */
		
		g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _azul);
	       
		
	}

	private void paintMovil(Canvas g, Movil e) {
	
		g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _azul);
		
		g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(),5, _rojo);
		

          /*          //Pintar el movil
		Shape square = new  Ellipse2D.Float(0,0,(int)e.getRadio()*2,(int)e.getRadio()*2);
		AffineTransform  trans = AffineTransform.getTranslateInstance(xCord-e.getRadio(),yCord-e.getRadio());
		square = trans.createTransformedShape(square);
		g.setPaint(Color.gray);
		g.fill(square);
		g.setPaint(Color.white);
		
		square = new  Ellipse2D.Float((int)(getX()),(int)(getY()),(int)(5),(int)(5));
		Color color= Color.orange;
		
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);
		
		//pintem el tirachinas
		
		if( e.getTirachinasX() != 0 && e.getTirachinasY() != 0){
			
			Vector2D tirachinas = new Vector2D(e.getX(),e.getY(),e.getTirachinasX(),e.getTirachinasY());
			int longitud = (int)tirachinas.getModulo();
			Shape linea = new  Rectangle2D.Float(0,0,longitud,1);
			AffineTransform  trans2 = AffineTransform.getTranslateInstance(e.getX(),e.getY());
			linea =  trans2.createTransformedShape(linea);
Bitmap.createBitmap
			AffineTransform  rotacionLinea = AffineTransform.getRotateInstance(tirachinas.getAngle(),e.getX(),e.getY());
			linea = rotacionLinea.createTransformedShape(linea);
			
			g.setPaint(Color.red);
			g.fill(linea);
			//g.setPaint(Color.white);
		}
		
		paintTrajectoria(g,e.getTrajectoria());*/
		
	}


	private void paintPlaneta(Canvas g, Planeta e) {
		
		if(e.getMasa() == 0)
			g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _gris);
		else
			g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _verde);
		/*
		double radio= e.getRadio();
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		Color color= Color.blue;
		
		if(e.getMasa()==0)
			color= Color.BLACK;
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);
		
		
		square = new  Ellipse2D.Float((int)(getX()),(int)(getY()),(int)(5),(int)(5));
		color= Color.orange;
		
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);*/
		
		
	}


	private void paintDiana(Canvas g, Diana e) {
		
		if(e.is_diana_final())
			g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _rojo);
		else
			g.drawCircle((int)e.getXRelatiu(), (int)e.getYRelatiu(), (int)e.getRadioRelatiu(), _rojo);
		
		
		/*int xCord = (int)e.getX();
		int yCord = (int)e.getY();	
		
		 AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord-30,yCord-30);
		 g.drawImage((Image)_texture_hash.get(e.getTexturaLocation()), imageTransform, null);//TODO: pintat escalant amb el radi
	*/
		
	}


}
