package milkyway.animaciones;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;

/**
 * Genera la animaci�n al mover un elemento
 *
 */
public class AniMover extends Animacion{

	public AniMover(double x, double y, int pasos){
		super();

		double distX_paso = x / pasos;
		double distY_paso = y / pasos;
		
		for (int i=0;i<pasos;i++){
			this._pasos.push(new Paso(distX_paso,distY_paso,0,0,0));
		}
		
		this._estado = 1;
	}
}
