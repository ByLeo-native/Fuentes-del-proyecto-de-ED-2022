package TDAArbol;

//Funciona
import java.util.Iterator;

import Excepciones.BoundaryViolationException;
import Excepciones.EmptyListException;
import Excepciones.EmptyTreeException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;

/**
 * Clase ArbolGeneral que implementa Tree.
 * @author Leonardo Paillamilla, UNS.
 * @param <E> tipo generico de los elementos en el árbol.
 */
public class ArbolGeneral <E> implements Tree <E> {
	//Atributos 
	private int tamaño;
	private TNodo<E> raiz;
	
	/**
	 * Constructor de un arbol general vacio.
	 */
	public ArbolGeneral() {
		this.raiz = null;
		tamaño = 0;
	}
	
	public int size() {
		return this.tamaño;
	}
	
	public boolean isEmpty() {
		return this.tamaño == 0;
	}
	
	public Iterator<E> iterator() {
		PositionList<E> list = new ListaDoblementeEnlazada<E>();
		if( !this.isEmpty() ) {
			this.recPreordenPorElementos(raiz, list);
		}
		return list.iterator();
	}
	
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> list = new ListaDoblementeEnlazada<Position<E>>();
		if( !this.isEmpty() ) {
			this.recPreorden(raiz, list);
		}
		return list;
	}
	
	public E replace (Position<E> v, E e) throws InvalidPositionException {
		if( this.raiz == null) {
			throw new InvalidPositionException("El arbol esta vacio");
		}
		
		TNodo<E> nodo = this.checkPosition(v);
		E elementToReturn = nodo.element();
		nodo.setElement(e);
		return elementToReturn;
	}
	
	public Position<E> root() throws EmptyTreeException {
		if( this.raiz == null ) {
			throw new EmptyTreeException("Arbol vacio");
		} else {
			return raiz;
		}
	}

	public Position<E> parent (Position<E> v) throws InvalidPositionException, BoundaryViolationException {
		TNodo<E> nodo = this.checkPosition(v);
		
		if ( this.raiz == nodo ) {
			throw new BoundaryViolationException("La raiz no tiene ancestro");
		} else {
			return nodo.getPadre();
		}
	}

	public Iterable<Position<E>> children(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		PositionList<TNodo<E>> descentans = nodo.getHijos();
		PositionList<Position<E>> list = new ListaDoblementeEnlazada<Position<E>>();
		for (TNodo<E> tnodo : descentans ) {
			list.addLast(tnodo);
		}
		return list;
	}

	public boolean isInternal(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		return !nodo.getHijos().isEmpty();
	}

	public boolean isExternal(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		return nodo.getHijos().isEmpty();
	}

	public boolean isRoot(Position<E> v) throws InvalidPositionException {
		try {
			TNodo<E> tNodo = checkPosition(v);
			return tNodo.equals(raiz);
		} catch ( InvalidPositionException e) {
			throw new InvalidPositionException(e.getMessage());
		}
	}

	public void createRoot(E e) throws InvalidOperationException {
		if ( this.raiz != null) {
			throw new InvalidOperationException("Operacion invalida");
		} else {
			this.raiz = new TNodo<E>( e );
			this.tamaño++;
		}
	}
	
	public Position<E> addFirstChild(Position<E> p, E e) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		TNodo<E> ancester = this.checkPosition(p);
		TNodo<E> nuevo = new TNodo<E>( e, ancester);
		ancester.getHijos().addFirst(nuevo);
		this.tamaño++;
		return nuevo;
	}

	public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		TNodo<E> ancester = this.checkPosition(p);
		TNodo<E> nuevo = new TNodo<E>( e, ancester);
		ancester.getHijos().addLast(nuevo);
		this.tamaño++;
		return nuevo;
	}

	public Position<E> addBefore(Position<E> p, Position<E> rb, E e) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		TNodo<E> ancester = this.checkPosition(p);		
		TNodo<E> hermanoDerecho = this.checkPosition(rb);
		
		if( hermanoDerecho.getPadre() != ancester ) {
			throw new InvalidPositionException("rb no es hijo de p");
		} else {
			TNodo<E> nuevoNodo = new TNodo<E>(e, ancester);
			PositionList<TNodo<E>> listaDeHijos = ancester.getHijos();
			boolean seEncontro = false;
			try {
				Position<TNodo<E>> posActual = listaDeHijos.first();
				while( posActual != null && !seEncontro ) {
					if( posActual.element() == hermanoDerecho ) {
						seEncontro = true;
					} else {
						posActual = (posActual != listaDeHijos.last()) ? listaDeHijos.next(posActual) : null;
					}
					if(!seEncontro) {
						throw new InvalidPositionException("No se encontro a rb en los hijos de p");
					} else {
						listaDeHijos.addBefore(posActual, nuevoNodo);
						this.tamaño++;
					}
				}
			} catch (EmptyListException | BoundaryViolationException e1) {e1.fillInStackTrace();}
			return nuevoNodo;
		}
	}

	public Position<E> addAfter(Position<E> p, Position<E> lb, E e) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
				
		TNodo<E> ancester = this.checkPosition(p);		
		TNodo<E> hermanoIzquierdo = this.checkPosition(lb);
				
		if( hermanoIzquierdo.getPadre() != ancester ) {
			throw new InvalidPositionException("lb no es hijo de p");
		} else {
			TNodo<E> nuevoNodo = new TNodo<E>(e, ancester);
			PositionList<TNodo<E>> listaDeHijos = ancester.getHijos();
			boolean seEncontro = false;
			try {
				Position<TNodo<E>> posActual = listaDeHijos.first();
				while( posActual != null && !seEncontro ) {
					if( posActual.element() == hermanoIzquierdo ) {
						seEncontro = true;
					} else {
						posActual = (posActual != listaDeHijos.last()) ? listaDeHijos.next(posActual) : null;
					}
					if(!seEncontro) {
						throw new InvalidPositionException("No se encontro a lb en los hijos de p");
					} else {
						listaDeHijos.addAfter(posActual, nuevoNodo);
						this.tamaño++;
					}
				}
			} catch (EmptyListException | BoundaryViolationException e1) {e1.fillInStackTrace();}
			return nuevoNodo;
		}
	}

	public void removeExternalNode(Position<E> p) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		//Lanzo excepcion en caso de que no sea un nodo externo
		if( !this.isExternal(p)) {
			throw new InvalidPositionException("El nodo no es un nodo externo");
		}
		
		TNodo<E> nodo = this.checkPosition(p);
		
		if( this.raiz == nodo ) {
			this.raiz = null;
			this.tamaño--;
		} else {
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> descentansOfAncester = ancester.getHijos();
			
			//Busco la Position<TNodo<E>> del nodo a eliminar en la lista de hijos de su padre
			boolean encontre = false;
			Position<TNodo<E>> pos = null;
			Iterable<Position<TNodo<E>>> positions = descentansOfAncester.positions();
			Iterator<Position<TNodo<E>>> it = positions.iterator();
			
			while( it.hasNext() && !encontre ) {
				pos = it.next();
				if ( pos.element() == nodo ) {
					encontre = true;
				}
			}
			
			if ( !encontre ) {
				throw new InvalidPositionException("p no aparece en la lista de hijos de su padre: ¡no elimine!");
			} else {
				descentansOfAncester.remove(pos);
				this.tamaño--;
			}
		}
	}

	public void removeInternalNode (Position<E> p) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		TNodo<E> nodo = this.checkPosition(p);
		
		if( !this.isInternal(p)) {
			throw new InvalidPositionException("No es un nodo interno");
		}
		//Si el nodo es la raiz
		if( this.raiz == nodo ) {
			//Si la raiz tiene solo un descendiente
			if( this.raiz.getHijos().size() == 1) {
				try {
					TNodo<E> nuevaRaiz = this.raiz.getHijos().remove(this.raiz.getHijos().first());
					nuevaRaiz.setPadre(null);
					this.raiz = nuevaRaiz;
					this.raiz.setPadre(null);
				} catch (EmptyListException e) {e.fillInStackTrace();}
			} else {
				throw new InvalidPositionException("La raiz no tiene un unico descendiete");
			}
		} else {
			//Si el nodo no es la raiz
			Position<TNodo<E>> posOfNodo;
			boolean encontre = false;
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> listDescentansOfAncester = ancester.getHijos();
			Iterator<Position<TNodo<E>>> it = listDescentansOfAncester.positions().iterator();
			Position<TNodo<E>> posActual = it.next();
			//Buscar en la lista de descendientes del ancestro de nodo la posicion de nodo
			while ( posActual != null && !encontre ) {
				if( posActual.element() == nodo ) {
					encontre = true;
				} else {
					posActual = it.hasNext() ? it.next() : null;
				}
			}
			
			if( !encontre ) {
				throw new InvalidPositionException("No encontre al descendiente en el nodo ancestro.");
			} else {
				//Teniendo la posicion de nodo en la lista de descendientes del ancestro de nodo
				posOfNodo = posActual;
				//Paso los descendiente del nodo a eliminar al al ancestro del nodo a eliminar
				for(TNodo<E> tNodo : nodo.getHijos()) {
					tNodo.setPadre(ancester);
					listDescentansOfAncester.addBefore(posOfNodo, tNodo);
				}
				
				//Reseteo los valores del nodo a eliminar
				nodo.setElement(null);
				nodo.setPadre(null);
				//Vacio la lista de descendientes del nodo a eliminar
				while(!nodo.getHijos().isEmpty()) {
					try {
						nodo.getHijos().remove( nodo.getHijos().first() );
					} catch (InvalidPositionException | EmptyListException e) {e.printStackTrace();
					}
				}
				listDescentansOfAncester.remove(posOfNodo);
			}
		}
		this.tamaño--;
	}
	
	public void removeNode (Position<E> p) throws InvalidPositionException {
		// Si no hay raiz, lanza una excepción.
		this.siElArbolEstaVacioLanzaInvalidPositionException();
		
		TNodo<E> nodo = this.checkPosition(p);
		//Si el nodo a eliminar es la raiz, ...
		if( this.raiz == nodo ) {
			//... entonces si la raiz no tiene hijos, ...
			if( this.raiz.getHijos().isEmpty()) {
				//... entonces borro la raiz
				this.raiz = null; 
			} else if( this.raiz.getHijos().size() == 1) {// ... de lo contrario, si la raiz tiene un hijo, ...
				try {//... entonces hacer que el hijo pase a ser la raiz del arbol
					TNodo<E> nuevaRaiz = this.raiz.getHijos().remove(this.raiz.getHijos().first());
					nuevaRaiz.setPadre(null);
					this.raiz = nuevaRaiz;
				} catch (EmptyListException e) {e.fillInStackTrace();}
				
			} else {//... de lo contrario, lanza una excepcion
				throw new InvalidPositionException("La raiz no tiene un unico descendiente");
			}
		} else {
			//... de lo contrario
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> listDescentansOfAncester = ancester.getHijos();
			Position<TNodo<E>> posOfNodo = null;
			boolean encontre = false;
			Iterator<Position<TNodo<E>>> it = listDescentansOfAncester.positions().iterator();
			
			while( it.hasNext() && !encontre) {
				Position<TNodo<E>> posActual = it.next();
				if( posActual.element() == nodo ) {
					encontre = true;
					posOfNodo = posActual;
				}
			}
			
			if( !encontre ) {
				throw new InvalidPositionException("No se encontro al descendiente en la lista del ancestro.");
			} else {
				if(this.isExternal(p)) { //Si es un nodo externo
					listDescentansOfAncester.remove(posOfNodo);
				} else {//Si es un nodo interno
					//Vacio la lista de descendientes del nodo a eliminar
					while(!nodo.getHijos().isEmpty()) {
						try {
							TNodo<E> hijo = nodo.getHijos().remove( nodo.getHijos().first() );
							hijo.setPadre(ancester);
							//Paso los descendiente del nodo a eliminar al ancestro del nodo a eliminar
							listDescentansOfAncester.addBefore(posOfNodo, hijo);
						} catch (InvalidPositionException | EmptyListException e) {e.printStackTrace();}
					}
					
					listDescentansOfAncester.remove(posOfNodo);
					//Reseteo los valores del nodo a eliminar
					nodo.setElement(null);
					nodo.setPadre(null);
				}
			}
		}
		this.tamaño--;
	}
	
	/**
	 * Valida si una posicion es valida para un arbol y retorna casteo de la posicion en caso que verifique.
	 * @param v Posicion a verificar.
	 * @return Casteo de la posicion verificada a un nodo
	 * @throws InvalidPositionException si la posicion es nula o no es valida
	 */
	private TNodo<E> checkPosition(Position<E> v) throws InvalidPositionException {
		if( v == null ) {
			throw new InvalidPositionException("Posicion invalida (1)");
		} else {
			try {
				TNodo<E> tNodo = (TNodo<E>) v;
				return tNodo;
			} catch (ClassCastException e) {
				throw new InvalidPositionException("Posicion invalida (2)");
			}
		}
	}
	
	/**
	 * Realiza un recorrido de pre-orden y agrega la posicion de cada elemento del árbol en una lista.
	 * @param v Nodo desde que se comienza a recorrer.
	 * @param list lista a agregar la posicion de los elementos.
	 */
	private void recPreorden( TNodo<E> v, PositionList<Position<E>> list) {
		list.addLast(v);
		for( TNodo<E> h : v.getHijos() ) {
			recPreorden( h, list );
		}
	}
	
	/**
	 * Realiza un recorrido de pre-orden y agrega cada elemento del arbol en una lista.
	 * @param v Nodo desde que se comienza a recorrer.
	 * @param list lista a agregar elementos.
	 */
	private void recPreordenPorElementos( TNodo<E> v, PositionList<E> list) {
		list.addLast(v.element());
		for( TNodo<E> h : v.getHijos() ) {
			recPreordenPorElementos( h, list);
		}
	}
	
	/**
	 * Verifica si el arbol esta vacio y en ese caso se lanza una excepción.
	 * @throws InvalidPositionException si el arbol esta vacio.
	 */
	private void siElArbolEstaVacioLanzaInvalidPositionException() throws InvalidPositionException {
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
	}
}
