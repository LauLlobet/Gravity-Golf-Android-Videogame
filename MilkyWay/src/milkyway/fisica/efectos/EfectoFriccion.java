package milkyway.fisica.efectos;

import milkyway.Mesa;
import milkyway.fisica.efectos.EfectoMecanico;
import milkyway.XMLUtils.LoadSaveXML;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.Asteroide;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.elementos.ZonaInercia;
import milkyway.fisica.AfectaMovil;
import milkyway.geometria.Vector2D;

public class EfectoFriccion extends EfectoMecanico {

	private double _friccion = XMLKeys.emptyDouble;
	
	public EfectoFriccion(AfectaMovil afect){
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
	
	/*public int parse(NBEasyXML xml, int idx) {
		
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
	}*/
	
	public void aplicaEfecto(Movil m)// TODO: eliminar aquest tall de codi de tots els efectes i deixarlo nomes en efectoMecanico
	{
		if(!debeAplicarse(m))
			return;
		aplica(m);
		
	}
	public boolean debeAplicarse(Movil m)
	{
		if(! super.debeAplicarse(m))
			return false;
		
		if(!(_afectador instanceof Elemento)){
			IO.ConsoleLog.println("S'ha aplicat efecto friccion a una clase que no es elemento");
			return false;
		}
		Elemento z = (Elemento)_afectador;
		
		if( z.isParteDentro(m) )
			return true;
		else
			return false;
	}
	
	public void aplica(Movil m)
	{
		
		Vector2D velocidad = new Vector2D(0,0,m.getVx(),m.getVy());
		
		if( velocidad.getModulo() == 0){
			
			Vector2D força = new Vector2D(0,0,m.getFx(),m.getFy());
			if( força.getModulo() - _friccion <= 0 ){
				m.setF(0,0);
				return;
			}
			Vector2D forçaN = new Vector2D(0,0,força.getModulo() - _friccion,0);
			forçaN.rotate(força.getAngle());
			m.setF(forçaN.getDX(), forçaN.getDY());
			return;
		}

		//s'esta movent
		Vector2D forçaV;
		if(velocidad.getModulo()<_friccion)
			forçaV = new Vector2D(0,0,- velocidad.getModulo(),0);
		else
			forçaV = new Vector2D(0,0,- _friccion,0);
		
		forçaV.rotate(velocidad.getAngle());
		m.setDF(forçaV.getDX(), forçaV.getDY());
		
	
		if(_afectador instanceof Asteroide && ! Mesa.getInstancia().isSimulando()){
			//double dv = _friccion/m.getMasa(); // f=m.a   f=m.dv 
			if(velocidad.getModulo()<_friccion)
				return;//ponemos 0 por comodidad ya que aparecen -1 de la acceleracion de los planetas vecinos //((Asteroide)_afectador).setDEnergia( - (Math.pow( velocidad.getModulo() * 100 ,2)/m.getMasa()) );
			else
				((Asteroide)_afectador).setDEnergia( - (Math.pow( _friccion * 100 ,2)/m.getMasa()) );// *100 evita 0'decimals que elevats al cuadrat s'empetitirien 
		}
	
	}

	
}
