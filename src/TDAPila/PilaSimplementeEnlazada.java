package TDAPila;

import Excepciones.EmptyStackException;

/**
 * Clase pila de elementos que implementa la interfaz Stack.
 * Una pila (stack o pushdown en ingl�s) es una lista de elementos de la cual s�lo se puede extraer el �ltimo elemento insertado.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> Tipo generico de los elementos a apilar.
 */
public class PilaSimplementeEnlazada <E> implements Stack <E>{
	//Atributos
	protected Nodo<E> head;
	protected int tama�o;
	
	/**
	 * Constructor de un objeto de tipo pila que se encuentra vacia.
	 */
	public PilaSimplementeEnlazada() {
		head = null;
		tama�o = 0;
	}
	
	public int size() {
		return tama�o;
	}
	
	public boolean isEmpty() {
		return tama�o == 0;
	}

	public E top() throws EmptyStackException{
		if (tama�o == 0) {
			throw new EmptyStackException("Pila vacia");
		} else {
			return head.element();
		}
	}

	public void push(E element) {
		head = new Nodo<E>(element, head);
		tama�o++;
	}

	public E pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("La pila esta vacia");
		}
		E aux = head.element();
		head = head.getSiguiente();
		tama�o--;
		return aux;
	}	
}
