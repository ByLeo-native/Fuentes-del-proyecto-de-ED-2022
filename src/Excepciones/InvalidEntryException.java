package Excepciones;

/**
 * Clase InvalidEntryException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada en caso que una entrada sea invalida.
 */
public class InvalidEntryException extends Exception {
	
	/**
	 * Constructor de la InvalidEntryException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public InvalidEntryException(String msg) {
		super(msg);
	}
}
