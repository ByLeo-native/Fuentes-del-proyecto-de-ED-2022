package Excepciones;

/**
 * Clase InvalidKeyException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada en caso que una clave sea invalida.
 */
public class InvalidKeyException extends Exception {
	
	/**
	 * Constructor de la InvalidKeyException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public InvalidKeyException(String msg) {
		super(msg);
	}
}
