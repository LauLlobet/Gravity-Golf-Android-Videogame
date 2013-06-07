package com.socunasindria.milkyway;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.puredata.android.service.PdPreferences;
import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.PdReceiver;
import org.puredata.core.utils.IoUtils;

import milkyway.Mesa;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MilkyWayActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{
    /** Called when the activity is first created. */
	
	MilkyWayThread _MilkyWayThread;
	MilkyWayView _MilkyWayView;
	
	private PdService pdService = null;
	

	private PdReceiver receiver = new PdReceiver() {

		private void pdPost(String msg) {
			Log.v("pdreceiver","Pure Data says, \"" + msg + "\"");
		}

		@Override
		public void print(String s) {
			Log.v("pdreceiver",s);
		}

		@Override
		public void receiveBang(String source) {
			Log.v("pdreceiver","bang");
		}

		@Override
		public void receiveFloat(String source, float x) {
			Log.v("pdreceiver","float: " + x);
		}

		@Override
		public void receiveList(String source, Object... args) {
			Log.v("pdreceiver","list: " + Arrays.toString(args));
		}

		@Override
		public void receiveMessage(String source, String symbol, Object... args) {
			Log.v("pdreceiver","message: " + Arrays.toString(args));
		}

		@Override
		public void receiveSymbol(String source, String symbol) {
			Log.v("pdreceiver","symbol: " + symbol);
		}
	};

	
	private final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder)service).getService();
			initPd();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// this method will never be called
		}
	};

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //pd staff
        PdPreferences.initPreferences(getApplicationContext());
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
		
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.milkyway_layout);
        
        // get handles to the LunarView from XML, and its LunarThread
        _MilkyWayView = (MilkyWayView) findViewById(R.id.milkyway);
        _MilkyWayThread = _MilkyWayView.getThread();

        // give the LunarView a handle to the TextView used for messages
        _MilkyWayView.setTextView((TextView) findViewById(R.id.text));
		
		
		//bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
    }
    

	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		startAudio();
	}
	

	private void initPd() {
		
		
		//DESACTIVANDO AUDIO 
		
		if(true)
			return;
		
		
		
		Resources res = getResources();
		File patchFile = null;
		try {
			PdBase.setReceiver(receiver);
			PdBase.subscribe("android");
			
			File dir = getFilesDir();
			patchFile = new File(dir, "sound_render.pd");
			IoUtils.extractZipResource(getResources().openRawResource(R.raw.patch), dir, true);
			PdBase.openPatch(patchFile.getAbsolutePath());
			
			/*InputStream in = res.openRawResource(R.raw.test);
			patchFile = IoUtils.extractResource(in, "test.pd", getCacheDir());
			PdBase.openPatch(patchFile);*/
			startAudio();
		} catch (IOException e) {
			Log.e("intiPd", e.toString());
			finish();
		} finally {
			if (patchFile != null) patchFile.delete();
		}
	}

	private void startAudio() {
		String name = getResources().getString(R.string.app_name);
		try {
			pdService.initAudio(-1, -1, -1, -1);   // negative values will be replaced with defaults/preferences
			pdService.startAudio(new Intent(this, MilkyWayActivity.class), R.drawable.milky_app, name, "Return to " + name + ".");
		} catch (IOException e) {
			Log.e("Startig audio",e.toString());
		}
	}
	
    @Override
    public void onPause(){
    	
    	super.onPause();
    	//_MilkyWayThread.interrupt();
    	
    	_MilkyWayThread.stopThread();
    	this.finish();
    }
    
    @Override 
    public void  onSaveInstanceState(Bundle b){
    	//_MilkyWayThread.stop();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent me) {

        int action = me.getAction();
        int currentXPosition = (int)me.getX();
        int currentYPosition = (int)me.getY();

        Log.v("MotionEvent", "Action = " + action);
        Log.v("MotionEvent", "X = " + currentXPosition + "Y = " + currentYPosition);
        Log.v("MotionEvent","padding top: "+getWindow());

       ;
        
        if (action == MotionEvent.ACTION_MOVE) {
            
        	
        	Mesa.getInstancia().getLogicaGeneral().fingerDraged(currentXPosition, currentYPosition);
        	
        }
        if (action == MotionEvent.ACTION_UP) {
          
 
        	
        	Mesa.getInstancia().getLogicaGeneral().fingerRelased(currentXPosition, currentYPosition);
            
        }
        
        if (action == MotionEvent.ACTION_DOWN) {

        	
          	Mesa.getInstancia().getLogicaGeneral().fingerPressed(currentXPosition, currentYPosition);
            
       }

        return true;
      }
}