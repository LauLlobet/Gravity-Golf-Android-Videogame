package milkyway.logica;

import milkyway.Mesa;
import milkyway.elementos.Movil;

public abstract class Logica {

		public abstract void fingerPressed(int x, int y);
		public abstract void fingerRelased(int x, int y);
		public abstract void fingerDraged(int x, int y);
		public abstract void keyTyped(char keyChar);
}
