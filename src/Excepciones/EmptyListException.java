package Excepciones;

/**
 * Clase EmptyListException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada cuando se solicita una operacion de un elemento de una lista cuando la lista esta vacia.
 */
public class EmptyListException extends Exception{
	
	/**
	 * Constructor de la EmptyListException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public EmptyListException(String msg) {
		super(msg);
	}
}
