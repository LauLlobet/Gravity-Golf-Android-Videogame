package milkyway.logica;

import java.util.Iterator;

import milkyway.logica.Logica;
import milkyway.logica.Pantalla;
import milkyway.logica.ResManager;

import milkyway.Mesa;
import milkyway.elementos.Asteroide;
import milkyway.elementos.Circular;
import milkyway.elementos.Elemento;
import milkyway.elementos.Masa;
import milkyway.elementos.Forma;
import milkyway.elementos.Planeta;


public class LogicaEditor extends Logica{

	public static final int _ESTADO_INICIO = 0;
	
	public static float _radio_dedo = 10;
	public static int _estado = _ESTADO_INICIO;
	public static Elemento _focus_elemento = null;
	public static Forma _forma_construintse = null;
	
	public static double _x_offset_dedo_elemento=0;
	public static double _y_offset_dedo_elemento=0;
	
	
	
	public LogicaEditor() {

	
	}

	@Override
	public void fingerPressed(int x, int y) {
		
		_focus_elemento = null;
		
		if(_forma_construintse !=null){
			_forma_construintse.addPuntoLinea(x, y);
			if(_forma_construintse.getSizePuntos() == 1){
				Mesa.getInstancia().nuevoElemento(_forma_construintse);
			    ((Pantalla)ResManager.getInstancia().getPantallas().get(0)).add(_forma_construintse);
			}
			return;
		}
		
		
		Circular m = new Circular(x, y, _radio_dedo);
		Iterator elementos = Mesa.getInstancia().getElementos().circularesColisionan(m).iterator();
		if(elementos.hasNext()){
			_focus_elemento = (Elemento)elementos.next();
			_x_offset_dedo_elemento = _focus_elemento.getX()-m.getX();
			_y_offset_dedo_elemento = _focus_elemento.getY()-m.getY();
		}
		
	}

	@Override
	public void fingerRelased(int x, int y) {
		
		_focus_elemento = null;
		
	}

	@Override
	public void fingerDraged(int x, int y) {
		
		if(_forma_construintse != null){
			_forma_construintse.addPuntoLinea(x, y);
		}
		
		if(_focus_elemento == null)
			return;
		_focus_elemento.setXY( x + _x_offset_dedo_elemento, y + _y_offset_dedo_elemento);
	}

	@Override
	public void keyTyped(char keyChar) {
		
		switch(keyChar){
		case 'm':
		      if(_focus_elemento != null && _focus_elemento instanceof Masa){
		    	  Masa m = (Masa)_focus_elemento;
		    	  m.setMasa( m.getMasa() +1);
		      }
			break;
			
		case 'n':
		      if(_focus_elemento != null && _focus_elemento instanceof Masa){
		    	  Masa m = (Masa)_focus_elemento;
		    	  m.setMasa( m.getMasa() -1);
		      }
			break;
			
		case 'b':
		      if(_focus_elemento != null && _focus_elemento instanceof Circular){
		    	  Circular m = (Circular)_focus_elemento;
		    	  m.setRadio( m.getRadio() +1);
		      }
			break;
			
		case 'v':
		      if(_focus_elemento != null && _focus_elemento instanceof Circular){
		    	  Circular m = (Circular)_focus_elemento;
		    	  m.setRadio( m.getRadio() -1);
		      }
			break;
			
		case 'r':
		      if(_focus_elemento != null){
		    	  Mesa.getInstancia().eliminaElemento(_focus_elemento);
		    	  ((Pantalla)ResManager.getInstancia().getPantallas().get(0)).remove(_focus_elemento);
		    	  _focus_elemento = null; 
		      }
			break;
			
		case 'p':
		      
			Planeta p = new Planeta(50,100,100,10);

			
		    Mesa.getInstancia().nuevoElemento(p);
		    ((Pantalla)ResManager.getInstancia().getPantallas().get(0)).add(p);
		    	
			break;

		case 'ñ':
		      
			_forma_construintse = new Forma();
			_forma_construintse.setTipo(new Planeta(0,0,0,10) );
			
			break;
			
		case 'k':
		      
			_forma_construintse = new Forma();
			_forma_construintse.setTipo(new Asteroide(0.3,10) );
			
			break;
			
		case 'l':
		      
			_forma_construintse = null;
		    	
			break;
			
		default:
			break;
			
		}
		
	}
	
}
