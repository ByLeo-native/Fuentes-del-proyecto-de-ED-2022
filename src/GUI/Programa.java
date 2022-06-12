package GUI;

import java.util.Iterator;

import Auxiliar.Queue;
import Auxiliar.QueueEnlazada;
import Excepciones.BoundaryViolationException;
import Excepciones.EmptyQueueException;
import Excepciones.EmptyStackException;
import Excepciones.EmptyTreeException;
import Excepciones.GInvalidOperationException;
import Excepciones.InvalidEntryException;
import Excepciones.InvalidKeyException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDAArbol.ArbolGeneral;
import TDAArbol.Tree;
import TDADiccionario.DiccionarioConHashAbierto;
import TDADiccionario.Dictionary;
import TDALista.Position;
import TDAMapeo.Entrada;
import TDAMapeo.Entry;
import TDAPila.PilaSimplementeEnlazada;
import TDAPila.Stack;

public class Programa {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private Tree<Entry<String, Integer>> arbolGeneral;
		
	public Programa () {
		this.seCreoArbol = false;
		this.arbolGeneral = null;
	}
	
	public boolean crearArbol() {
		this.arbolGeneral = new ArbolGeneral<Entry<String,Integer>>();
		this.seCreoArbol = true;
		return true;
	}
	
	public boolean crearRaiz(String rotuloDeLaRaiz)  throws InvalidOperationException {
		boolean seEjecutoCompleto = false;
		Entry<String, Integer> entrada = new Entrada<String,Integer>( rotuloDeLaRaiz, 0 );
		try {
			this.arbolGeneral.createRoot(entrada);	
		} catch (InvalidOperationException e) {
			throw new InvalidOperationException(e.getMessage());
		}
		
		this.seCreoRaiz = true;
		seEjecutoCompleto = true;
		
		return seEjecutoCompleto ;
	}
	
	public int agregarNodo( String rotuloDeNuevoNodo, String rotuloDelNodoAncestro) throws InvalidPositionException, GInvalidOperationException {
		int nuevoGradoDelNodoAncestro = 0;
		boolean seEncontro = false;
		if(!seCreoArbol || !seCreoRaiz) {
			throw new GInvalidOperationException("Error en ejecucion");
		} else {
			//Busco la posicion del nodo a agregar un nuevo nodo
			Iterable<Position<Entry<String, Integer>>> list = this.arbolGeneral.positions();
			Iterator<Position<Entry<String, Integer>>> it = list.iterator();
			Position<Entry<String,Integer>> pos = null;
			while(it.hasNext() && !seEncontro) {
				Position<Entry<String, Integer>> aux = it.next();
				if(aux.element().getKey().equals(rotuloDelNodoAncestro)) {
					seEncontro = true;
					pos = aux;
				}
			}
			
			if(!seEncontro) {
				throw new InvalidPositionException("No se encontro el nodo ancestro "+rotuloDelNodoAncestro+" en el árbol");
			} else {
				nuevoGradoDelNodoAncestro = pos.element().getValue() + 1;
				//Si se encontro la posicion del nodo a agregar un nuevo nodo, creo el objeto de tipo entrada que tendra el rotulo y con grado 0
				Entry<String, Integer> entradaNueva = new Entrada<String, Integer>(rotuloDeNuevoNodo, 0);
				this.arbolGeneral.addLastChild(pos, entradaNueva);
				//Actualizo el grado del nodo del ancestro
				this.arbolGeneral.replace(pos, new Entrada<String,Integer>( rotuloDelNodoAncestro, nuevoGradoDelNodoAncestro));
			}
			return nuevoGradoDelNodoAncestro;
		}
	}
	
