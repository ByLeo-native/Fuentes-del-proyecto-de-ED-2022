package Programa;

import java.util.Iterator;

import Excepciones.BoundaryViolationException;
import Excepciones.EmptyQueueException;
import Excepciones.EmptyStackException;
import Excepciones.EmptyTreeException;
import Excepciones.FullQueueException;
import Excepciones.InvalidKeyException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDAArbol.ArbolGeneral;
import TDAArbol.Tree;
import TDACola.Queue;
import TDACola.QueueCircular;
import TDADiccionario.DiccionarioConHashAbierto;
import TDADiccionario.Dictionary;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;
import TDAPila.PilaSimplementeEnlazada;
import TDAPila.Stack;

/**
 * Clase Programa
 * @author Leonardo Paillamilla, UNS.
 */
public class Programa {
	//Atributos
	private boolean seCreoRaiz;
	private Tree<Pair<Character, Integer>> arbolGeneral;
	
	/**
	 * Constructor para la logica de la interfaz grafica.
	 */
	public Programa () {
		this.seCreoRaiz = false;
		this.arbolGeneral = new ArbolGeneral<Pair<Character,Integer>>();
	}
	
	/**
	 * Se crea la raiz del árbol con un rotulo y sin hijos.
	 * @param rotuloDeLaRaiz Character del rotulo de la raiz.
	 * @return verdadero si se creo la raiz.
	 * @throws InvalidOperationException si el árbol ya contaba con una raiz.
	 */
	public boolean crearRaiz(Character rotuloDeLaRaiz)  throws InvalidOperationException {
		boolean seEjecutoCompleto = false;
		if(seCreoRaiz) {
			throw new InvalidOperationException("Ya se creo la raíz.");
		} else {
			try {
			Pair<Character, Integer> par = new Par<Character,Integer>( rotuloDeLaRaiz, 0 );
			this.arbolGeneral.createRoot(par);	
			this.seCreoRaiz = true;
			seEjecutoCompleto = true;
			} catch (InvalidOperationException e) {
				throw new InvalidOperationException(e.getMessage());
			}
			return seEjecutoCompleto ;
		}
	}
	
	/**
	 * Agrega un nodo a un nodo que sera el nodo padre y devuelve el grado actualizado del nodo padre.
	 * @param rotuloDeNuevoNodo Character del rotulo del nodo a agregar.
	 * @param rotuloDelNodoAncestro Character del rotulo del nodo padre del nodo a agregar.
	 * @return entero del grado del nodo ancestro luego de agregar el nuevo nodo.
	 * @throws InvalidPositionException si no se encuentra la posicion del nodo con rotulo del que seria el nodo padre.
	 * @throws GInvalidOperationException si aun no se creo el árbol y el árbol no tiene raiz.
	 */
	public int agregarNodo( Character rotuloDeNuevoNodo, Character rotuloDelNodoAncestro) throws InvalidPositionException {
		
		Position<Pair<Character,Integer>> posDelNodoAncestro = this.buscarPosicionEnElArbol(rotuloDelNodoAncestro);
		int nuevoGradoDelNodoAncestro = posDelNodoAncestro.element().getValue() + 1;
		Pair<Character,Integer> nuevoPar = new Par<Character,Integer>(rotuloDeNuevoNodo,0);
		this.arbolGeneral.addLastChild(posDelNodoAncestro, nuevoPar);
		posDelNodoAncestro.element().setValue(nuevoGradoDelNodoAncestro);
		
		return nuevoGradoDelNodoAncestro;
	}
	
	/**
	 * Elimina el nodo con el rotulo que pasa por parametro
	 * @param rotuloDelNodo Character del rotulo del nodo.
	 * @throws InvalidPositionException si el rotulo pasado por parametro es invalido.
	 */
	public void eliminarNodo( Character rotuloDelNodo) throws InvalidPositionException {
		try {
			Position<Pair<Character,Integer>> posicionParAEliminar = this.buscarPosicionEnElArbol(rotuloDelNodo);
			
			if(!this.arbolGeneral.isRoot(posicionParAEliminar)) {
				Position<Pair<Character,Integer>> posicionParDelAncestro = this.arbolGeneral.parent(posicionParAEliminar);
				
				int gradoDelNodoAEliminar = posicionParAEliminar.element().getValue();
				int gradoDelNodoPadreDelNodoAEliminar = posicionParDelAncestro.element().getValue();
				//El padre del nodo a eliminar tendra la cantidad de hijos que tiene + la cantidad de hijos que tiene el nodo a eliminar - 1 (por el nodo eliminado)
				int nuevoGradoDelPadreDelNodoAEliminar = gradoDelNodoPadreDelNodoAEliminar + gradoDelNodoAEliminar - 1;
				//Actualizo al nodo padre del nodo eliminado con la cantidad correcta de hijos
				posicionParDelAncestro.element().setValue(nuevoGradoDelPadreDelNodoAEliminar);
			}
			//Elimino al nodo a eliminar
			this.arbolGeneral.removeNode(posicionParAEliminar);
			
			if(this.arbolGeneral.isEmpty()) {
				this.condicionesIniciales();
			}
		} catch (BoundaryViolationException e) {
			throw new InvalidPositionException(e.getMessage());
		}
	}
	
