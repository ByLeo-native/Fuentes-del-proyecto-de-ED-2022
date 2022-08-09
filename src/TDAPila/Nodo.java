package TDAPila;

import TDALista.Position;

/**
 * Una clase de nodo simplemente enlazado donde se usara para
 * una pila simplemente enlazada.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico que almacenara cada nodo.
 */
public class Nodo <E> implements Position<E> {
	private E elemento;
	private Nodo<E> siguiente;
	
	/**
	 * Constructor de un nodo simplemente enlazado que almacena un elemento y la referencia del siguiente nodo.
	 * @param item elemento de tipo generico.
	 * @param sig nodo siguiente al nodo actual.
	 */
	public Nodo(E item, Nodo <E> sig) {
		elemento = item;
		siguiente = sig;
	}
	
	/**
	 * Constructor de un nodo simplente enlazado que almacena un elemento y una referencia nula a al siguiente nodo.
	 * @param item elemento de tipo generico.
	 */
	public Nodo (E item) {
		this(item, null);
	}
	
	/**
	 * Establece el elemento del nodo.
	 * @param item elemento de tipo generico a remplazar.
	 */
	public void setElemento (E item) {
		elemento = item;
	}
	
	/**
	 * Establece el nodo siguiente.
	 * @param sig Nodo de tipo generico.
	 */
	public void setSiguiente ( Nodo<E> sig) {
		siguiente = sig;
	}
	
	/**
	 * Devuelve el elemento almacenado en el nodo.
	 * @return elemento almacenado en el nodo.
	 */
	public E element() {
		 return elemento;
	}
	
	/**
	 * Devuelve la referencia del nodo siguiente.
	 * @return referencia del nodo siguiente.
	 */
	public Nodo<E> getSiguiente() {
		return siguiente;
	}
}