	public boolean eliminarNodo( String rotuloDelNodo) throws InvalidPositionException {
		Position<Entry<String,Integer>> posicionEntradaDelAncestro = null;
		
		String rotuloDelNodoPadreDelNodoAEliminar = "";
		int gradoDelNodoPadreDelNodoAEliminar = 0;
		int gradoDelNodoAEliminar = 0;
	
		int nuevoGradoDelPadreDelNodoAEliminar = 0;
		
		boolean seEncontro = false;
		Iterable<Position<Entry<String, Integer>>> list = this.arbolGeneral.positions();
		Iterator<Position<Entry<String, Integer>>> it = list.iterator();
		Position<Entry<String,Integer>> pos = null;
		while(it.hasNext() && !seEncontro) {
			Position<Entry<String, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotuloDelNodo)) {
				seEncontro = true;
				pos = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se encontro el nodo ancestro ( "+rotuloDelNodo+") en el árbol");
		} else {
			//Obtengo informacion del nodo ancestro del ARBOL antes de eliminarlo
			if(!this.arbolGeneral.isRoot(pos)) {
				try {
					posicionEntradaDelAncestro  = this.arbolGeneral.parent(pos);
					//Obtengo informacion para actualizar al nodo padre del nodo a eliminar
					gradoDelNodoAEliminar = pos.element().getValue();
					rotuloDelNodoPadreDelNodoAEliminar = posicionEntradaDelAncestro.element().getKey();
					gradoDelNodoPadreDelNodoAEliminar = posicionEntradaDelAncestro.element().getValue();
					//El padre del nodo a eliminar tendra la cantidad de hijos que tiene + la cantidad de hijos que tiene el nodo a eliminar - 1 (por el nodo eliminado)
					nuevoGradoDelPadreDelNodoAEliminar = gradoDelNodoPadreDelNodoAEliminar + gradoDelNodoAEliminar - 1;
					//Actualizo al nodo padre del nodo eliminado con la cantidad correcta de hijos
					this.arbolGeneral.replace(posicionEntradaDelAncestro , new Entrada <String,Integer>( rotuloDelNodoPadreDelNodoAEliminar, nuevoGradoDelPadreDelNodoAEliminar));
				} catch (InvalidPositionException | BoundaryViolationException e) {throw new InvalidPositionException("Error");}	
			}
			
			
			//Elimino al nodo a eliminar
			this.arbolGeneral.removeNode(pos);
		}
		
