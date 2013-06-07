package milkyway.animaciones;

/*
 * Clase abstracta que define una animacion
 * 
 */

import java.util.Stack;

import milkyway.elementos.Elemento;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;

public abstract class Animacion {
	int _estado; //0:vacia , 1:con pasos
	Stack _pasos;
	
	public Animacion(){
		_pasos = new Stack();
		_estado = 0; //vacia
	}
	
	public int getNumPasos(){
		return _pasos.size();
	}
	
	public boolean finalizada(){
		if (_estado == 0) return true;
		return false;//TODO: animacion en bucle
	}
	
        public boolean aplicaAnimacion(Elemento e){
                Paso p_tmp = (Paso)_pasos.pop();
                        //aplicamos el paso de la animacion
                p_tmp.aplicaPaso(e);

                //miramos si la animacion ha finalizado
                if (_pasos.isEmpty()){
                        _estado = 0;
                        return true;
                }
                return false;        
	}
	
	//junta dos animaciones en una
	public void mezclaAnimacion(Animacion a, boolean sincronia){
		Paso paso_tmp;
		int num_pasos1 = _pasos.size();
		int num_pasos2 = a.getNumPasos();		
		
		// comprovamos si tienen el mismo n�mero de pasos
		if (num_pasos1 == num_pasos2){
			//si tienen los mismos solo hay que unir las animaciones paso a paso
			for (int i=0;i<num_pasos1;i++){
				paso_tmp = (Paso)a._pasos.get(i);
				((Paso)_pasos.get(i)).juntaPaso(paso_tmp);
			}
		}
		else{
			//si no tienen el mismo n�mero de pasos hay que mirar la sincron�a
			if (sincronia){
				//si hay sincronia tenemos que recalcular la animacion recibida
				a.ajustaNumPasos(num_pasos1);
				this.mezclaAnimacion(a, sincronia);
			}
			else{
				//si no hay sincronia unimos los pasos en com�n
				int min_pasos = Math.min(num_pasos1, num_pasos2);
				for (int i=0;i<min_pasos;i++){
					paso_tmp = (Paso)a._pasos.get(i);
					((Paso)_pasos.get(num_pasos1-i-1)).juntaPaso(paso_tmp);
				}
				//luego adjuntamos los que tiene de m�s la segunda animaci�n (si los tiene)
				for (int i=min_pasos;i<num_pasos2;i++){
					_pasos.insertElementAt(a._pasos.get(i),0);
				}
			}
		}//if (mismos pasos)
	}
	
	//A�ade los pasos de una nueva animaci�n despu�s de los existentes
	public void concadenaAnimacion(Animacion a){
		Paso paso_tmp;
		
		//recorremos todos los pasos de la animacion y los insertamos
		int num_pasos_a = a.getNumPasos();
		for (int i=0;i<num_pasos_a;i++){
			paso_tmp = (Paso)a._pasos.pop();
			_pasos.insertElementAt(paso_tmp,0);
		}
	}
	
	//Ajusta el n�mero de pasos de una animaci�n
	public void ajustaNumPasos(int num_pasos){
		double totalX = 0;
		double totalY = 0;
		double totalTamano = 0;
		double totalAngulo = 0;
		double totalAlpha = 0;
		Paso paso_tmp;
		
		int num_pasos_or = _pasos.size();
		//recorremos todos los pasos en la pila
		for (int i=0;i<num_pasos_or;i++){
			paso_tmp = (Paso)_pasos.pop();
			totalX += paso_tmp.x;
			totalY += paso_tmp.y;
			totalTamano += paso_tmp.tamano;
			totalAngulo += paso_tmp.angulo;
			totalAlpha += paso_tmp.alpha;
		}
		
		//calculamos variaciones en cada paso
		double DX = totalX / num_pasos;
		double DY = totalY / num_pasos;
		double DTamano = totalTamano / num_pasos;
		double DAngulo = totalAngulo / num_pasos;
		double DAlpha = totalAlpha / num_pasos;
		
		_pasos.clear();
		//generamos los nuevos pasos
		for (int i=0;i<num_pasos;i++){
			_pasos.push(new Paso(DX,DY,DTamano,DAngulo,DAlpha));
		}
	}
	
}
