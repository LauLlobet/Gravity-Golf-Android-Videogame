package IO;


public class TextMessage {

	private static TextMessage _instancia = null;
	
	public static TextMessage getInstancia(){
		if(_instancia == null)
			_instancia = new TextMessage();
		return _instancia;
	}
	
	protected TextMessage(){
		
	}
	
	
	public void setText(String text){
			IO.ConsoleLog.println("MISSATGE DE TEXTMESSAGE:"+text);
	}
	
}