	/**
	 * Devuelve una cadena de texto donde se muestran todos las Pars clasificadas segun su correspondiente grado.
	 * @return una cadena de texto con todos las Pars clasificadas segun su correspondiente grado.
	 */
	public String obtenerGrados() {
		String textoCompleto = "";
		int gradoDelArbol = 0;
		
		Dictionary<Integer, Character> diccionario = new DiccionarioConHashAbierto<Integer,Character>();
		Iterator<Pair<Character,Integer>> it = this.arbolGeneral.iterator();
		
		//Cada Par del arbol, las inserto al diccionario donde la clave es el grado de la Par y el valor el rotulo de la Par. Ademas se registra el grado del arbol.
		while(it.hasNext()) {
			Pair<Character,Integer> par = it.next();
			try {
				diccionario.insert(par.getValue(), par.getKey());
			} catch (InvalidKeyException e) {e.fillInStackTrace();}
			
			if( gradoDelArbol < par.getValue()) {
				gradoDelArbol = par.getValue();
			}
		}
		
		//Desde el grado 0 hasta el grado del arbol, se iran añadiendo los rotulo segun la cantidad de grados que tenga.
		for( int i = 0; i <= gradoDelArbol; i++) {
			
			String textoPorGrado = "Nodos de grado "+i+":";
			String textoDeLosRotulos = "";
			try {
				for(Pair<Integer,Character> par : diccionario.findAll(i)) {
					textoDeLosRotulos += " "+par.getValue()+",";
				}
			} catch (InvalidKeyException e) {e.printStackTrace();}
			if(textoDeLosRotulos.length() != 0) {
				textoPorGrado += textoDeLosRotulos;
				textoPorGrado = textoPorGrado.substring(0, textoPorGrado.length() -1)+".";
				textoCompleto += textoPorGrado+"\n";
			}
		}
		return textoCompleto;
	}
	
	/**
	 * Devuelve el grado del árbol.
	 * @return numero entero correspondiente al grado del árbol.
	 */
	public int obtenerGradoDelArbol() {
		int maximoGradoEncontradoActualmente = 0;
		
		Iterator<Pair<Character,Integer>> it = this.arbolGeneral.iterator();
		
		while(it.hasNext()) {
			int gradoDelNodoActual = it.next().getValue();
			if( maximoGradoEncontradoActualmente < gradoDelNodoActual ) {
				maximoGradoEncontradoActualmente = gradoDelNodoActual;
			}
		}
		
		return maximoGradoEncontradoActualmente;
	}
	
	/**
	 * Devuelve el camino desde la raiz del arbol hasta el nodo con el rotulo pasado por parametro.
	 * @param rotulo Character con el rotulo a buscar camino.
	 * @return Cadena de texto con el camino desde la raiz del arbol hasta el nodo con rotulo.
	 * @throws InvalidPositionException si no se encuentra una par con el rotulo pasado por parametro.
	 */
	public String obtenerCamino(Character rotulo) throws InvalidPositionException {
		String camino = "";
		
		Stack<Pair<Character,Integer>> pila = new PilaSimplementeEnlazada<Pair<Character,Integer>>();
		
		Position<Pair<Character,Integer>> posicionActual = this.buscarPosicionEnElArbol(rotulo);
		
		//Mientras la posicion actual no sea la posicion de la raiz
		while(!this.arbolGeneral.isRoot(posicionActual)) {
			pila.push(posicionActual.element());
			//Obtengo el padre de la posicion actual
			try {
				posicionActual = this.arbolGeneral.parent(posicionActual);
			} catch (InvalidPositionException | BoundaryViolationException e) {}
		}
		//Ya siendo la posicion actual el nodo raiz
		pila.push(posicionActual.element());//Pusheo la raiz
		//Mientras la pila no este vacia
		while(!pila.isEmpty()) {
			try {
				Pair<Character, Integer> par = pila.pop(); //Desapilo una par
				camino += "("+par.getKey()+", "+par.getValue()+")\n"; //Agrego la par en la ruta de camino
			} catch (EmptyStackException e) {e.fillInStackTrace();}
		}
		return camino; //Retorno el camino
	}

