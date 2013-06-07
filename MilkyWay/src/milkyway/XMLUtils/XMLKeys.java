package milkyway.XMLUtils;

import java.util.Vector;

public class XMLKeys {
	
	public static final double emptyDouble = 999999;
	public static final int emptyInt = 999999;
	public static final String emptyString = null;
	
	public static final int detectorTembloresApertura = 	0;
	public static final int detectorTembloresSimilitudVeclocidades =	1;
	public static final int detectorTembloresVelocidadMinimaParado = 2;
	public static final int detectorTembloresVelocidadMinimaTemblor = 3;
	public static final int detectorTembloresLongitudBuffer = 4;
	public static final int relojFps = 5; 
	public static final int efectoReboteVLimit = 6; 
	public static final int efectoReboteMargen = 7;
	public static final int logicaFactorVDedo = 8;
	
	public static final int movil = 9;
	public static final int diana = 10;
	public static final int planeta = 11; 
	public static final int zonaInercia = 12;
	public static final int x = 13;
	public static final int y = 14;
	public static final int radio = 15; 
	public static final int vx = 16;
	public static final int vy = 17;
	public static final int masa = 18;
	public static final int cola_length = 19;
	public static final int longitud = 20;
	public static final int altura = 21;
	public static final int friccion = 22;
	public static final int efectoGravedad = 23;
	public static final int efectoRebote = 24;
	public static final int desgasteX = 25;
	public static final int desgasteY = 26;
	
	public static final int texturaLocation = 28;
	public static final int efectoInercia = 29;
	public static final int efectoParada = 30;
	
	public static final int pantalla = 31;

	public static final int debugOn = 32;
	
	public static final int constanteGravitacional = 33;
	
	public static final int nubePolvo = 34;
	public static final int separacionPolvos = 35;
	public static final int efectoNubePolvo = 36;
	public static final int numeroPolvos = 37;
	
	public static final int marcaTextura = 38;
	
	public static final int dianaFinal = 39;
	
	public static final int polvoTextura = 40;
	public static final int radioPolvo = 41;
	
	  static String search[] ={
          "apertura_maxima",
          "similitud_velocidades",
          "velocidad_minima_parado",
          "velocidad_temblor",
          "longitud_buffer",
          "fps",
          "efecto_rebote_v_limit",
          "efecto_rebote_margen",
          "factor_v_dedo",
          "Movil",
          "Diana",
          "Planeta",
          "ZonaInercia",
          "x",
          "y",
          "radio",
          "vx",
          "vy",
          "masa",
          "cola_length",
          "longitud",
          "altura",
          "friccion",
          "EfectoGravedad",
          "EfectoRebote",
          "desgasteX",
          "desgasteY",
          "------",
          "textura_location",
          "EfectoInercia",
          "EfectoParada",
          "Pantalla",
          "debug_on",
          "constante_gravitacional",
          "nube_polvo",
          "separacion_polvos",
          "EfectoNubePolvo",
          "numero_polvos",
          "marca_textura",
          "diana_final",
          "polvo_textura",
          "radio_polvo",
          
      };
	  
	
	public XMLKeys(){
		
	}
	
    public static int identify(String token)
    {
        if(token != null)
        {
            int idx = 0;
            while (idx < search.length)
            {
                if (token.equals(search[idx]))
                    return idx;
                idx++;
            }

            idx = 0;
        }
        return -1;
    }

    public static String id2str(int id)
    {
           return search[id];
    }
}
