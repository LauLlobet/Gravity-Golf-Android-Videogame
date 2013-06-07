package milkyway.fisica.efectos;
import milkyway.elementos.*;
import milkyway.fisica.AfectaMovil;
import milkyway.elementos.Movil;

public class EfectoMecanico 
{
	AfectaMovil _afectador = null;
	
	public EfectoMecanico(AfectaMovil afect){
		_afectador = afect;
	}
	
	public void aplicaEfecto(Movil m){}
	
	public boolean debeAplicarse(Movil m){
		
		if(m.equals(_afectador))
		 return false;
		
		return true;
		 
	}
	
	public void  aplica(Movil m){}
}
