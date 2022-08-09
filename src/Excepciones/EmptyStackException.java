package Excepciones;

/**
 * Clase EmptyStackException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada cuando se intenta quitar un elemento en una pila vacia.
 */
public class EmptyStackException extends Exception {
	
	/**
	 * Constructor de la EmptyStackException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public EmptyStackException(String msg) {
		super(msg);
	}
}
