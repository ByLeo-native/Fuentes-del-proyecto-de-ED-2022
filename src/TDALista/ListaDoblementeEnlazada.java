package TDALista;

import java.util.Iterator;
import Excepciones.*;

/**
 * Clase de una lista doblemente enlazada que implementa la interfaz PositionList.
 * Una lista de elementos es una secuencia finita y ordenada de elementos donde pueden ser insertados, eliminados y accedidos de manera flexible y no estricta.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico de los elementos de la lista.
 */
public class ListaDoblementeEnlazada <E> implements PositionList <E> {
	//Atributos
	protected int tamaño;
	protected DNodo<E> head, tail;
	
	/**
	 * Constructor una lista vacia.
	 */
	public ListaDoblementeEnlazada() {
		this.head = new DNodo<E>(null);
		this.tail = new DNodo<E>(null);
		this.head.setPrev(null);
		this.head.setNext(tail);
		this.tail.setPrev(head);
		this.tail.setNext(null);
		this.tamaño = 0;
	}
	
	public int size() {
		return this.tamaño;
	}
	
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	public Position<E> first() throws EmptyListException {
		this.siLaListaEstaVaciaLanzaEmptyListException();
		return this.head.getNext();
	}
	
	public Position<E> last() throws EmptyListException {
		this.siLaListaEstaVaciaLanzaEmptyListException(); 
		return this.tail.getPrev();
	}

	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {	
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		if (p == this.tail.getPrev()) {
			throw new BoundaryViolationException("No existe un siguiente al ultimo elemento.");
		}
		return pos.getNext();		
	}
	
	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		if (pos == this.head.getNext()) {
			throw new BoundaryViolationException("No existe el previo al primer elemento");
		} else {
			return pos.getPrev();
		}
	}

	public void addFirst(E element) {
		DNodo<E> nuevo = new DNodo<E> (element);
		//A nuevo le establezco como siguiente al actual primer elemento de la lista
		nuevo.setNext(this.head.getNext());
		//Al nuevo primer elemento le establezco como previo a head
		nuevo.setPrev(this.head);
		//Establezco como el siguiente de head al nuevo elemento que sera el primer elemento
		this.head.setNext(nuevo);
		//Al siguiente de nuevo le establezco como previo al nuevo primer elemento
		nuevo.getNext().setPrev(nuevo);
		//Aumento el tamaño de la lista
		this.tamaño++;
	}
	
	public void addLast(E element) {
		DNodo<E> nuevo = new DNodo<E> (element);
		//A nuevo le establezco como siguiente a la tail de la lista
		nuevo.setNext(tail);
		//Al nuevo ultimo elemento le establezco como previo al previo de la tail
		nuevo.setPrev(tail.getPrev());
		//Establezco como el previo de tail al nuevo elemento que sera el ultimo elemento
		tail.setPrev(nuevo);
		//Al previo de nuevo le establezco como su siguiente al nuevo ultimo elemento
		nuevo.getPrev().setNext(nuevo);
		//Aumento el tamaño de la lista
		tamaño++;
	}

	public void addAfter(Position<E> p, E element) throws InvalidPositionException {
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		DNodo<E> nuevo = new DNodo<E>(element);
		nuevo.setNext(pos.getNext());
		nuevo.setPrev(pos);
		nuevo.getNext().setPrev(nuevo);
		pos.setNext(nuevo);
		this.tamaño++;
	}

	public void addBefore(Position<E> p, E element) throws InvalidPositionException {
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		DNodo<E> nuevo = new DNodo<E>(element);
		nuevo.setNext(pos);
		nuevo.setPrev(pos.getPrev());
		nuevo.getPrev().setNext(nuevo);
		pos.setPrev(nuevo);
		this.tamaño++;
	}

	public E remove(Position<E> p) throws InvalidPositionException {
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		E aux = pos.element();
		pos.getPrev().setNext(pos.getNext());
		pos.getNext().setPrev(pos.getPrev());
		
		pos.setElement(null);
		pos.setPrev(null);
		pos.setNext(null);
		
		this.tamaño--;
		return aux;
	
	}

	public E set(Position<E> p, E element) throws InvalidPositionException {
		this.siLaListaEstaVaciaLanzaInvalidPositionException();
		DNodo<E> pos = checkPosition(p);
		E aux = pos.element();
		pos.setElement(element);
				
		return aux;
	}
	
	public Iterator<E> iterator() {
		ElementIterator<E> it = null;
		try {
			it = new ElementIterator<E>(this);
		} catch (EmptyListException e) {System.out.println("Entro en un excepcion");}
		return it;
	}
	
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> list = new ListaDoblementeEnlazada<Position<E>>();
		if (!this.isEmpty()) {
			try {
				Position<E> pos = this.first();
				while(pos != this.last()) {
					list.addLast(pos);
					pos = this.next(pos);
				}
				//Ya cuando a la salida del while, pos sera la ultima posicion y hay que añadirla
				list.addLast(pos);
			} catch (EmptyListException | InvalidPositionException | BoundaryViolationException e) {e.printStackTrace();}
		}
		return list;
	}
	
	/**
	 * Si la lista esta vacia lanza una excepción.
	 * @throws EmptyListException si la lista esta vacia.
	 */
	private void siLaListaEstaVaciaLanzaEmptyListException() throws EmptyListException {
		if(this.isEmpty()) {
			throw new EmptyListException("Lista vacia");
		}
	}
	
	/**
	 * Si la lista esta vacia lanza una excepción.
	 * @throws InvalidPositionException si la lista esta vacia.
	 */
	private void siLaListaEstaVaciaLanzaInvalidPositionException() throws InvalidPositionException {
		if (this.isEmpty()) {
			throw new InvalidPositionException("Lista vacia.");
		}
	}

	/**
	 * Valida si una posición es válida.
	 * @param p posición a verifica.
	 * @return Nodo doble enlazado del cateo de la posicion pasada por parametro.
	 * @throws InvalidPositionException si la posicion tiene una referencia nula, es la posicion de los centinelas o es invalida.
	 */
	private DNodo<E> checkPosition(Position<E> p) throws InvalidPositionException {
		if (p == null) {
			throw new InvalidPositionException("Posicion p con referencia nula");
		} else {
			DNodo<E> aRetornar;
			try {
				aRetornar = (DNodo<E>)p;
				
				//Protejo los centinelas
				if(aRetornar == this.head || aRetornar == this.tail) {
					throw new InvalidPositionException("Posición invalida");
				}
				
			} catch ( ClassCastException e) {
				throw new InvalidPositionException("Posicion invalida");
			}
			return aRetornar;
		}
	}
}
