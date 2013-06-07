package milkyway.animaciones;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;


public class AniEscalar extends Animacion{

	public AniEscalar(double tamano, int pasos){
		super();

		double tam_paso = tamano / pasos;
		
		for (int i=0;i<pasos;i++){
			this._pasos.push(new Paso(0,0,tam_paso,0,0));
		}

		this._estado = 1;
	}
}
