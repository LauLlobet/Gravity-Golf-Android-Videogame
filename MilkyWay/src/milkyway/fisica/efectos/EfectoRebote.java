package milkyway.fisica.efectos;

import java.util.Collection;
import java.util.Vector;

import milkyway.*;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.*;
import milkyway.fisica.AfectaMovil;
import milkyway.geometria.*;
import milkyway.logica.ResManager;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.fisica.efectos.EfectoMecanico;
import milkyway.geometria.Vector2D;
public class EfectoRebote extends EfectoMecanico implements LoadSaveXML{

	private static double _vLimitFactor;
	private static double _margen;
	
	private double _factorX= XMLKeys.emptyDouble;	
	private double _factorY= XMLKeys.emptyDouble;

	
	public EfectoRebote(AfectaMovil afec)
	{
		super(afec);
		_vLimitFactor= ResManager.getInstancia().getVariableDouble(XMLKeys.efectoReboteVLimit);//TODO:eliminar margen i vlimit del codi i l'xml
		_margen = ResManager.getInstancia().getVariableDouble(XMLKeys.efectoReboteMargen);
		
	}
	
	public EfectoRebote(AfectaMovil afec,double x, double y) {
		super(afec);
		_vLimitFactor= ResManager.getInstancia().getVariableDouble(XMLKeys.efectoReboteVLimit);
		_margen = ResManager.getInstancia().getVariableDouble(XMLKeys.efectoReboteMargen);
		_factorX= x;	
		_factorY= y;
	}

	private boolean atributosIncompletosEfectoRebote(){
		if(_factorY == XMLKeys.emptyDouble || _factorX == XMLKeys.emptyDouble)
			return true;
		else
			return false;
	}
	
	public int parse(NBEasyXML xml, int idx) {
		
		idx = xml.findChildElement(idx);
		while(atributosIncompletosEfectoRebote()){
			int id = XMLKeys.identify(xml.getName(idx));
			switch(id){
				case XMLKeys.desgasteX:
					 setDesgasteX(xml.getDoubleContent(idx));
					 idx = xml.findNextPeer(idx);
				break;
				case XMLKeys.desgasteY:
					 setDesgasteY(xml.getDoubleContent(idx));
					 idx = xml.findNextPeer(idx);
				break;
				
				default:
					IO.ConsoleLog.println("XML tag en parseEfectoRebote no reconocida");
				
				}	
		}
		return idx;
	}
	
	public String save(){
		return "\t\t\t<"+XMLKeys.id2str(XMLKeys.desgasteX)+">"+getDesgasteX()+"</"+XMLKeys.id2str(XMLKeys.desgasteX)+">\n"+
				"\t\t\t<"+XMLKeys.id2str(XMLKeys.desgasteY)+">"+getDesgasteY()+"</"+XMLKeys.id2str(XMLKeys.desgasteY)+">\n";
	}

	public double getDesgasteX(){
		return _factorX;
	}
	public double getDesgasteY(){
		return _factorY;
	}
	
	public void setDesgasteX(double x){
		_factorX = x;
	}
	
	public void setDesgasteY(double y){
		_factorY = y;
	}

	public void aplicaEfecto(Movil m)
	{
		if(!debeAplicarse(m))
			return;
		aplica( m);
		
	}
	

