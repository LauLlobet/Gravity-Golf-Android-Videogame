package milkyway.geometria;

import java.util.*;

import milkyway.Mesa;

public class Punto {
	private double x;
	private double y;
	
	public Punto(double _x,double _y)
	{
		setX(_x);
		setY(_y);
	}
	public String toString()
	{
		return "("+getX()+","+getY()+")";
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public double getXRelatiu() {
		return Mesa.getInstancia().getoffsetX()+ x*Mesa.getInstancia().getescalat();
	}

	public double getYRelatiu() {
		return Mesa.getInstancia().getoffsetY()+ y*Mesa.getInstancia().getescalat();
	}
	
	
}
