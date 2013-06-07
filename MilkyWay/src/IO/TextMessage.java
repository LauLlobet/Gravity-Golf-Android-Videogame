package IO;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class TextMessage {

	private static TextMessage _instancia = null;
	Handler _handler;
	
	public static TextMessage getInstancia(){
		if(_instancia == null)
			_instancia = new TextMessage();
		return _instancia;
	}
	
	protected TextMessage(){
		
	}
	
	public void setHandler(Handler c){
		_handler = c;
	}
	
	public void setText(String text){
		Message msg = Message.obtain();
	    Bundle b = new Bundle();
	    b.putString("text", "Victoria");
	    b.putInt("viz", View.VISIBLE);
	    msg.setData(b);
	    _handler.sendMessage(msg);
	}
	
}
