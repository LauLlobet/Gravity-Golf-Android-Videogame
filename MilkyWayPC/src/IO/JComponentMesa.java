package IO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import milkyway.Mesa;
import milkyway.elementos.*;
import milkyway.geometria.Punto;
import milkyway.geometria.Vector2D;

import milkyway.elementos.Circular;
import milkyway.elementos.Diana;
import milkyway.elementos.Elemento;
import milkyway.elementos.Imagen;
import milkyway.elementos.ListaTiradas;
import milkyway.elementos.MarcaTirada;
import milkyway.elementos.Movil;
import milkyway.elementos.Planeta;
import milkyway.elementos.Polvo;
import milkyway.elementos.Rectangulo;
import milkyway.elementos.Trajectoria;
import milkyway.elementos.ZonaInercia;


public class JComponentMesa  extends JComponent{

	private BufferedImage _fondo;//TODO: atributo de la pantalla, convertir en elemento
	private JFrame _fs;
	private TextureHashtable _texture_hash;
	private MediaTracker tracker ;
	
	public JComponentMesa( JFrame nfs){
		//Questions de Jcomponent i pantalla
		_fs = nfs;
		this.setDoubleBuffered(true);
		
		_texture_hash = new TextureHashtable();
		
		tracker = new MediaTracker( this );
		
		//Cargamos fondo
		cargarFondo();
	}
	
	
	public void cargarFondo(){
		   
		
			try {
			    _fondo = ImageIO.read(new File("solucio_pantalla1.png"));
			} catch (IOException e) {
	            System.out.println( "Error en la carga de la imagen"+ "solucio_pantalla1.png" );
                System.exit( 1 );        
			}

	        tracker.addImage( _fondo,1 );
	        try {
	            if( !tracker.waitForID( 1,10000 ) ) {
	                    System.out.println( "Error en la carga de la imagen Tracker" );
	                    System.exit( 1 );        
	        }
	        } catch( InterruptedException e ) {
	                System.out.println( e );
	        }
			
			Mesa.getInstancia().fondo_invalidate = false;
	        
	}
    
	public void paintComponent(Graphics g2) {
	
		Graphics2D g = (Graphics2D) g2;
		
		if(Mesa.getInstancia().fondo_invalidate == true){
			cargarFondo();
		}
		
		//seteijar atributs de renderitzat
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	
		double xScale = (500f/_fondo.getWidth(null));
		double yScale = (800f/_fondo.getHeight(null));

		
		//escalar les imatges
		AffineTransform imageTransform = AffineTransform.getTranslateInstance(0,0);
        if(_fs.getSize().height!=0)
        	imageTransform.concatenate(AffineTransform.getScaleInstance(xScale,yScale));
        
        //pintar fondo
        //g.drawImage(_fondo, imageTransform, this);
      
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
            	if( e instanceof Asteroide ){
            		paintAsteroide(g,(Asteroide)e);
            		continue;
            	}
            	if( e instanceof Movil ){
            	//	paintMovil(g,(Movil)e);
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
              	if( e instanceof Imagen ){
            		paintImagen(g,(Imagen)e);
            		continue;
            	}
              	paintRectangulo(g,(Rectangulo)e);
              	continue;
            }
            if(e instanceof Trajectoria){
            	paintTrajectoria(g,(Trajectoria)e);
            }
            if(e instanceof Forma){
            	paintPared(g,(Forma)e);
            }
        }
       
