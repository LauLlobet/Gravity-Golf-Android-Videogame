package milkyway.logica;

import java.util.Hashtable;
import java.util.Vector;

import milkyway.logica.EstadoGeneral;

public class EstadoGeneral {
	
	
	private static EstadoGeneral _instancia = null;
	
	private static int _estado_juego;
	private static int _estado_anterior;
	
	public static final int _ESTADO_INICIO = 0; // estat incial , sense pantalla
	public static final int _ESTADO_INICIO_CARGAR_PANTALLA= 1;
	public static final int _ESTADO_JUEGO_LANZAMIENTO = 2; //estat de pantalla cargada esperant llançament
	public static final int _ESTADO_JUEGO_LANZAMIENTO_TIRANDO = 3; // se esta tirant del tirachinas, onrelase salta al seguent estat
	public static final int _ESTADO_JUEGO_PRE_FISICAS_LANZADAS = 4; // estat anterior a inciar fisicas, util per a debug
	public static final int _ESTADO_JUEGO_FISICAS_LANZADAS = 5; // estat despres del llançament, fisica on 
	public static final int _ESTADO_VICTORIA = 6; // el movil pasa per diana
	public static final int _ESTADO_DERROTA = 7; // el movil s'ha posat a tremolar, s'ha parat o ha sortit de la zona
	public static final int _ESTADO_FIN = 8; // el movil pasa per diana
	
	public static final int _ESTADO_EDICION = 9; // edicion
	public static final int _ESTADO_SALVAR_PANTALLA = 10; // salvar la pantalla actual
	
	public static final int _ESTADO_RESETEAR_PANTALLA = 11; // salvar la pantalla actual

	protected EstadoGeneral(){
	}
	
	public static EstadoGeneral getInstancia(){
		if(_instancia == null)
			_instancia = new EstadoGeneral();
		return _instancia;
	}
	
	
	public static void setEstado(int estado){
			_estado_anterior = _estado_juego;
			_estado_juego = estado;
		}
	public static int getEstado(){
		return _estado_juego;
	}
		
	public static int getEstadoAnterior(){
		return _estado_anterior;
	}
	
}
