package com.socunasindria.milkyway;


import java.io.FileInputStream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MilkyWayView extends SurfaceView implements SurfaceHolder.Callback {

	
	MilkyWayThread _thread;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView _StatusText;

	
	public MilkyWayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        // create thread only; it's started in surfaceCreated()
        _thread = new MilkyWayThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                _StatusText.setVisibility(m.getData().getInt("viz"));
                _StatusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true); // make sure we get key events
    }
	
    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        _StatusText = textView;
   
    }
	
    public MilkyWayThread getThread() {
		return _thread;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	
		_thread.setScreenSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	       _thread.start();
	       
	       
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		
	}



	
}