       //pintamos primero (lo ultimo que se pinta es lo que queda mas arriba) el movil
       for (int i=0;i<Mesa.getInstancia().getElementos().size();i++){
           Elemento e = ((Elemento)Mesa.getInstancia().getElementos().get(i));
           if( e instanceof Movil ){
           	paintMovil(g,(Movil)e);
           }
           
       }
       
                
	}
	
	private void paintImagen(Graphics2D g, Imagen e) {
		
		/*int radi = e.getRadiColisio();
		Color color= Color.LIGHT_GRAY;
		g.setPaint(color);
		
		for(int y = -radi; y<e.getYd()+radi; y++){
			for(int x = -radi; x<e.getXd()+radi; x++){
				if(e.getColisions(x, y)!=0){
					Shape square = new Rectangle2D.Double(e.getX()+x,e.getY()+y,1,1);
					g.draw(square);
				}
			}
		}*/

		
		Image img = (Image)_texture_hash.get(e.getId());
		 g.drawImage(img,(int)e.getX(),(int)e.getY(),null);
		
	}


	private void paintTrajectoria(Graphics2D g, Trajectoria e) {
		/*if(!e.isDo_paint())
			return;
		Iterator it = e.getPosiciones().iterator();
		Punto p_anterior = new Punto(e.getX(),e.getY());
		
		while(it.hasNext()){
			
			int longitud = 0;
			Vector2D segmento = null;
			Punto p = null;
			
			while(longitud == 0){
				p = (Punto)it.next();
				if(!it.hasNext())
					return;
				segmento = new Vector2D(p_anterior.getXRelatiu(),p_anterior.getYRelatiu(),p.getXRelatiu(),p.getYRelatiu());
				longitud = (int)segmento.getModulo();
			}
			Shape linea = new  Rectangle2D.Float(0,0,longitud,2);
			AffineTransform  trans2 = AffineTransform.getTranslateInstance(p_anterior.getXRelatiu(),p_anterior.getYRelatiu());
			linea =  trans2.createTransformedShape(linea);

			AffineTransform  rotacionLinea = AffineTransform.getRotateInstance(segmento.getAngle(),p_anterior.getXRelatiu(),p_anterior.getYRelatiu());
			linea = rotacionLinea.createTransformedShape(linea);

			g.setPaint(new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1f));
			g.fill(linea);

			p_anterior = p;
		}*/		
	}

	private void paintPared(Graphics2D g, Forma e){
		Iterator<Circular> it = e.getPlanetas();
		while(it.hasNext()){
			Circular circ = it.next();
			if( circ instanceof Planeta)
				paintPlaneta(g,(Planeta)circ);
			else
				paintAsteroide(g,(Asteroide)circ);
					
		}
	}
	
	private void paintZonaInercia(Graphics2D g, ZonaInercia e) {
		paintRectangulo(g, (Rectangulo)e);
		
	}


	private void paintRectangulo(Graphics2D g, Rectangulo e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		Shape square = new Rectangle2D.Double(xCord, yCord,e.getXdRelatiu(), e.getYdRelatiu());
		Color color= Color.orange;
		g.setPaint(color);
		g.draw(square);
		g.setPaint(Color.white);
	}


	private void paintListaTiradas(Graphics2D g, ListaTiradas e) {
		paintRectangulo(g, (Rectangulo)e);
		Iterator it = e.getListaMoviles().iterator();
		while(it.hasNext()){
			paintMovil(g,(Movil)it.next());
		}
		
	}


	private void paintCircular(Graphics2D g, Circular e) {
		
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		double radio= e.getRadioRelatiu() ;
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		Color color= Color.blue;
		g.setPaint(color);
		g.fill(square);
		g.setPaint(Color.white);
		
		
	}


	private void paintPolvo(Graphics2D g, Polvo e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		// AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord,yCord);
        //g.drawImage(e._textura, imageTransform, null);//TODO: pintat escalant amb el radi
		
		
		Shape square = new Rectangle2D.Double(xCord, yCord,5,5);
		Color color= Color.orange;
		g.setPaint(color);
		g.draw(square);
		g.setPaint(Color.white);
		
	}


	private void paintMarcaTirada(Graphics2D g, MarcaTirada e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		/* AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord-3,yCord-3);
        g.drawImage((Image)_texture_hash.get(e.getTexturaLocation()), imageTransform, null);//TODO: pintat escalant amb el radi
        */
		Shape square = new  Ellipse2D.Float(0,0,(int)e.getRadioRelatiu()*2,(int)e.getRadioRelatiu()*2);
		AffineTransform  trans = AffineTransform.getTranslateInstance(xCord-e.getRadioRelatiu(),yCord-e.getRadioRelatiu());
		square = trans.createTransformedShape(square);
		g.setPaint(Color.cyan);
		g.fill(square);
	}

	private void paintMovil(Graphics2D g, Movil e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();			
		
		//Pintar su cola  Anchura 5 , Longitud proporcional a la |v|*_colaLengthFactor
		/*if(_vx!=0||_vy!=0){
			Shape cola = new  Rectangle2D.Float(0,0,(int)(_colaLengthFactor*Math.sqrt(_vx*_vx+_vy*_vy)),5);
			AffineTransform  trans2 = AffineTransform.getTranslateInstance(xCord,yCord);
			cola =  trans2.createTransformedShape(cola);
			double anguloCola=Math.atan(_vy/_vx);
			
			if(_vx<0)
			{
				anguloCola+=Math.PI;
			}else{
			
				if(_vy<0)
					anguloCola+=2*Math.PI;
			}
			
			AffineTransform  rotacionCola = AffineTransform.getRotateInstance(anguloCola+Math.PI,xCord,yCord);
			cola = rotacionCola.createTransformedShape(cola);
			g.setPaint(Color.green);
			g.fill(cola);
			g.setPaint(Color.white);
		}*/

                    //Pintar el movil
		
		Shape square = new  Ellipse2D.Float(0,0,(int)e.getRadioRelatiu()*2,(int)e.getRadioRelatiu()*2);
		AffineTransform  trans = AffineTransform.getTranslateInstance(xCord-e.getRadioRelatiu(),yCord-e.getRadioRelatiu());
		square = trans.createTransformedShape(square);
		g.setPaint(Color.gray);
		g.fill(square);
		g.setPaint(Color.white);

	    g.setColor(Color.BLACK);
	    g.drawString( ""+e.getMasa(),xCord,yCord);
	    
		//pintem el tirachinas
		
		if( e.getTirachinasX() != 0 && e.getTirachinasY() != 0){
			
			Vector2D tirachinas = new Vector2D(e.getXRelatiu(),e.getYRelatiu(),e.getTirachinasXRelatiu(),e.getTirachinasYRelatiu());
			int longitud = (int)tirachinas.getModulo();
			Shape linea = new  Rectangle2D.Float(0,0,longitud,1);
			AffineTransform  trans2 = AffineTransform.getTranslateInstance(e.getXRelatiu(),e.getYRelatiu());
			linea =  trans2.createTransformedShape(linea);

			AffineTransform  rotacionLinea = AffineTransform.getRotateInstance(tirachinas.getAngle(),e.getXRelatiu(),e.getYRelatiu());
			linea = rotacionLinea.createTransformedShape(linea);
			
			g.setPaint(Color.red);
			g.fill(linea);
			
		    g.setColor(Color.BLACK);
		    g.drawString( " x: "+(int)e.getTirachinasX()+" y:"+(int)e.getTirachinasY(),(int)e.getTirachinasX(),(int)(e.getTirachinasY()+40));
		    
			//g.setPaint(Color.white);
		}
		
		paintTrajectoria(g,e.getTrajectoria());
		
	}

	private void paintPlanetaPared(Graphics2D g, Planeta e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		double radio= e.getRadioRelatiu();
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		Color color= Color.red;
	    
	    if(e.getMasa()==0)
			color= Color.ORANGE;
		g.setPaint(color);
		g.fill(square);
		
	}

	private void paintAsteroide(Graphics2D g, Asteroide e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		double radio= e.getRadioRelatiu();
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
	
		float alpha = (float) (( 1 - (700 - e.getEnergia() ) / 700)* 0.5);
		
		if(alpha>1)
			alpha=1;
		if(alpha<0)
			alpha=0;
		
		Color color= new Color(0f,1f,0f,alpha);
	    
		
		g.setPaint(color);
		g.fill(square);
		
	}
	
	
	private void paintPlaneta(Graphics2D g, Planeta e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		double radio= e.getRadioRelatiu();
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		
		float alpha = (float) (( 1 - (e.energia_inicial - e.getEnergia() ) / e.energia_inicial)* 0.5);
		if(alpha>1)
			alpha=1;
		if(alpha<0)
			alpha=0;
		Color color= new Color(0f,0f,1f,alpha);
	    

		g.setPaint(color);
		g.fill(square);
		
	    g.setColor(Color.BLACK);
	    g.drawString( ""+e.getMasa(),xCord,yCord);
	}


	private void paintDiana(Graphics2D g, Diana e) {
		int xCord = (int)e.getXRelatiu();
		int yCord = (int)e.getYRelatiu();	
		
		 /*AffineTransform imageTransform = AffineTransform.getTranslateInstance(xCord-30,yCord-30);
		 g.drawImage((Image)_texture_hash.get(e.getTexturaLocation()), imageTransform, null);//TODO: pintat escalant amb el radi*/
	
		 
		double radio= e.getRadioRelatiu();
		Shape square = new  Ellipse2D.Float((int)(xCord-radio),(int)(yCord-radio),(int)(radio*2),(int)(radio*2));
		Color color= Color.cyan;
		
	    if(e.is_diana_final())
			color= Color.red;
		g.setPaint(color);
		g.fill(square);
	 
		
	}


	public int escalarCoordenadaX (double x)
	{
            return (int)(x*_fs.getSize().width);
	}
	
	public int escalarCoordenadaY (double y)
	{
            return (int)(y*_fs.getSize().height);
	}
	
	
}
