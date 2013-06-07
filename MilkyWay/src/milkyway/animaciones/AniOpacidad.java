package milkyway.animaciones;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;

/**
 * Genera la animaci�n al mover un elemento
 *
 */
public class AniOpacidad extends Animacion{

	public AniOpacidad(double alpha, int pasos){
		super();

		double alp_paso = alpha / pasos;
		
		for (int i=0;i<pasos;i++){
			this._pasos.push(new Paso(0,0,0,0,alp_paso));
		}

		this._estado = 1;
	}
}
