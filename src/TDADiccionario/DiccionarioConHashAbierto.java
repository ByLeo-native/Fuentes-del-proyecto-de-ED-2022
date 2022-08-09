package TDADiccionario;

import java.util.Iterator;

import Excepciones.InvalidEntryException;
import Excepciones.InvalidKeyException;
import Excepciones.InvalidPositionException;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;

/**
 * Clase DiccionarioConHashAbierto que implementa Dictionary
 * Un diccionario (tabla, aplicación, mapping, array asociativo, almacenamiento asociativo) es una colección de pares clave/valor.
 * @author Leonardo Paillamilla, UNS.
 * @param <K> tipo generico de las claves del diccionario.
 * @param <V> tipo generico de los valores del diccionario.
 */
public class DiccionarioConHashAbierto <K,V> implements Dictionary <K,V>{
	//Atributos
	private int tamaño;
	protected PositionList<Entry<K,V>> [] arregloOfBuckets;
	protected int N;
	protected static final float factor= 0.9f;
	
	/**
	 * Constructor de un diccionario que utiliza un hash abierto con 223 buckets o cubetas
	 */
	@SuppressWarnings("unchecked")
	public DiccionarioConHashAbierto () {
		N = 223;
		this.arregloOfBuckets = (PositionList<Entry<K,V>>[]) new ListaDoblementeEnlazada[N];
		for( int i = 0; i < N; i++) {
			arregloOfBuckets[i] = new ListaDoblementeEnlazada<Entry<K,V>>();
		}
		tamaño = 0;
	}

	public int size() {
		return tamaño;
	}

	public boolean isEmpty() {
		return this.tamaño==0;
	}
	
	public Entry<K,V> find(K key) throws InvalidKeyException {
		int hashCode = this.hashCode(key);
		
		Entry<K,V> entradaBuscada = null;
		boolean seEncontro = false;
		Iterator<Entry<K,V>> it = this.arregloOfBuckets[hashCode].iterator();
		while(it.hasNext() && !seEncontro) {
			Entry<K,V> entradaActual = it.next();
			if(entradaActual.getKey().equals(key)) {
				seEncontro = true;
				entradaBuscada = entradaActual;
			}
		}
		
		return entradaBuscada;
	}

	public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
		int hashCode = this.hashCode(key);
		PositionList<Entry<K,V>> list = new ListaDoblementeEnlazada<Entry<K,V>>();
		
		for(Entry<K,V> entrada : this.arregloOfBuckets[hashCode]) {
			if(entrada.getKey().equals(key)) {
				list.addLast(entrada);
			}
		}
		
		return list;
	}

	public Entry<K,V> insert(K key, V value) throws InvalidKeyException {
		int hashCode = this.hashCode(key);
		
		Entrada<K,V> nuevaEntrada = new Entrada<K,V>( key, value);
		this.arregloOfBuckets[hashCode].addLast(nuevaEntrada);
		this.tamaño++;
		if(!(tamaño/N < factor)) {
			reHash();
		}
		return nuevaEntrada;
	}

	public Entry<K,V> remove (Entry<K,V> e) throws InvalidEntryException {
		Entrada<K,V> entrada = this.checkEntry(e);
		try {
			int hashCode = this.hashCode(entrada.getKey());
			PositionList<Entry<K,V>> list = this.arregloOfBuckets[hashCode];
			
			Position<Entry<K,V>> posicionDeLaEntradaAEliminar = null;
			Iterator<Position<Entry<K,V>>> itDePosiciones = this.arregloOfBuckets[hashCode].positions().iterator();
			boolean seEncontro = false;
			//Buscar la posicion con la entrada buscada
			while(itDePosiciones.hasNext() && !seEncontro) {
				Position<Entry<K,V>> posActual = itDePosiciones.next();
				if(posActual.element().equals(entrada)) {
					seEncontro = true;
					posicionDeLaEntradaAEliminar = posActual;
				}
			}
			
			if(!seEncontro) {
				throw new InvalidEntryException("Entrada invalida");
			} else {
				this.tamaño--;
				//Remueve a la posicion y se retorna el elemento que sera la entrada
				e = list.remove(posicionDeLaEntradaAEliminar);
			}
		} catch (InvalidPositionException | InvalidKeyException e1) {
			e1.fillInStackTrace();
		}
		
		return entrada;
	}

	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> it = new ListaDoblementeEnlazada<Entry<K,V>>();
		for( int i = 0; i < N; i++ ) {
			for(Entry<K,V> entrada : this.arregloOfBuckets[i] ) {
				it.addLast(entrada);
			}
		}
		return it;
	}

	/**
	 * Devuelve el indice del bucket que le corresponde a cierta clave.
	 * @param key clave de un entrada.
	 * @return entero que corresponde al indice del bucket de la clave pasada por parametro.
	 * @throws InvalidKeyException si la clave es invalida.
	 */
	private int hashCode(K key) throws InvalidKeyException {
		if (key == null) {
			throw new InvalidKeyException("Clave invalida");
		} else {
			return key.hashCode() % N;
		}
	}
	
	/**
	 * Verifica si una entrada es valida y devuelve un casteo a una entrada del parametro.
	 * @param e Entrada a verificar
	 * @return Objeto de la entrada.
	 * @throws InvalidEntryException si la entrada es invalida.
	 */
	private Entrada<K,V> checkEntry(Entry<K,V> e) throws InvalidEntryException {
		if( e == null) {
			throw new InvalidEntryException("Entrada invalida");
		} else {
			try {
				Entrada<K,V> entrada = (Entrada<K,V>) e;
				return entrada;
			} catch ( ClassCastException error ) {
				throw new InvalidEntryException("Entrada invalida");
			}
		}
	}

	/**
	 * Devuelve el siguiente primo de un numero.
	 * @param m numero entero.
	 * @return el numero primo siguiente al numero pasado por parametro.
	 */
	private int nextPrimo(int m) {
		boolean esPrimo = false;
		int puedeSerPrimo = m;
		while(!esPrimo) {
			int i=2;
			puedeSerPrimo++;
			while( i <= Math.sqrt(puedeSerPrimo)) {
				esPrimo= puedeSerPrimo%i!=0;
				i++;
			}
		}
		int sigPrimo = puedeSerPrimo;
		return 	sigPrimo;
	}
	
	/**
	 * Redimensiona el arreglo de buckets a un nuevo tamaño y reubica las entradas almacenadas.
	 */
	@SuppressWarnings("unchecked")
	private void reHash() {
		int tamañoAntesDelReHash = N;
		N = this.nextPrimo(N*2);
		PositionList <Entry<K,V>> [] nuevoArreglo = (PositionList<Entry<K,V>>[]) new PositionList[N];
		//Añado las lista vacias al nuevo arreglo
		for(int i = 0; i < N; i++) {
			nuevoArreglo[i] = new ListaDoblementeEnlazada<Entry<K,V>>();
		}
		//Por cada buckets del arreglo actual,
		for( int i = 0; i < tamañoAntesDelReHash; i++) {
			//Por cada entrada del bucket actual
			for( Entry<K,V> entrada : this.arregloOfBuckets[i]) {
				int nuevoHashCode = entrada.getKey().hashCode() % N; //obtengo un nuevo hashCode con el nuevo tamaño del arreglo
				nuevoArreglo[nuevoHashCode].addLast(entrada); //Añado la entrada en su nuevo correspondiente bucket en el nuevo arreglo
			}
		}
		this.arregloOfBuckets = nuevoArreglo; //Reasigno el arreglo del objeto al nuevo arreglo
	}
}