	public boolean debeAplicarse(Movil m)
	{
		
		if(! super.debeAplicarse(m))
			return false;
		
		if( _afectador instanceof Movil && m.hanRebotado((Movil)_afectador) )
			return false;
		
		if(!(_afectador instanceof Elemento))
			return false; 
		Elemento p=(Elemento)_afectador;

		//caso simple
		if(p.isIntersectsSuperficie(m))
			return true;
		
		//caso traspasa superficie
		if( ( p.isTodoDentro(m) && p.isTodoFuera(m.getAnterior()) ) ||
			    (p.isTodoFuera(m)  && p.isTodoDentro(m.getAnterior()) ) )
			return true;
		
		
		return false;
	}
	
	

	
	public void aplica(Movil m)
	{
		Circular e=(Circular)_afectador;
		Movil m_otro = null;
		if(e instanceof Movil)
			m_otro = (Movil)e;
		
		double angulo = e.tangenteSuperficie(m); // es fara servir despres pero el mirem ara, abans de desplaçar
		double normal = angulo-(Math.PI/2.0); // angulo de la normal
		double normal_otro = normal- Math.PI;
		
		/*double dist_marge = e.distanciaaMargen(m,normal, 1);
		if(dist_marge>0){
			Vector2D desplaçament = new Vector2D(0, 0, dist_marge, 0);
			desplaçament.rotate(normal);
			m.setXY(m.getX()+desplaçament.getDX(), m.getY()+desplaçament.getDY());
		}*/

		// Calcular v en coordenades rotades
		Vector2D velocidad =new Vector2D(0,0,m.getVx(),m.getVy());
		double velocidadmod = velocidad.getModulo();
		
		Vector2D velocidad_otro;
		velocidad_otro = new Vector2D(0,0,0,0);	
		if(m_otro!=null)
			velocidad_otro =new Vector2D(0,0,m_otro.getVx(),m_otro.getVy());
		double velocidadmod_otro = velocidad_otro.getModulo();
		
		velocidad.rotateFrom(normal_otro, 0, 0);
		velocidad_otro.rotateFrom(normal_otro, 0, 0);
		
		// reflexar v, VX es la que esta en direccio a la normal , vy es tangencial
		double v1 = velocidad.getXd();
		double vyp = velocidad.getYd();
		double v2 = velocidad_otro.getXd();
		double vyp_otro = velocidad_otro.getYd();
		
		if ( v1-v2 > 0 ){
			
			//Obtenir les masses
			double m1 = m.getMasa();
			double m2 = 999999999;
			if(m_otro != null)
				m2 = m_otro.getMasa();
			
			/*vxp= - (vxp + (vxp*_factorY)); // el factor es negatiu, la formula sencera es negativa per a el reflexe
			vyp= vyp + (vyp*_factorX);*/ // Rebot antic
			
			double vxp_rebotada =((m1-m2)/(m1+m2))*v1+ (2*m2/(m1+m2))*v2;
			double vxp_otro_rebotada =(2*m1/(m1+m2))*v1+((m2-m1)/(m2+m1))*v2;
			
			// Calcular v novament
			Vector2D velocidad2 =new Vector2D(0,0,vxp_rebotada,vyp);
			velocidad2.rotateFrom(-normal_otro, 0, 0);
			m.setV(velocidad2.getXd(), velocidad2.getYd());
			
			if(m_otro != null){
				Vector2D velocidad2_otro =new Vector2D(0,0,vxp_otro_rebotada,vyp_otro);
				velocidad2.rotateFrom(-normal_otro, 0, 0);	
				m_otro.setV(velocidad2_otro.getXd(), velocidad2_otro.getYd());
				m.setHanRebotat(m_otro);
			}
		}
		/*if(! Mesa.getInstancia().isSimulando())
			e.setDEnergia( - (Math.pow( ( velocidad2.getModulo()- velocidad.getModulo() ) ,2)*m.getMasa()) );
		*/
		
		//IO.ConsoleLog.println("VM despres de rebotar:"+velocidad3.getModulo());
		
		// ARA FEM EL MATEIX  REFLEXANT LA NORMAL
		
		/*
		Vector2D fuerza = new Vector2D(0,0,m.getFx(),m.getFy());
		fuerza.rotateFrom(normal, 0, 0);
		
		// reflexar v
		double fxp = fuerza.getXd();
		double fyp = fuerza.getYd();
		

		fxp= -fxp;
		fyp= fyp;
		
		
		// Calcular v novament
		Vector2D fuerza2 =new Vector2D(0,0,fxp,fyp);
		fuerza2.rotateFrom(-normal, 0, 0);
		
		//IO.ConsoleLog.println("Aplicando efecto rebote VI:"+vi.getModulo()+" vf"+velocidad2.getModulo());
		
		//IO.ConsoleLog.println("A una  Fx:"+m.getFx()+" Fy:"+m.getFy());
		//IO.ConsoleLog.println("Normal Nx:"+fuerza2.getXd()+" Ny:"+fuerza2.getYd());
		//assignar
		m.setDF(fuerza2.getXd(), fuerza2.getYd());
		//IO.ConsoleLog.println("Total  Fx:"+m.getFx()+" Fy:"+m.getFy());
		
		*/
	
		if(m_otro != null)
			m.setHanRebotat(m_otro);
	}

	

	
}
