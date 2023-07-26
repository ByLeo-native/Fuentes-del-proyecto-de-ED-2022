 package TDACola;

import Excepciones.EmptyQueueException;
import Excepciones.FullQueueException;

/**
 * Clase de una cola circular.
 * Una cola es una estructura donde los elementos se insertan en un extremo (el fondo) y la supresiones tienen lugar en el otro extremo (el frente).
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico de los elementos que se encolaran en la cola.
 */
public class QueueCircular <E> implements Queue <E> {
	private int f, r;
	private E [] datos;
	
	/**
	 * Constructor de una cola circular de n componentes
	 * @param n Cantidad de componentes de la cola
	 */
	@SuppressWarnings("unchecked")
	public QueueCircular( int n) {
		datos = (E[]) new Object[n];
		f = r = 0;
	}
	
	/**
	 * Constructor de una cola circular de 1001 componentes
	 */
	public QueueCircular() {
		this(1001);
	}
	
	public int size() {
		return (datos.length - f + r)% (datos.length);
	}

	public boolean isEmpty() {
		return (f == r);
	}

	public E front() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			return datos[f];
		}
	}

	public void enqueue(E element) throws FullQueueException {
		if ( this.size() == (datos.length - 1)) {
			throw new FullQueueException("Cola llena");
		} else {
			datos[r] = element;
			r = (r+1) % datos.length;
		}
		
		if((datos.length - f + r)% (datos.length) == datos.length) {
			this.agrandarArreglo();
		}
	}

	public E dequeue() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			E temp = datos[f];
			datos[f] = null;
			f = (f+1) % datos.length;
			return temp;
		}
	}
	
	/**
	 * Incrementa el tamaño del arreglo.
	 */
	@SuppressWarnings("unchecked")
	private void agrandarArreglo() {
		E [] nuevoArreglo = (E[]) new Object [this.datos.length * 2 ];
		int i=0;
		int pos = r;
		while( i < this.size() ) {
			nuevoArreglo [i++] = this.datos[pos];
			pos = (pos+1) % datos.length;
		}
		this.datos = nuevoArreglo;
	}
}
