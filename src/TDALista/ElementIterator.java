 package TDALista;

import java.util.NoSuchElementException;

import Excepciones.*;

/**
 * Clase de una elemento iterador que implementa la interface iterator provista por java.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico de los elementos del iterador.
 */
public class ElementIterator <E> implements java.util.Iterator<E> {
	//Atributos
	protected PositionList<E> list;
	protected Position<E> cursor;
	
	/**
	 * Constructor del iterador de elementos.
	 * @param list lista de posiciones.
	 * @throws EmptyListException si la lista esta vácia.
	 */
	public ElementIterator(PositionList<E> list) throws EmptyListException {
		this.list = list;
		if (list.isEmpty()) {
			cursor = null;
		}  else {
			try {
				cursor = list.first();
			} catch (EmptyListException e) {
				throw new EmptyListException(e.getMessage());
			}
		}
	}
	
	/**
	 * Hay siguiente si el cursor no esta mas alla de la ultima posición.
	 * @return Verdadero si el cursor no se encuentra en el ultimo elemento.
	 */
	public boolean hasNext() {
		return cursor != null;
	}
	
	/**
	 * Devuelve el elemento que apunta el cursor y pasa a su siguiente.
	 * Si no existe el siguiente elemento, entonces el cursor apunta a una referencia nula.
	 * @return elemento de tipo generico almacenado en la referencia del cursor.
	 * @throws NoSuchElementException si no existe un siguiente elemento al actual.
	 */
	public E next() throws NoSuchElementException {
		if (cursor == null) {
			throw new NoSuchElementException("Error: No hay siguiente");
		} else {
			E toReturn = cursor.element();
			try {
				cursor = (cursor == list.last()) ? null : list.next(cursor);
			} catch (EmptyListException | InvalidPositionException | BoundaryViolationException e) {
				e.printStackTrace();
			}
			return toReturn;
		}
	}
	
	public void remove() {
		//No implementacion hecha
	}
}
