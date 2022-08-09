package TDAPila;

import Excepciones.EmptyStackException;

/**
 * Clase pila de elementos que implementa la interfaz Stack.
 * Una pila (stack o pushdown en inglés) es una lista de elementos de la cual sólo se puede extraer el último elemento insertado.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> Tipo generico de los elementos a apilar.
 */
public class PilaSimplementeEnlazada <E> implements Stack <E>{
	//Atributos
	protected Nodo<E> head;
	protected int tamaño;
	
	/**
	 * Constructor de un objeto de tipo pila que se encuentra vacia.
	 */
	public PilaSimplementeEnlazada() {
		head = null;
		tamaño = 0;
	}
	
	public int size() {
		return tamaño;
	}
	
	public boolean isEmpty() {
		return tamaño == 0;
	}

	public E top() throws EmptyStackException{
		if (tamaño == 0) {
			throw new EmptyStackException("Pila vacia");
		} else {
			return head.element();
		}
	}

	public void push(E element) {
		head = new Nodo<E>(element, head);
		tamaño++;
	}

	public E pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("La pila esta vacia");
		}
		E aux = head.element();
		head = head.getSiguiente();
		tamaño--;
		return aux;
	}	
}
