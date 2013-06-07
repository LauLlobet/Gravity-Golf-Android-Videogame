package milkyway.fisica.efectos;

import milkyway.fisica.efectos.EfectoMecanico;
import milkyway.elementos.Elemento;
import milkyway.elementos.Movil;
import milkyway.elementos.ZonaInercia;
import milkyway.fisica.AfectaMovil;

public class EfectoParada extends  EfectoMecanico {

	
	public EfectoParada(AfectaMovil afect){
		super(afect);
	}
	
	
	public void aplicaEfecto(Movil m)
	{
		if(!debeAplicarse(m))
			return;
		aplica(_afectador , m);
		
	}
	public boolean debeAplicarse(Movil m)
	{
		
		if(! super.debeAplicarse(m))
			return false;
		
		if(!(_afectador instanceof Elemento)){
			IO.ConsoleLog.println("S'ha aplicat efecto parada a una clase que no es Elemento");
			return false;
		}
	
		Elemento z = (Elemento)_afectador;
		if(z.isParteFuera(m))
			return true;
		else
			return false;
	}
	
	public void aplica(AfectaMovil afectador,Movil m)
	{
		m.setV(0, 0);
	}
	
}
