package milkyway.logica;

import java.util.Iterator;
import java.util.Vector;

import milkyway.elementos.Circular;
import milkyway.elementos.Diana;
import milkyway.elementos.Editable;
import milkyway.elementos.Elemento;
import milkyway.elementos.MarcaTirada;


public class VectorElementos extends Vector{
	
	public Vector elementosColisionan(Circular m){
		Vector colisionan = new Vector();
		
		Iterator it = this.iterator();
		while(it.hasNext()) {
			Elemento e = (Elemento)it.next();
			if(e.isParteDentro(m))
				colisionan.add(e);
		}
		return colisionan;
	}

	public Vector circularesColisionan(Circular m){
		Vector colisionan = new Vector();
		
		Iterator it = this.iterator();
		while(it.hasNext()) {
			Elemento e = (Elemento)it.next();
			if(e.isParteDentro(m) && e instanceof Editable)
				colisionan.add(e);
		}
		return colisionan;
	}
	
	public Vector dianasColisionan(Circular m){
		Vector colisionan = new Vector();
		
		Iterator it = this.iterator();
		while(it.hasNext()) {
			Elemento e = (Elemento)it.next();
			if(e.isParteDentro(m) && e instanceof Diana)
				colisionan.add(e);
		}
		return colisionan;
	}
	
	public void marcaTiradaColisionan(Circular m, Vector colisionan,Vector no_colisionan){
		
		Iterator it = this.iterator();
		while(it.hasNext()) {
			Elemento e = (Elemento)it.next();
			if(e instanceof MarcaTirada){
				if(e.isParteDentro(m))
					colisionan.add(e);
				else
					no_colisionan.add(e);
			}
		}
	}
	
	@Override
	public boolean add(Object o){
		if(!(o instanceof Elemento))
			return false;
		((Elemento)o).setContenedorElementos(this);
		return super.add(o);
	}
	
	
}
