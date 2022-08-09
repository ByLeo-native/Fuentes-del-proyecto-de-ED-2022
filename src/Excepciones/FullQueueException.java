package Excepciones;

/**
 * Clase FullQueueException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada cuando en caso de que se intente encolar un elemento en una cola con arreglo llena.
 */
public class FullQueueException extends Exception {
	
	/**
	 * Constructor de la FullQueueException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public FullQueueException(String msg) {
		super(msg);
	}
}
