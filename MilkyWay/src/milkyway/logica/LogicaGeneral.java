package milkyway.logica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import milkyway.*;
import milkyway.XMLUtils.XMLKeys;
import milkyway.elementos.Asteroide;
import milkyway.elementos.Circular;
import milkyway.elementos.Diana;
import milkyway.elementos.Elemento;
import milkyway.elementos.Imagen;
import milkyway.elementos.ImagenFriccion;
import milkyway.elementos.ImagenRebote;
import milkyway.elementos.MarcaTirada;
import milkyway.elementos.Movil;
import milkyway.elementos.Forma;
import milkyway.elementos.Planeta;
import milkyway.elementos.Trajectoria;
import milkyway.geometria.Punto;

import org.xml.sax.InputSource;

import IO.TextMessage;


import milkyway.logica.EstadoGeneral;
import milkyway.logica.Logica;
import milkyway.logica.Pantalla;
import milkyway.logica.ResManager;




public class LogicaGeneral extends Logica{
	
	//private Vector _pantallas;
	private int _no_pantalla;
	
	private int _fisica = 0;
	public static final int _FISICA_ON =1;
	public static final int _FISICA_OFF =0;
	
	public static boolean _debug_on = false;
	
	public static double _factor_v_dedo;
	
	Imagen img,img2;
	
	public LogicaGeneral(){
		
		EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO);
		_factor_v_dedo = ResManager.getInstancia().getVariableDouble(XMLKeys.logicaFactorVDedo);
		
		_debug_on = ResManager.getInstancia().getVariableBoolean(XMLKeys.debugOn);
		
		
		
