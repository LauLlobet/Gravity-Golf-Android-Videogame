package milkyway.fisica;

import java.util.Enumeration;
import java.util.Hashtable;

import milkyway.elementos.Movil;
import milkyway.fisica.AfectaMovil;
import milkyway.fisica.DetectorTemblores;

import java.util.Vector ;

import milkyway.*;
import milkyway.elementos.*;
import milkyway.fisica.efectos.EfectoGravedad;
import milkyway.fisica.efectos.EfectoMecanico;
import milkyway.geometria.Vector2D;
public class FisicaMecanica { // Aplica los distintos efectos , para cada movil mira todos los elementos 

	private DetectorTemblores _detectorTemblores;
	private boolean _is_movil_temblando;
	private boolean _is_movil_parado;
	public FisicaMecanica()
	{
		_detectorTemblores = new DetectorTemblores();
		set_is_movil_temblando(false);
		set_is_movil_parado(false);
	}
	
	public void calculaDT(Vector moviles,Vector elementos)//falta pasar movil a hash de moviles
	{
		Movil movil = (Movil) moviles.firstElement();
		Vector2D velocidad_movil = new Vector2D(0,0,movil.getVx(),movil.getVy());
		
		calculaDTFisicas(moviles,elementos);
		
		_detectorTemblores.nextD(velocidad_movil);
		//set_is_movil_temblando(_detectorTemblores.isTemblando());
		set_is_movil_parado(_detectorTemblores.isParado());
	}
	public void calculaDTFisicas(Vector moviles, Vector elementos){
		Enumeration lista_moviles;
		Enumeration  lista_efectos;
		
		Movil act;
		Object posible_afectador;
		AfectaMovil afectador;
		EfectoMecanico efecto;
		
// Evaluamos efectos que puedan ocasionar los elementos de la mesa	(incluido el elemento inercia)	
		lista_moviles = moviles.elements();
		while(lista_moviles.hasMoreElements())
		{
			Enumeration lista_elementos = elementos.elements();
			act=(Movil)lista_moviles.nextElement();
			
			while(lista_elementos.hasMoreElements())
			{
				posible_afectador=lista_elementos.nextElement();
				if(posible_afectador instanceof AfectaMovil)
				{
					afectador=(AfectaMovil)posible_afectador;
					lista_efectos=afectador.getEfectos();
					
					while( lista_efectos!=null && lista_efectos.hasMoreElements())
					{
						efecto=(EfectoMecanico)lista_efectos.nextElement();
						efecto.aplicaEfecto(act);
					}

				}
			}
			
			lista_elementos = elementos.elements();
			
			while(lista_elementos.hasMoreElements())
			{
				posible_afectador=lista_elementos.nextElement();
				if(posible_afectador instanceof AfectaMovil)
				{
					afectador=(AfectaMovil)posible_afectador;
					lista_efectos=afectador.getEfectosSegundos(act.getX(),act.getY());
					
					while(lista_efectos!=null && lista_efectos.hasMoreElements())
					{
						efecto=(EfectoMecanico)lista_efectos.nextElement();
						efecto.aplicaEfecto(act);
					
					}

				}
			}
			

			lista_elementos = elementos.elements();
			
			while(lista_elementos.hasMoreElements())
			{
				posible_afectador=lista_elementos.nextElement();
				if(posible_afectador instanceof AfectaMovil)
				{
					afectador=(AfectaMovil)posible_afectador;
					lista_efectos=afectador.getEfectosTerceros(act.getX(),act.getY());
					
					while(lista_efectos!=null && lista_efectos.hasMoreElements())
					{
						efecto=(EfectoMecanico)lista_efectos.nextElement();
						efecto.aplicaEfecto(act);
					}

				}
			}
			
			
			lista_elementos = elementos.elements();
			
			while(lista_elementos.hasMoreElements())
			{
				posible_afectador=lista_elementos.nextElement();
				if(posible_afectador instanceof AfectaMovil)
				{
					afectador=(AfectaMovil)posible_afectador;
					lista_efectos=afectador.getEfectosCuartos();
					
					while(lista_efectos!=null && lista_efectos.hasMoreElements())
					{
						efecto=(EfectoMecanico)lista_efectos.nextElement();
						efecto.aplicaEfecto(act);
					}

				}
			}
			
		}	
	}

	public boolean is_movil_temblando() {
		return _is_movil_temblando;
	}

	public void set_is_movil_temblando(boolean is_movil_temblando) {
		if(is_movil_temblando)
			IO.ConsoleLog.println("someone set temblando true");
		this._is_movil_temblando = is_movil_temblando;
	}

	public boolean is_movil_parado() {
		return _is_movil_parado;
	}

	public void set_is_movil_parado(boolean is_movil_parado) {
		this._is_movil_parado = is_movil_parado;
	}
	
	public void setFisicaOff(){
		_detectorTemblores.restart();
	}
}
