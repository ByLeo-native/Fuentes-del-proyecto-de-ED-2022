package Excepciones;

/**
 * Clase InvalidOperationException que extiende de la clase Exception.
 * @author Leonardo Paillamilla, UNS.
 * Utilizada en caso que se solicite crear la raiz de un árbol que ya cuenta con un nodo raiz.
 */
public class InvalidOperationException extends Exception {
	
	/**
	 * Constructor de la InvalidOperationException con un mensaje.
	 * @param msg String mensaje de la excepcion.
	 */
	public InvalidOperationException(String msg) {
		super(msg);
	}
}
