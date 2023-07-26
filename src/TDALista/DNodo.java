package TDALista;

/**
 * Clase de un nodo doble enlazado que implementa la interfaz Position
 * Nodo que mantiene referencia a hasta dos nodos y almacena un elemento.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico del elemento que se almacena en el nodo.
 */
public class DNodo <E> implements Position <E> {
	//Atributos
	private E elemento;
	private DNodo<E> previo, siguiente;
	
	/**
	 * Constructor de un nodo doblemente enlazado que almacena un elemento.
	 * @param item elemento de tipo generico.
	 * @param p Nodo doblemente enlazado previo.
	 * @param n Nodo doblemente enlazado siguiente.
	 */
	public DNodo(E item, DNodo<E> p, DNodo<E> n) {
		elemento = item;
		previo = p;
		siguiente = n;
	}
	
	/**
	 * Constructor de un nodo doblemente enlazado que almacena un elemento y no cuenta referencia a otros nodos.
	 * @param item elemento de tipo generico.
	 */
	public DNodo(E item) {
		this(item, null, null);
	}
	
	/**
	 * Establece el elemento.
	 * @param e elemento de tipo generico.
	 */
	public void setElement(E e) {
		elemento = e;
	}
	
	/**
	 * Establece el nodo previo.
	 * @param d Nodo previo.
	 */
	public void setPrev(DNodo<E> d) {
		previo = d;
	}
	
	/**
	 * Establece el nodo siguiente.
	 * @param d Nodo siguiente.
	 */
	public void setNext(DNodo<E> d) {
		siguiente = d;
	}
	
	/**
	 * Devuelve el elemento del nodo.
	 * @return elemento de tipo generico.
	 */
	public E element() {
		return elemento;
	}
	
	/**
	 * Devuelve la referencia al nodo previo.
	 * @return Nodo doblemente enlazado previo al nodo.
	 */
	public DNodo<E> getPrev() {
		return previo;
	}
	
	/**
	 * Devuelve la referencia al nodo siguiente.
	 * @return Nodo doblemente enlazado siguiente al nodo.
	 */
	public DNodo<E> getNext() {
		return siguiente;
	}
}
