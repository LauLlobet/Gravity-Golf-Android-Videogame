package IO;

import milkyway.Mesa;
import milkyway.elementos.Movil;

import org.puredata.core.PdBase;

public class SoundPainter {

	
	public static void paintSound(){
		
		Mesa mesa = Mesa.getInstancia();
		if(mesa.getMoviles().size() == 0)
			return;
		Movil m = (Movil)mesa.getMoviles().firstElement();
		
		//PAN
		double x = m.getX();
		
		double p = 0.5/(mesa.centre_x+mesa.marge_so);
		double n = 0.5 - p*mesa.centre_x;
		double pan = p * x + n;
		setPan(notExtrem(pan));
		
		//DISTANCIA
		double y = m.getY();
		
		double x1 = mesa.pantalla_y +mesa.marge_so;
		double y1 = 1;
		double x2 = - mesa.marge_so;
		double y2 = 0;
		
		
		p = (y1-y2)/(x1-x2);
		n = y2 - p*x2;
		double distancia = p * y + n;
		setDistancia(notExtrem(distancia));
		
		//VELOCITAT
		double vy = m.getVy();
		

		x1 = 8;
		y1 = 1;
		x2 = -8;
		y2 = 0;
		
		
		p = (y1-y2)/(x1-x2);
		n = y2 - p*x2;
		double  velocitat = p * vy + n;
		setVelocitat(notExtrem(velocitat));
		
		
	}
	
	public static double notExtrem(double x){
		if(x>1)
			return 1;
		if(x<0)
			return 0;
		return x;
	}
	
	
	public static void setDistancia(double distancia){
		PdBase.sendFloat("distancia", (float)distancia);
	}
	public static void setPan(double pan){
		PdBase.sendFloat("pan", (float)pan);
	}
	public static void setVelocitat(double velocitat){
		PdBase.sendFloat("velocitat", (float)velocitat);
	}
	public static void setActivitat(double activitat){
		PdBase.sendFloat("activitat", (float)activitat);
	}
	
}
