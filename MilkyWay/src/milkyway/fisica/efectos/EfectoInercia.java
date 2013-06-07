package milkyway.fisica.efectos;

import milkyway.fisica.efectos.EfectoMecanico;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.elementos.ZonaInercia;
import milkyway.fisica.AfectaMovil;

public class EfectoInercia extends EfectoMecanico implements LoadSaveXML{

	private double _friccion = XMLKeys.emptyDouble;
	private double _dt = 0.003;
	
	public EfectoInercia(AfectaMovil afect){
		super(afect);
	}
	
	
	private boolean atributosIncompletosEfectoInercia(){
		if(_friccion == XMLKeys.emptyDouble)
			return true;
		else
			return false;
	}
	public void setFriccion(double f){
		_friccion = f;
	}
	public double getFriccion(){
		return _friccion;
	}
	
	public int parse(NBEasyXML xml, int idx) {
		
		idx = xml.findChildElement(idx);
		while(atributosIncompletosEfectoInercia()){
			int id = XMLKeys.identify(xml.getName(idx));
			switch(id){
				case XMLKeys.friccion:
					 setFriccion(xml.getDoubleContent(idx));
					 idx = xml.findNextPeer(idx);
				break;
				default:
					IO.ConsoleLog.println("XML tag en parseEfectoInercia no reconocida");
				
				}	
		}
		return idx;
	}
	
	public String save(){
		return "\t\t\t<"+XMLKeys.id2str(XMLKeys.friccion)+">"+getFriccion()+"</"+XMLKeys.id2str(XMLKeys.friccion)+">\n";
	}
	
	public void aplicaEfecto(Movil m)// TODO: eliminar aquest tall de codi de tots els efectes i deixarlo nomes en efectoMecanico
	{
		if(!debeAplicarse(m))
			return;
		aplica( m);
		
	}
	public boolean debeAplicarse(Movil m)
	{
		
		if(! super.debeAplicarse(m))
			return false;
		
		if(!(_afectador instanceof Elemento)){
			IO.ConsoleLog.println("S'ha aplicat efecto inercia a una clase que no es elemento");
			return false;
		}
		Elemento z = (Elemento)_afectador;
		
		if( z.isTodoDentro(m) )
			return true;
		else
			return false;
	}
	
	public void aplica(Movil m)
	{
		//m.setDV(-m.getVx()*_friccion,-m.getVy()*_friccion);
		
		/*//IO.ConsoleLog.println("[inercia]Força a aplicar inercia: fy"+ m.getFy() + "fx"+ m.getFx());
		m.setDV(m.getFx(), m.getFy());
		//IO.ConsoleLog.println("[inercia]Velocitat: vx"+ m.getVx() + "vy"+ m.getVy());
		m.setF(0, 0);
		m.setDXY(m.getVx(), m.getVy()); 
		*/
		
		
		//IO.ConsoleLog.println("[inercia]Força a aplicar inercia: fy"+ m.getFy() + "fx"+ m.getFx());
		double ax = m.getFx();
		double ay = m.getFy();
		
		double vx = m.getVx();
		double vy = m.getVy();
		
		//IO.ConsoleLog.println("avans inercia :"+m);
		m.setDXY(0.5*ax*_dt*_dt + vx*_dt , 0.5*ay*_dt*_dt + vy*_dt ); // si la acceleracio es 0 la inercia en un dt=0.5 fara avançar un v*0.5
		m.setDV(ax*_dt,ay*_dt);
		//IO.ConsoleLog.println("despres inercia :"+m);
		m.setF(0, 0);
		
		m.emptyRebotats();
		
	}

	
}
