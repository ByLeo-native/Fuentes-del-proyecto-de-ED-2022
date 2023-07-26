package Excepciones;

/**
 * Clase InvalidPositionExceptoin que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada en caso que una posicion sea invalida.
 */
public class InvalidPositionException extends Exception{
	
	/**
	 * Constructor de la InvalidPositionException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public InvalidPositionException(String msg) {
		super(msg);
	}
}
