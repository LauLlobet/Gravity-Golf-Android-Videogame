package com.socunasindria.milkyway;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.xml.sax.InputSource;

import milkyway.Mesa;
import milkyway.logica.ResManager;

import IO.CanvasPainter;
import IO.PantallaWriter;
import IO.SoundPainter;
import IO.TextMessage;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

public class MilkyWayThread extends Thread{

    /** Message handler used by thread to interact with TextView */
    private Handler _Handler;
    /** Handle to the surface manager object we interact with */
    private SurfaceHolder _SurfaceHolder;
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context _Context;

    
    private Mesa _mesa;
    private CanvasPainter _painter;
    boolean _is_ruuning = true;
    
   
    public MilkyWayThread(SurfaceHolder surfaceHolder, Context context,
            Handler handler) {
        // get handles to some important objects
        _SurfaceHolder = surfaceHolder;
        _Handler = handler;
        _Context = context;
        
        _painter = new CanvasPainter();
        
        
    	try {
    		
    		AssetManager mgr =  _Context.getAssets();
			
			ResManager.getInstancia().setLoadVariables(new InputSource(mgr.open("variables.xml")));
			ResManager.getInstancia().setPantallas(new InputSource(mgr.open("pantallas.xml")));
			
			PantallaWriter.getInstancia().setContext(_Context);
    	
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    public void stopThread(){
    	_is_ruuning = false;
    }
    
    public void setScreenSize(int x, int y){
    	Mesa.getInstancia().setScreenSize(x,y);
    }
    
    @Override
    public void run() {
    	
	    _mesa = Mesa.getInstancia();
	    
	    long now = System.currentTimeMillis();
	    
	    TextMessage.getInstancia().setHandler(_Handler);
	    
        while (_is_ruuning) {
            Canvas c = null;
            
            try {
                c = _SurfaceHolder.lockCanvas(null);
                synchronized (_SurfaceHolder) {
	                _mesa.doTimed(System.currentTimeMillis()-now);
	                _painter.paintComponent(c);
	                SoundPainter.paintSound();
	                now = System.currentTimeMillis();
	                
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _SurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

}
