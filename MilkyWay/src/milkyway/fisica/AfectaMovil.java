package milkyway.fisica;
import java.util.Vector;
import java.util.Enumeration;

public interface AfectaMovil {
		public Enumeration getEfectos();
		public Enumeration getEfectosSegundos(double x, double y);
		public Enumeration getEfectosTerceros(double x, double y);
		public Enumeration getEfectosCuartos();
}