	/**
	 * Devuelve una cadena de texto con los rotulos de los nodos del arbol con un recorrido de preorden.
	 * @return cadena de texto con los rotulos de los nodos del arbol con un recorrido de preorden.
	 */
	public String mostrarRecorridoPreorden() {
		String recorrido = "";
		try {
			recorrido = this.PreOrden(this.arbolGeneral.root());
		} catch (EmptyTreeException e) {e.fillInStackTrace();}
		return recorrido;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de los nodos del arbol con un recorrido de postorden.
	 * @return cadena de texto con los rotulos de los nodos del arbol con un recorrido de postorden.
	 */
	public String mostrarRecorridoPosorden() {
		String recorrido = "";
		try {
			recorrido += this.PosOrden(this.arbolGeneral.root());
			
		} catch (EmptyTreeException e) {e.fillInStackTrace();}
		return recorrido;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de los nodos del arbol donde en cada linea se mostraran los rotulos con el mismo nivel (altura).
	 * @return cadena de texto con los rotulos por niveles.
	 */
	public String mostrarPorNiveles() {
		String texto = "";
		Queue<Position<Pair<Character,Integer>>> cola = new QueueCircular<Position<Pair<Character,Integer>>>();
		try {
			cola.enqueue(this.arbolGeneral.root());
			cola.enqueue(null);
			while (!cola.isEmpty()) {
				Position<Pair<Character,Integer>> v = cola.dequeue();
				if (v != null) {
					texto += "-"+v.element().getKey()+"-";
					for (Position<Pair<Character,Integer>> w : this.arbolGeneral.children(v))
						cola.enqueue(w);
				} else {
					texto += "\n";
					if (!cola.isEmpty())
						cola.enqueue(null);
				}
			}
		} catch (EmptyQueueException | EmptyTreeException | InvalidPositionException | FullQueueException e) {e.fillInStackTrace();}
		return texto;
	}
	
	/**
	 * Elimina todos los nodos con un grado especifico y devuelve una cadena de texto con los rotulos de los nodos eliminados.
	 * @param k Grado de los nodos a eliminar.
	 * @return String con los rotulos de los nodos eliminados.
	 */
	public String eliminarNodosGradoK(int k) {
		String textoDeLosRotulos = "";
		
		Iterator<Pair<Character,Integer>> it = this.arbolGeneral.iterator();
		PositionList<Pair<Character,Integer>> list = new ListaDoblementeEnlazada<Pair<Character,Integer>>();
		
		while(it.hasNext()) {
			Pair<Character, Integer> par = it.next();
			if(par.getValue().equals(k) ) {
				list.addLast(par);
			}
		}
		
		for(Pair<Character,Integer> par : list ) {
			textoDeLosRotulos += " "+par.getKey()+",";
			try {
				this.eliminarNodo(par.getKey());
			} catch (InvalidPositionException e) {e.printStackTrace();}
		}
		
		if(textoDeLosRotulos.length() != 0) {
			textoDeLosRotulos = textoDeLosRotulos.substring(0, textoDeLosRotulos.length() -1)+".";
		} 
		
		return textoDeLosRotulos;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de forma preOrden.
	 * @param v posicion desde que se empieza a recorrer de forma preorden.
	 * @return cadena de texto con los rotulos de manera preorden desde la posicion de la par pasada por parametro.
	 */
	private String PreOrden(Position<Pair<Character,Integer>> v) {
		String recorrido = ""+v.element().getKey();
		try {
			for (Position<Pair<Character, Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += "-"+PreOrden(hijo);
			}
		} catch (InvalidPositionException e) {e.fillInStackTrace();}
		return recorrido;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de forma posOrden.
	 * @param v posicion desde que se empieza a recorrer de forma posorden.
	 * @return cadena de texto con los rotulos de manera posorden desde la posicion de la par pasada por parametro.
	 */
	private String PosOrden( Position<Pair<Character,Integer>> v) {
		String recorrido = "";
		try {
			for (Position<Pair<Character,Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += this.PosOrden(hijo);
			}

			recorrido += v.element().getKey();
			
			if(!this.arbolGeneral.isRoot(v)) {
				recorrido += "-";
			}
			
		} catch (InvalidPositionException e) {e.fillInStackTrace();}
		return recorrido;
	}
	
	public Tree<Pair<Character,Integer>> referenciaDelArbol() {
		return this.arbolGeneral;
	}
	
	/**
	 * Busca la posición de un nodo con el rotulo pasado y lo retorna.
	 * @param rotuloDelNodo rotulo del nodo a buscar.
	 * @return Posición del nodo del arbol con el rotulo pasado por parametro.
	 * @throws InvalidPositionException si no se encuentra el nodo en el árbol.
	 */
	private Position<Pair<Character,Integer>> buscarPosicionEnElArbol(Character rotuloDelNodo) throws InvalidPositionException {
		Position<Pair<Character,Integer>> posicionBuscada = null;
		
		boolean seEncontro = false;
		Iterator<Position<Pair<Character, Integer>>> it = this.arbolGeneral.positions().iterator();
		
		while(it.hasNext() && !seEncontro) {
			Position<Pair<Character, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotuloDelNodo)) {
				seEncontro = true;
				posicionBuscada = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se ha encontrado el rotulo "+rotuloDelNodo+" en el árbol.");
		}
		
		return posicionBuscada;
	}
	
	/**
	 * Establece los parametros a las condiciones iniciales.
	 */
	public void condicionesIniciales() {
		this.seCreoRaiz = false;
	}
}

	

