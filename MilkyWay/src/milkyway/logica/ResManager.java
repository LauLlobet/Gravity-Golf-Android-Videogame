package milkyway.logica;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import milkyway.*;
import milkyway.XMLUtils.NBEasyXML;
import milkyway.XMLUtils.XMLKeys;
import milkyway.fisica.DetectorTemblores;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import milkyway.logica.Pantalla;
import milkyway.logica.ResManager;


import java.io.*;


public class ResManager{
	
	private static ResManager _instancia = null;
	private InputSource _variables_location;
	private InputSource _pantallas_location;
	private Hashtable _variables;
	private Vector _pantallas;
	private XMLKeys _keys;
	private NBEasyXML _variablesXML;
	private NBEasyXML _pantallasXML;
	
	protected ResManager(){
		_pantallas = new Vector();
		_variables = new Hashtable();
	}
	
	public static ResManager getInstancia(){
		if(_instancia == null)
			_instancia = new ResManager();
		return _instancia;
	}
	
	public void setLoadVariables(InputSource resource_variables_location){
	
		this._variables_location = resource_variables_location;
		_variablesXML = new NBEasyXML(_variables_location);
		parseVariables();
		
	}
	
	
	public void setPantallas(InputSource resource_pantallas_location){
		this._pantallas_location = resource_pantallas_location;
		_pantallasXML = new NBEasyXML(_pantallas_location);
		
	}
	
	public void parseVariables(){
		
		
        int testDepth=0;
        int position = 0;
        
        while ((position < _variablesXML.size())&&(position != -1)){
            int op = position;
            
                int id = _keys.identify(_variablesXML.getName(position));  
                switch (id)
                {
                	//se saltara variables i detector de temblores
                    case XMLKeys.detectorTembloresApertura:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.detectorTembloresLongitudBuffer:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.detectorTembloresSimilitudVeclocidades:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.detectorTembloresVelocidadMinimaParado:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.detectorTembloresVelocidadMinimaTemblor:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.relojFps:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.efectoReboteMargen:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.efectoReboteVLimit:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.logicaFactorVDedo:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.debugOn:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.constanteGravitacional:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.marcaTextura:
                    	position = parseVar(position);
                    break;
                    
                    case XMLKeys.polvoTextura:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.radioPolvo:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.separacionPolvos:
                    	position = parseVar(position);
                    break;
                    case XMLKeys.numeroPolvos:
                    	position = parseVar(position);
                    break;
                    
               	 	default:
               	 		
               	 //		IO.ConsoleLog.println("tag no reconocido en variables.xml");
               	 	break;
               	 		
                }
            if (position == op) position++;
        }
	}
	
	private int parseVar(int position){

		int peer = _variablesXML.findNextPeer(position);
        String var = _variablesXML.getContent(position);
        this.putVariable(_variablesXML.getName(position), var);
        
        if(peer == -1)
        	peer = _variablesXML.findOtherElement(position);
        
		return peer;
	}
	
	private void putVariable(String key,Object obj){
		//IO.ConsoleLog.println("variable cargada"+key+" val:"+obj);
		_variables.put(key, obj);
	}
	
	public double getVariableDouble(int key_id){
		return Double.parseDouble((String)_variables.get(_keys.id2str(key_id)));
	}
	
	public boolean getVariableBoolean(int key_id){
		String key = _keys.id2str(key_id);
		String ans = (String)_variables.get(key);
		if(ans == null)
			IO.ConsoleLog.println("boolean no existeix en variables");
		boolean b =  Boolean.parseBoolean(ans);
		return b;
	}
	
	
	public int getVariableInt(int key_id){
		return Integer.parseInt((String)_variables.get(_keys.id2str(key_id)));
	}
	
	public String getVariableString(int key_id){
		return ((String)_variables.get(_keys.id2str(key_id)));
	}
	
	public void parsePantallas(){
		int testDepth=0;
        int position = 0;
        _pantallas = new Vector();
        
        while ((position < _pantallasXML.size())&&(position != -1)){
            int id = _keys.identify(_pantallasXML.getName(position));  
            switch (id)
            {
            	case XMLKeys.pantalla:
            		Pantalla p = new Pantalla();
            		position = p.parse(_pantallasXML, position);
            		_pantallas.add(p);
                break;
           	 	default:
                    position++;
           		break;
            }

        }
        
	}
	
	public Vector getPantallas(){
		return _pantallas;
	}


}
