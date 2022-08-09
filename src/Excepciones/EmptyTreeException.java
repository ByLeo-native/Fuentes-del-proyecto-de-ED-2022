package Excepciones;

/**
 * Clase EmptyTreeException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada en algunas operaciones de Tree cuando el árbol esta vacio.
 */
public class EmptyTreeException extends Exception {
	
	/**
	 * Constructor de la EmptyTreeException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public EmptyTreeException(String msg) {
		super(msg);
	}
}
