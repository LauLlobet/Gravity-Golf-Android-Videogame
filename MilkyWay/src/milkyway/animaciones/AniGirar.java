package milkyway.animaciones;

import milkyway.animaciones.Animacion;
import milkyway.animaciones.Paso;


public class AniGirar extends Animacion{

	public AniGirar(double angulo, int pasos){
		super();

		double ang_paso = angulo / pasos;
		
		for (int i=0;i<pasos;i++){
			this._pasos.push(new Paso(0,0,0,ang_paso,0));
		}

		this._estado = 1;
	}
}
