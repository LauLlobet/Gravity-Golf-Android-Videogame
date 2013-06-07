package milkyway.geometria;


public class Vector2D {
	
	private double _xo;
	private double _yo;
	private double _xd;
	private double _yd;
	private double _angulo;
	private double _modulo;
	
	public Vector2D(double xo,double yo,double xd,double yd)
	{
		this._xo=xo;
		this._yo=yo;
		this._yd=yd;
		this._xd=xd;
		
		getAngle();//actualitzem els valors angulo i modulo
		getModulo();
	
	}
	
    public Vector2D(double xo,double yo,double modulo,double angulo,boolean modoPorModulo){
        this._xo = xo;
        this._yo = yo;
        this._angulo = angulo;
        this._modulo = modulo;        
        
        this._xd = modulo * Math.cos(angulo) + xo;
        this._yd = modulo * Math.sin(angulo) + yo;
    }

	public double getAngle()
	{
		double dy=this._yd-this._yo;
		double dx=this._xd-this._xo;
		this._angulo=Math.atan(dy/dx);
	
		if(dx<0)
			this._angulo = this._angulo + Math.PI;
		if(dx>0 && dy<0)
			this._angulo = this._angulo + 2*Math.PI;

		
		return _angulo;
	}
	public double getModulo()
	{
		double dy=this._yd-this._yo;
		double dx=this._xd-this._xo;
		
		this._modulo=Math.sqrt(dy*dy+dx*dx);
		
		return _modulo;
	}
	public void rotate(double angulo_rad){
		if(this._xo != 0 || this._yo != 0){
			IO.ConsoleLog.println("intentant rotar un Vector2D amb origen fora del 0,0");
		}
	    double rx = (this._xd * Math.cos(angulo_rad)) - (this._yd * Math.sin(angulo_rad));
	    double ry = (this._xd * Math.sin(angulo_rad)) + (this._yd * Math.cos(angulo_rad));
	    this._xd  = rx;
	    this._yd = ry;
	}
	public void actualiza(){
		getAngle();
		getModulo();
	}
	public void rotateFrom(double angulo_rad,double x,double y)// rota los puntos del vector respeto el eje dado por px y py
	{
		// Primer trasladamos el vector al centro 
		double ydn=this._xd-x;
		double xdn=this._xd-x;
		double yon=this._yo-y;
		double xon=this._xo-x;
		
		if(x!=this._xo || y!=this._yo){
			xon=(this._xo-x)*Math.cos(angulo_rad)+(this._yo-y)*Math.sin(angulo_rad);
			yon=-(this._xo-x)*Math.sin(angulo_rad)+(this._yo-y)*Math.cos(angulo_rad);
		}
		if(x!=this._xd || y!=this._yd){
			xdn=(this._xd-x)*Math.cos(angulo_rad)+(this._yd-y)*Math.sin(angulo_rad);
			ydn=-(this._xd-x)*Math.sin(angulo_rad)+(this._yd-y)*Math.cos(angulo_rad);
		}
		this._xd=xdn;
		this._yd=ydn;
		
	
		actualiza();
		
	}
	
	public void Traslada(double dx , double dy)
	{
		this._xo=this._xo+dx;
		this._yo=this._yo+dy;
		this._xd=this._xd+dx;
		this._yd=this._yd+dy;
	}
	public double getDX()
	{
		return _xd-_xo;
	}
	
	public double getDY()
	{
		return _yd-_yo;
	}
	public double getXd()
	{
		return _xd;
	}
	public void setXd(double xd) {
		_xd = xd;
		actualiza();
	}
	
	
	public double getYd()
	{
		return _yd;
	}
	public void setYd(double yd) {
		_yd = yd;
		actualiza();
	}
	public double getXo()
	{
		return _xo;
	}
	public void setXo(double xo) {
		_xo = xo;
		actualiza();
	}
	
	public double getYo()
	{
		return _yo;
	}
	public void setYo(double yo) {
		_yo = yo;
		actualiza();
	}
	
	/*public void paint(Graphics2D g)
	{

		Shape cola = new  Rectangle2D.Float((int)_xo,(int)_yo,(int)(this._modulo),4);		
		AffineTransform  rotacionCola = AffineTransform.getRotateInstance(_angulo,_xo,_yo);
		cola =  rotacionCola.createTransformedShape(cola);
		
		g.setPaint(Color.red);
		g.fill(cola);
		g.setPaint(Color.white);
	}*/
	
	
	@Override
	public String toString() {
		return "x0:"+this.getXo()+"y0:"+this.getYo()+" X1:"+this.getXd()+"Y2:"+this.getYd();
	}
	
}
