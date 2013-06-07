package IO;

import android.util.Log;

public class ConsoleLog {

	public static void println(String clase, String message){
		Log.v(clase,message);
	}
	
	public static void println(String message){
		Log.v("",message);
	}
	
}
