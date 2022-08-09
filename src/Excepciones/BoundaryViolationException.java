package Excepciones;

/**
 * Clase BoundaryViolationException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada cuando se solicita una posicion que excede de los limites de una lista.
 */
public class BoundaryViolationException extends Exception {
	
	/**
	 * Constructor de la BoundaryViolationException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public BoundaryViolationException(String msg) {
		super(msg);
	}
}
