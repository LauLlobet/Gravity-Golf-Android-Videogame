package milkyway.animaciones;

import milkyway.animaciones.AniEscalar;
import milkyway.animaciones.AniOpacidad;
import milkyway.animaciones.Animacion;

/**
 * Genera la animaci�n al incorporar un nuevo elemento en la mesa
 *
 */
public class AniIncorporacion extends Animacion{

	public AniIncorporacion(int pasos){
		super();

		Animacion escalado = new AniEscalar(1, pasos);
		Animacion opacidad = new AniOpacidad(-1, pasos);
		
		this.concadenaAnimacion(escalado);
		this.mezclaAnimacion(opacidad, true);

		this._estado = 1;
	}
}