		_no_pantalla = 0;
	
	}
	


	
	public void setFisica(int fisica){
		_fisica = fisica;
	}
	
	
	public void doLogica(){
		
		switch(EstadoGeneral.getEstado()){
			case EstadoGeneral._ESTADO_INICIO:
				IO.ConsoleLog.println("_ESTADO_INICIO");
				_fisica = _FISICA_OFF;
				_no_pantalla = 0;
				
				InputSource pfis = IO.PantallaWriter.getInstancia().getNewPantallaFIS();
				ResManager.getInstancia().setPantallas(pfis);
				Mesa.getInstancia().fondo_invalidate = true;
				
				EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO_CARGAR_PANTALLA);
				break;
				
			case EstadoGeneral._ESTADO_INICIO_CARGAR_PANTALLA:
				_fisica = _FISICA_OFF;
				IO.ConsoleLog.println("_ESTADO_INICIO_CARGAR_PANTALLA");
				ResManager.getInstancia().parsePantallas();

				if(ResManager.getInstancia().getPantallas().size()>0){
					Pantalla p = ((Pantalla)ResManager.getInstancia().getPantallas().get(_no_pantalla));
					Mesa.getInstancia().cargarPantalla(p);
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO);
					
					
					/*img = new ImagenRebote();
					img.carrega("imatge3.png");
					img.setXY(200, 200);
					Mesa.getInstancia().nuevoElemento(img);
					
					//img2 = new ImagenRebote();
					img2.carrega("lat2.png");
					img2.setXY(230, 200);
					Mesa.getInstancia().nuevoElemento(img2);//
					
					Movil mt1 = (Movil)Mesa.getInstancia().getMoviles().firstElement(); 
					img.expandeixPerColisions((int)mt1.getRadio());
					//img2.expandeixPerColisions((int)mt1.getRadio());*/
					
		
					/*Forma pared = new Forma();
					pared.setTipo(new Planeta(0,0,0,2));
					pared.addPuntoLinea(200, 200);
					pared.addPuntoLinea(430, 210);
					Mesa.getInstancia().nuevoElemento(pared);*/
					
				/*	Forma pared = new Forma();
					pared.setTipo(new Planeta(30,0,0,2));
					pared.addPuntoLinea(200, 210);
					pared.addPuntoLinea(315, 220);
					Mesa.getInstancia().nuevoElemento(pared);
					*/
					
					Asteroide e = new Asteroide(5,40);
					e.setXY(200, 400);
					e.setEnergia(1200);
					Mesa.getInstancia().nuevoElemento(e);
					
					/*Forma aster = new Forma();
					aster.setTipo(new Asteroide(0.02, 7));
					aster.addPuntoLinea(200, 240);
					aster.addPuntoLinea(430, 250);
					Mesa.getInstancia().nuevoElemento(aster);*/
					
				//	pared.addPuntoLinea(260, 200);

					
					/*Movil m = (Movil)Mesa.getInstancia().getMoviles().firstElement();
					m.getTrajectoria().refresca(m,m.getVx(),m.getVy(), Mesa.getInstancia().getElementos(), 290);
					m.getTrajectoria().setDo_paint(true);*/
					


					
				}else{
					IO.ConsoleLog.println("colgado en _ESTADO_INICIO_CARGAR_PANTALLA por falta de pantalla");
				}				
				
				break;
			case EstadoGeneral._ESTADO_RESETEAR_PANTALLA: // torna a llegir la pantalla per a disposar els elements com al principi
				
				IO.ConsoleLog.println("_ESTADO_RESETEAR_PANTALLA");
				_fisica = _FISICA_OFF;
				
				ResManager.getInstancia().parsePantallas();
				Pantalla p = ((Pantalla)ResManager.getInstancia().getPantallas().get(_no_pantalla));
				Mesa.getInstancia().resetearPantalla(p);
				
				EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO);
				 

				
			case EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO: //Salta a lanzamiento tirando amb un click
				//IO.ConsoleLog.println("_ESTADO_JUEGO_LANZAMIENTO");
				_fisica = _FISICA_OFF;
				break;
			case EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO_TIRANDO: //TIRANDO DEL TIRACHINAS Salta a fisicas_lanzades amb l'accio Lanzar
				//IO.ConsoleLog.println("_ESTADO_JUEGO_LANZAMIENTO_TIRANDO");
				_fisica = _FISICA_OFF;
				break;
				
			case EstadoGeneral._ESTADO_JUEGO_PRE_FISICAS_LANZADAS: //TODO: eliminar estat
				IO.ConsoleLog.println("_ESTADO_JUEGO_PRE_FISICAS_LANZADAS");
			
								
				EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS);
				break;
				
			case EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS: // sala a victoria si movil colisona amb diana
				
				
				//IO.ConsoleLog.println("_ESTADO_JUEGO_FISICAS_LANZADAS");

				
				Movil mt = (Movil)Mesa.getInstancia().getMoviles().firstElement();
				mt.getTrajectoria().setDo_paint(false);
				
				
				//IO.ConsoleLog.println("Movil "+mt);
				
				_fisica = _FISICA_ON;

				if(Mesa.getInstancia().getDianas().size()==0 || Mesa.getInstancia().getMoviles().size()==0){
					IO.ConsoleLog.println("no hay moviles o dianas para pasar la pantalla");
					break;
				}
				
				//miramos si hay colision entre diana y moviles
			
				Movil m = (Movil)Mesa.getInstancia().getMoviles().firstElement();
				Iterator dianas = Mesa.getInstancia().getElementos().dianasColisionan(m).iterator();
				
				while(dianas.hasNext()){
					Diana d = (Diana)dianas.next();
					if(d.is_diana_final())
						EstadoGeneral.setEstado(EstadoGeneral._ESTADO_VICTORIA);
					else
						Mesa.getInstancia().eliminaElemento(d);
				}
				/*if(Mesa.getInstancia().isParado()){
					IO.ConsoleLog.println("Parado");
					Mesa.getInstancia().setParado(false);
					_fisica = _FISICA_OFF;
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_DERROTA);
				}*/
				/*if(Mesa.getInstancia().isTemblando()){
					IO.ConsoleLog.println("Temblando");
					Mesa.getInstancia().setTemblando(false);
					_fisica = _FISICA_OFF;
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_DERROTA);
				//	_estado_juego = _ESTADO_FIN;
				}*/
				break;
				
			case EstadoGeneral._ESTADO_VICTORIA:
				IO.ConsoleLog.println("_ESTADO_VICTORIA");
				_fisica = _FISICA_OFF;
				_no_pantalla++;
				
				/*if(_no_pantalla < ResManager.getInstancia().getPantallas().size()){
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO_CARGAR_PANTALLA);
				}else
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_FIN);
				*/
				TextMessage.getInstancia().setText("Victoria!");
				EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO);
				
				break;
				
			case EstadoGeneral._ESTADO_DERROTA:
				IO.ConsoleLog.println("_ESTADO_DERROTA");
				_fisica = _FISICA_OFF;
				
				if(Mesa.getInstancia().getMovilesTiradas().size()>0){
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_RESETEAR_PANTALLA);
				}
				else{
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO);
					//EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO_CARGAR_PANTALLA);
				}
				
				break;
			case EstadoGeneral._ESTADO_FIN:
				IO.ConsoleLog.println("_ESTADO_FIN");
				_fisica = _FISICA_OFF;
				Mesa.getInstancia().limpiaMesa();
				break;
				
			case EstadoGeneral._ESTADO_EDICION:
				//IO.ConsoleLog.println("_ESTADO_EDICION");
				_fisica = _FISICA_OFF;
				break;
				
			case EstadoGeneral._ESTADO_SALVAR_PANTALLA:
				IO.ConsoleLog.println("_ESTADO_SALVAR_PANTALLA");
				Mesa.getInstancia().salvarPantallaActual();
				EstadoGeneral.setEstado(EstadoGeneral.getEstadoAnterior());
				break;
				
		}
		
		
		
		if(_fisica == _FISICA_ON){
			Mesa.getInstancia().doFisica();
		}else{
			//IO.ConsoleLog.println("set Fisica off");
			Mesa.getInstancia().setFisicaOff(); // per a buidar el buffer del detector de temblores
		}
	}
	
	

	public void fingerPressed(int xp, int yp) {
		Movil m;
		Punto real = Mesa.getInstancia().pixelToReal(xp, yp);
		int x = (int)real.getX();
		int y = (int)real.getY();
		
		switch (EstadoGeneral.getEstado()){
			case(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO):
				m = (Movil)Mesa.getInstancia().getMoviles().firstElement();

				if(m.isParteDentro(new Circular(x,y,30)))
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO_TIRANDO);
			
				break;
			case(EstadoGeneral._ESTADO_EDICION):
				Mesa.getInstancia().getLogicaEditor().fingerPressed(x,y);
			break;
			case(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS):
				Mesa.getInstancia().getLogicaJuego().fingerPressed(x,y);
			break;
		}
	}
	public void fingerDraged(int xp, int yp) {
		
		Punto real = Mesa.getInstancia().pixelToReal(xp, yp);
		int x = (int)real.getX();
		int y = (int)real.getY();
		
		switch (EstadoGeneral.getEstado()){
		case(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO_TIRANDO):

			
			Movil m = (Movil)Mesa.getInstancia().getMoviles().firstElement();
			m.setTirachinas(x, y);
			
			//Codi comentat es per a activar el pintat de trajectoria nomes cuan es colisioni amb una marca de tirada
			//Vector colisonan = new Vector();//TODO: poc productiu, crear variables vector en logica partida i cridar logica partida
			//Vector no_colisionan = new Vector();
			//Circular dedo = new Circular(x, y, 5);//TODO: poc productiu, crear variables vector en logica partida i cridar logica partida //TODO: tamanyo dedo en variables.xml							
			//Mesa.getInstancia().getElementos().marcaTiradaColisionan(dedo,colisonan,no_colisionan);
			
			m.getTrajectoria().refresca(m, (m.getX()-x)*_factor_v_dedo, (m.getY()-y)*_factor_v_dedo, Mesa.getInstancia().getElementos(), 290);
			m.getTrajectoria().setDo_paint(true);
			
			/*
			// TODO: set alpha/longitud a la trajectoria segons la distancia a una marca tirada
			if(colisonan.size()>0){ 
				//TODO: iteracions atribut xml dins movil
				m.getTrajectoria().refresca(m, (m.getX()-x)*_factor_v_dedo, (m.getY()-y)*_factor_v_dedo, Mesa.getInstancia().getElementos(), 290);
				m.getTrajectoria().setDo_paint(true);
			}else
				//m.getTrajectoria().setDo_paint(false);
			
			break;*/
		}
		switch (EstadoGeneral.getEstado()){
			case(EstadoGeneral._ESTADO_EDICION):
				Mesa.getInstancia().getLogicaEditor().fingerDraged(x,y);
			break;
			case(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS):
				Mesa.getInstancia().getLogicaJuego().fingerDraged(x,y);
			break;
		}
	}

	public void fingerRelased(int xp, int yp) {
		Movil m;
		Punto real = Mesa.getInstancia().pixelToReal(xp, yp);
		int x = (int)real.getX();
		int y = (int)real.getY();

		switch (EstadoGeneral.getEstado()){
			case(EstadoGeneral._ESTADO_JUEGO_LANZAMIENTO_TIRANDO):
				m = (Movil)Mesa.getInstancia().getMoviles().firstElement();
				if(!_debug_on)
					m.setV((m.getX()-x)*_factor_v_dedo,(m.getY()-y)*_factor_v_dedo);
				
					IO.ConsoleLog.println("_____MOVIL INIT: "+m);
				
					m.setTirachinas(0, 0);
					
					IO.ConsoleLog.println("lanzamiento a x:"+x+" y:"+y);
					
					Mesa.getInstancia().nuevoElemento(new MarcaTirada(x,y,10));//TODO: tamany marca tirada en variables.xml			
					
					EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_PRE_FISICAS_LANZADAS);
			break;
			case(EstadoGeneral._ESTADO_EDICION):
				Mesa.getInstancia().getLogicaEditor().fingerRelased(x,y);
			break;
			case(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS):
				Mesa.getInstancia().getLogicaJuego().fingerRelased(x,y);
			break;
		}
	}
	
	public void keyTyped(char keyChar) {
		
		//miramos las keys independietes de los estados
		switch(keyChar){
			case 's':
			       IO.ConsoleLog.println("s is pressed");
			       EstadoGeneral.setEstado(EstadoGeneral._ESTADO_SALVAR_PANTALLA);
				break;
			case 'i':
			       IO.ConsoleLog.println("i  is is pressed");
			       EstadoGeneral.setEstado(EstadoGeneral._ESTADO_INICIO);
				break;
			case 'o':
			       IO.ConsoleLog.println("o  is is pressed");
			       EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS);
				break;
			case 'u':
			       IO.ConsoleLog.println("p  is is pressed");
			       ((Movil)Mesa.getInstancia().getMoviles().firstElement()).setV(0, 0);
			       EstadoGeneral.setEstado(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS);
				break;
			case 'e':
			     IO.ConsoleLog.println("e is is pressed");
			     EstadoGeneral.setEstado(EstadoGeneral._ESTADO_EDICION);
				break;
				
				
		}
		
		
		switch (EstadoGeneral.getEstado()){
			case(EstadoGeneral._ESTADO_EDICION):
				Mesa.getInstancia().getLogicaEditor().keyTyped(keyChar);
			break;
			case(EstadoGeneral._ESTADO_JUEGO_FISICAS_LANZADAS):
				Mesa.getInstancia().getLogicaJuego().keyTyped(keyChar);
			break;
		}
		
	}
}
