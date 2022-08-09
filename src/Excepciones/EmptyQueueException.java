package Excepciones;

/**
 * Clase EmptyQueueException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada cuando se solicita el frente o desencolar en una cola vacia.
 */
public class EmptyQueueException extends Exception{
	
	/**
	 * Constructor de la EmptyQueueException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public EmptyQueueException (String msg) {
		super(msg);
	}
}
