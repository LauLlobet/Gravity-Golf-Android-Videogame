package milkyway.elementos;


import milkyway.Mesa;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.geometria.Vector2D;

import milkyway.elementos.Circular;
import milkyway.elementos.Elemento;

public class Circular extends Elemento implements LoadSaveXML,Editable {

	private double _radio = XMLKeys.emptyDouble;
	

	public Circular() {
		super();
	}
	
	public Circular(double x, double y, double r) {
		setXY(x,y);
		setRadio(r);
		
	}

	private boolean atributosIncompletosCircular(){
		if(_radio == XMLKeys.emptyDouble)
			return true;
		else
			return false;
	}
	public int parse(NBEasyXML xml, int idx) {
		int didx = super.parse(xml,idx);
		
		while(atributosIncompletosCircular()){
			int id = XMLKeys.identify(xml.getName(didx));
			switch(id){
				case XMLKeys.radio:
					 setRadio(xml.getDoubleContent(didx));
					 didx = xml.findNextPeer(didx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en Circular no reconocida");
				
				}	
		}
		return didx;
	}
	
	public String save(){
		return super.save()+
				"\t\t<"+XMLKeys.id2str(XMLKeys.radio)+">"+getRadio()+"</"+XMLKeys.id2str(XMLKeys.radio)+">\n";
	}
	public double getRadio(){
		return this._radio;
	}
	
	public double getRadioRelatiu(){
		return this._radio*Mesa.getInstancia().getescalat();
	}
	
	public void setRadio(double a){
		_radio=a;
	}
	


	
	//funcions geometriques
	
	
	public boolean isPointIn(double x ,double y)
	{
		Vector2D ctop=new Vector2D(this.getX(),this.getY(),x,y);
		if( this._radio <= ctop.getModulo())
			return true;
		else
			return false;
	}
	
	public boolean isIntercercCircle(Circular a)
	{
		Vector2D ctoc=new Vector2D(getX(),getY(),a.getX(),a.getY());
		if( (this._radio+a.getRadio()) >= ctoc.getModulo())
			return true;
		else
			return false;
	}
	
	

	
	/*
	 * 
	 * int circle_circle_intersection(double x0, double y0, double r0,
                               double x1, double y1, double r1,
                               double *xi, double *yi,
                               double *xi_prime, double *yi_prime)
{
  double a, dx, dy, d, h, rx, ry;
  double x2, y2;

  /* dx and dy are the vertical and horizontal distances between
   * the circle centers.
   */
	
	
	public Vector2D intersects(Circular b)
	{
			double  x0 = getX();
			double  x1 = b.getX();
			double  y0 = getY();
			double  y1 = b.getY();
			double r0 =_radio,r1=b.getRadio();
			double a, dx, dy, d, h, rx, ry;
			double x2, y2;
	
			
			dx = x1 - x0;
			dy = y1 - y0;
	
	  /* Determine the straight-line distance between the centers. */
			d = Math.sqrt((dy*dy) + (dx*dx));
	
	  /* Check for solvability. */
			if (d > (r0 + r1))
				return new Vector2D(0,0,0,0);
			if (d < Math.abs(r0 - r1))
				return new Vector2D(0,0,0,0);
	
	  /* 'point 2' is the point where the line through the circle
	   * intersection points crosses the line between the circle
	   * centers.  
	   */
	
	  /* Determine the distance from point 0 to point 2. */
			a = ((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d) ;
	
	  /* Determine the coordinates of point 2. */
			x2 = x0 + (dx * a/d);
			y2 = y0 + (dy * a/d);
	
	  /* Determine the distance from point 2 to either of the
	   * intersection points.
	   */
	  h = Math.sqrt((r0*r0) - (a*a));
	
	  /* Now determine the offsets of the intersection points from
	   * point 2.
	   */
	  rx = -dy * (h/d);
	  ry = dx * (h/d);
	
	  /* Determine the absolute intersection points. */
	  
	  /* Determine the absolute intersection points. */
	  /*
	   * *xi = x2 + rx;
	  *xi_prime = x2 - rx;
	  *yi = y2 + ry;
	  *yi_prime = y2 - ry;
	  */
	
	  Vector2D solucion=new Vector2D(x2 + rx,y2+ry,x2 - rx,y2 - ry);
	
	  return solucion;
	}
	
	public boolean isParteDentro(Circular m){
		Vector2D dp =new Vector2D(getX(),getY(),m.getX(),m.getY());
		double radsum=(getRadio()+m.getRadio());
	    if( dp.getModulo()<radsum ){
	    	return true;
	    }
	    return false;
	}
	
	public boolean isTodoDentro(Circular m){
		Vector2D dp=new Vector2D(getX(),getY(),m.getX(),m.getY());
	    if( dp.getModulo()+m.getRadio()<getRadio() ){
	    	return true;
	    }
	    return false;
	}
	
	
	public double tangenteSeguraSuperficie(Circular m){
		
		Vector2D nPaM=new Vector2D(getX(),getY(),m.getX(),m.getY());
		double angulo =nPaM.getAngle(); // angulo de la normal
		angulo = angulo+(Math.PI/2.0); // abgulo de la tangente
		return angulo;
	}

	public double distanciaaMargen(Circular c, double normal,double margen) {
		
		Vector2D pam = new Vector2D(0,0,c.getX()-getX(),c.getY()-getY());
		
		double dist_adequada = c.getRadio()+ getRadio()-margen;
		
		if(pam.getModulo()>= dist_adequada )
			return 0;
		
		return dist_adequada - pam.getModulo();
	}
	
	/*
	 * 
	 * expulsar fuera 
	 * 
	 * //IO.ConsoleLog.println("EfectoNotin");
		// decoracion
		Planeta p=(Planeta)e;
		Vector2D intersec = p.intersects(m);
		//p._vector2d_aux=intersec;
		// encontramos el angulo de la colision
		Vector2D dp=new Vector2D(p.getX(),p.getY(),m.getX(),m.getY());
		double a=dp.getAngle();
		// encontramos el punto de colision
		double radio =p.getRadio();
		double pcx,pcy;
		pcx=radio*Math.cos(a);
		pcy=radio*Math.sin(a);
		//seteamos el movil fuera
		double mcdx,mcdy;
		mcdx=m.getRadio()*Math.cos(a)+1;
		mcdy=m.getRadio()*Math.sin(a)+1;
		
		m.setXY(p.getX()+pcx+mcdx,p.getY()+pcy+mcdy);
				
	 */
	
}
