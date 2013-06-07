package milkyway.animaciones;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;

/**
 * Genera la animaci�n al mover un elemento
 *
 */
public class AniMoverAcelerado extends Animacion{

	public AniMoverAcelerado(double vx, double vy,double ax,double ay, int pasos){
		super();

		double vpy = vy;
		double vpx = vx;
		
		for (int i=0;i<pasos;i++){
			this._pasos.push(new Paso(vpx,vpy,0,0,0));
			vpx = vpx + ax;
			vpy = vpy + ay;
		}
		
		this._estado = 1;
	}
}