		return seEncontro;
		
	}
	
	public String obtenerGrados() {
		String textoCompleto = "";
		
		int gradoDelArbol = 0;
		
		Dictionary<Integer, String> diccionario = new DiccionarioConHashAbierto<Integer,String>();
		
		Iterator<Entry<String,Integer>> it = this.arbolGeneral.iterator();
		
		while(it.hasNext()) {
			Entry<String,Integer> entrada = it.next();
			try {
				diccionario.insert(entrada.getValue(), entrada.getKey());
			} catch (InvalidKeyException e) {}
			
			if( gradoDelArbol < entrada.getValue()) {
				gradoDelArbol = entrada.getValue();
			}
		}
		
		for( int i = 0; i <= gradoDelArbol; i++) {
			
			String textoPorGrado = "Nodos de grado "+i+":";
			String textoDeLosRotulos = "";
			try {
				for(Entry<Integer,String> entrada : diccionario.findAll(i)) {
					textoDeLosRotulos += " "+entrada.getValue()+",";
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
	 * Devuelve el grado del árbol
	 * @return grado del árbol.
	 */
	public int obtenerGradoDelArbol() {
		int maximoGradoEncontradoActualmente = 0;
		
		Iterator<Entry<String,Integer>> it = this.arbolGeneral.iterator();
		
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
	 * @param rotulo String con el rotulo a buscar camino
	 * @return Cadena de texto con el camino desde la raiz del arbol hasta el nodo con rotulo.
	 * @throws InvalidPositionException si no se encuentra una entrada con el rotulo pasado por parametro.
	 */
	public String obtenerCamino(String rotulo) throws InvalidPositionException {
		String camino = "";
		
		Stack<Entry<String,Integer>> pila = new PilaSimplementeEnlazada<Entry<String,Integer>>();
		
		//Buscar la posicion comparando los rotulos de las entradas.
		
		Iterator<Position<Entry<String,Integer>>> it = this.arbolGeneral.positions().iterator();
		Position<Entry<String,Integer>> posBuscado = null;
		boolean seEncontro = false;
		//Busco la posicion de nodo con el rotulo buscado
		while( it.hasNext() && !seEncontro ) {
			Position<Entry<String, Integer>> posicionDeLaEntradaActual = it.next();
			if( posicionDeLaEntradaActual.element().getKey().equals(rotulo)) {
				seEncontro = true;
				posBuscado = posicionDeLaEntradaActual;
			}
		}
		//Si no se encontro, lanzo una excepcion
		if(!seEncontro) {
			throw new InvalidPositionException("No se ha encontrado el rotulo "+rotulo+" en el arbol.");
		} else {
			Position<Entry<String,Integer>> posActual = posBuscado; 
			//Mientras la posicion actual no sea la posicion de la raiz
			while(!this.arbolGeneral.isRoot(posActual)) {
				pila.push(posActual.element());
				//Obtengo el padre de la posicion actual
				try {
					posActual = this.arbolGeneral.parent(posActual);
				} catch (InvalidPositionException | BoundaryViolationException e) {}
			}
			//Ya siendo la posicion actual el nodo raiz
			pila.push(posActual.element());//Pusheo la raiz
			//Mientras la pila no este vacia
			while(!pila.isEmpty()) {
				try {
					Entry<String, Integer> entrada = pila.pop(); //Desapilo una entrada
					camino += "("+entrada.getKey()+", "+entrada.getValue()+")\n"; //Agrego la entrada en la ruta de camino
				} catch (EmptyStackException e) {}
			}
			return camino; //Retorno el camino
		}
	}

	public String mostrarRecorridoPreorden() {
		String recorrido = "";
		try {
			recorrido = this.PreOrden(this.arbolGeneral.root());
		} catch (EmptyTreeException e) {}
		return recorrido;
	}
	
	public String mostrarRecorridoPostorden() {
		String recorrido = "";
		try {
			recorrido += this.PosOrden(this.arbolGeneral.root());
			
		} catch (EmptyTreeException e) {}
		return recorrido;
	}
	
	public String mostrarPorNiveles() {
		String texto = "";
		Queue<Position<Entry<String,Integer>>> cola = new QueueEnlazada<Position<Entry<String,Integer>>>();
		try {
			cola.enqueue(this.arbolGeneral.root());
			cola.enqueue(null);
			while (!cola.isEmpty()) {
				Position<Entry<String,Integer>> v = cola.dequeue();
				if (v != null) {
					texto += "-"+v.element().getKey()+"-";
					for (Position<Entry<String,Integer>> w : this.arbolGeneral.children(v))
						cola.enqueue(w);
				} else {
					texto += "\n";
					if (!cola.isEmpty())
						cola.enqueue(null);
				}
			}
		} catch (EmptyQueueException | EmptyTreeException | InvalidPositionException e) {}
		return texto;

	}
	
	public boolean eliminarNodosGradoK(int k) {
		boolean seCompleto = false;
		
		return seCompleto;
	}
	
	public int obtenerTamañoDelArbol() {
		return this.arbolGeneral.size();
	}
	
	private String PreOrden(Position<Entry<String,Integer>> v) {
		String recorrido = ""+v.element().getKey();
		try {
			for (Position<Entry<String, Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += "-"+PreOrden(hijo);
			}
		} catch (InvalidPositionException e) {}
		return recorrido;
	}
	
	private String PosOrden( Position<Entry<String,Integer>> v) {
		String recorrido = "";
		try {
			for (Position<Entry<String,Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += this.PosOrden(hijo);
				
			}

			recorrido += v.element().getKey();
			
			if(!this.arbolGeneral.isRoot(v)) {
				recorrido += "-";
			}
			
			
		} catch (InvalidPositionException e) {}
		return recorrido;
	}
	
	public String rotuloDeLaRaiz() {
		String rotulo = null;
		try {
			rotulo = this.arbolGeneral.root().element().getKey();
		} catch (EmptyTreeException e) {
			e.fillInStackTrace();
		}
		return rotulo;
	}
	
	public int valorDeLaRaiz() {
		int valor = 0;
		try {
			valor = this.arbolGeneral.root().element().getValue();
		} catch (EmptyTreeException e) {
			e.fillInStackTrace();
		}
		return valor;
	}
	
	public void condicionesIniciales() {
		this.seCreoArbol = false;
		this.seCreoRaiz = false;
	}
	
	private int height(Position<Entry<String, Integer>> v) throws InvalidPositionException {

		int altura = 0;
		if(this.arbolGeneral.isExternal(v)) {
			return 0;
		} else {
			for(Position<Entry<String,Integer>> w: this.arbolGeneral.children(v)) {
				altura = Math.max(altura, this.height(w));
			}
			return 1+altura;
		}
	}
	
	
	
}

	

