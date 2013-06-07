package milkyway.fisica.efectos;


import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.*;
import milkyway.fisica.AfectaMovil;
import milkyway.geometria.*;
import milkyway.logica.ResManager;
import milkyway.elementos.Masa;
import milkyway.elementos.Movil;
import milkyway.elementos.Planeta;
import milkyway.fisica.efectos.EfectoMecanico;

public class EfectoGravedad extends EfectoMecanico{
	
	private static double _constante_gravitacional_newton = XMLKeys.emptyDouble;
	
	public EfectoGravedad(AfectaMovil afect)
	{
		super(afect);
		_constante_gravitacional_newton = ResManager.getInstancia().getVariableDouble(XMLKeys.constanteGravitacional);
	}
	

	public void aplicaEfecto(Movil movil)
	{
		if(this.debeAplicarse(movil))
			this.aplica(movil);
	}

	public boolean debeAplicarse(Movil m)
	{
		/*if(true)
			return false;*/
		
		if(! super.debeAplicarse(m))
			return false;
		
		//no si el planeta es null o no es planeta
		if(_afectador==null || !(_afectador instanceof Masa))
			return false;
		
		Masa p=(Masa)_afectador;
		
		/*
		//no si hay colision

		Vector2D dp=new Vector2D(p.getX(),p.getY(),m.getX(),m.getY());
		
		if(dp.getModulo()>(p.getRadio()+m.getRadio()))
			return true;
		
		return false;*/
		
		return true;
	}	
	public void aplica(Movil movil)
	{
		Masa planeta =(Masa) _afectador;
		
		//Obtenemos distancias
		Vector2D PaM=new Vector2D(planeta.getX(),planeta.getY(),movil.getX(),movil.getY());
		double modul =PaM.getModulo();
		
		//Aplicamos la ley de Mr.Newton
		double cuaddist = 1/(modul*modul);
		double massprod = movil.getMasa() * planeta.getMasa() * _constante_gravitacional_newton;		
		double dfx = cuaddist*massprod*PaM.getDX()/modul;
		double dfy = cuaddist*massprod*PaM.getDY()/modul;
		
		//Seteamos las F's
		movil.setDF(-dfx, -dfy);
		
		
		
		
		
		/*
		if(planeta==null)
			return;

		//Obtenemos distancias
		double dx = planeta.getX()-movil.getX();
		double dy = planeta.getY()-movil.getY();
		double modul =Math.sqrt(dx*dx+dy*dy);
		
		//Aplicamos la ley de Mr.Newton
		double cuaddist = 1/(modul*modul);
		double massprod = movil.getMasa() * planeta.getMasa() * _constante_gravitacional_newton;		
		double ax =cuaddist*massprod*dx/modul;
		double ay =cuaddist*massprod*dy/modul;
		


		//Seteamos los
		movil.setV(movil.getVx()+ax, movil.getVy()+ay);
		movil.setDXY(movil.getVx(), movil.getVy());*/
		
	}
	
}
