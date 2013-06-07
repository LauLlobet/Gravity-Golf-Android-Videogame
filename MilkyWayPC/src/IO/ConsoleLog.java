package IO;

public class ConsoleLog {

	public static void println(String clase, String message){
		System.out.println( "["+clase+"]"+message);
	}
	
	public static void println(String message){
		System.out.println( "[?]"+message);
	}
	
}
