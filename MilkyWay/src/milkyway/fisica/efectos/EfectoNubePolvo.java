package milkyway.fisica.efectos;

import milkyway.Mesa;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.elementos.Polvo;
import milkyway.elementos.ZonaInercia;
import milkyway.fisica.AfectaMovil;
import milkyway.geometria.Vector2D;
import milkyway.fisica.efectos.EfectoMecanico;

public class EfectoNubePolvo extends EfectoMecanico {
	
	
	public EfectoNubePolvo(AfectaMovil afect){
		super(afect);
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
		
		if(m.getNube() != null)
			return true; //TODO: colision con una area
		return false;
	}
	
	public void aplica(Movil m)
	{
		Movil anterior = m.getAnterior();
		Vector2D desplazamiento = new Vector2D(m.getX(),m.getY(),anterior.getX(),anterior.getY());
		Vector2D velocidad = new Vector2D(0,0,m.getVx(),m.getVy());
		velocidad.rotate(Math.PI);
		
		if( m.getNube().disminuyeSeparacion(desplazamiento.getModulo())){
			m.getNube().createPolvo(m.getX(), m.getY(),velocidad.getXd(),velocidad.getYd(), Mesa.getInstancia());
			
		}
			
	}

	
}
