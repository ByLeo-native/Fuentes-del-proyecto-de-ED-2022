package TDAArbol;

import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;

/**
 * Clase TNodo que implementa Position.
 * Nodo que almacena un elemento y mantiene refencia a uno nodo padre, y una lista de nodos hijos.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico del elemento del nodo.
 */
public class TNodo <E> implements Position <E> {
	//Atributos
	private E element;
	private TNodo<E> ancestor;
	private PositionList<TNodo<E>> descendant;
	
	/**
	 * Constructor de un nodo que cuenta un elemento, una referencia a un nodo ancestro y una lista vacia de los nodos descendientes.
	 * @param element tipo generico E.
	 * @param ancestor TNodo<E> ancestro del nodo.
	 */
	public TNodo(E element, TNodo<E> ancestor) {
		this.element = element;
		this.ancestor = ancestor;
		this.descendant = new ListaDoblementeEnlazada<TNodo<E>>();
	}
	
	/**
	 * Constructor de un nodo que cuenta un elemento, un nodo ancestro con referencia nula y una lista vacia de los nodos descendientes.
	 */
	public TNodo(E element) {
		this(element, null);
	}
	
	/**
	 * Devuelve el elemento almacenado en el nodo.
	 * @return elemento de tipo generico E.
	 */
	public E element() {
		return this.element;
	}
	
	/**
	 * Devuelve la lista de descendientes directos del nodo.
	 * @return Lista de descendientes del nodo.
	 */
	public PositionList<TNodo<E>> getHijos() {
		return this.descendant;
	}
	
	/**
	 * Establece el elemento del nodo.
	 * @param element nuevo elemento del nodo.
	 */
	public void setElement(E element) {
		this.element = element;
	}
	
	/**
	 * Devuelve la referencia del nodo ancestro del nodo.
	 * @return TNodo<E> ancestro del nodo.
	 */
	public TNodo<E> getPadre() {
		return this.ancestor;
	}
	
	/**
	 * Establece la referencia del nodo ancestro.
	 * @param padre TNodo<E> nuevo ancestro del nodo.
	 */
	public void setPadre( TNodo<E> padre ) {
		this.ancestor = padre;
	}
}
