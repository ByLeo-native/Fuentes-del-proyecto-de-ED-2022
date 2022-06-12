package GUI;

import java.util.Iterator;

import Auxiliar.Queue;
import Auxiliar.QueueEnlazada;
import Excepciones.BoundaryViolationException;
import Excepciones.EmptyQueueException;
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

public class Programa {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private Tree<Entry<String, Integer>> arbolGeneral;
	private int gradoDelArbol;
	private String rotuloDelNodoConMasDescendiente;
		
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
				} catch (InvalidPositionException | BoundaryViolationException e) {throw new InvalidPositionException("Error");}	
			}
			//Obtengo informacion para actualizar al nodo padre del nodo a eliminar
			gradoDelNodoAEliminar = pos.element().getValue();
			rotuloDelNodoPadreDelNodoAEliminar = posicionEntradaDelAncestro.element().getKey();
			gradoDelNodoPadreDelNodoAEliminar = posicionEntradaDelAncestro.element().getValue();
			//El padre del nodo a eliminar tendra la cantidad de hijos que tiene + la cantidad de hijos que tiene el nodo a eliminar - 1 (por el nodo eliminado)
			nuevoGradoDelPadreDelNodoAEliminar = gradoDelNodoPadreDelNodoAEliminar + gradoDelNodoAEliminar - 1;
			
			//Elimino al nodo a eliminar
			this.arbolGeneral.removeNode(pos);
			//Actualizo al nodo padre del nodo eliminado con la cantidad correcta de hijos
			this.arbolGeneral.replace(posicionEntradaDelAncestro , new Entrada <String,Integer>( rotuloDelNodoPadreDelNodoAEliminar, nuevoGradoDelPadreDelNodoAEliminar));
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
	
	public String obtenerCamino(String rotulo) throws InvalidPositionException {
		String camino = "";
		boolean seEncontro = false;
		Iterable<Position<Entry<String, Integer>>> list =this.arbolGeneral.positions();
		Iterator<Position<Entry<String, Integer>>> it = list.iterator();
		Position<Entry<String,Integer>> pos = null;
		while(it.hasNext() && !seEncontro) {
			Position<Entry<String, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotulo)) {
				seEncontro = true;
				pos = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se encontro el nodo ancestro "+rotulo+" en el árbol");
		} else {
			camino = ""+pos.element().getKey();
			try {
				Position<Entry<String,Integer>> posAscendente = this.arbolGeneral.parent(pos);
				boolean seCompleto = false;
				
				while( !seCompleto ) {
					camino += "--"+pos.element().getKey();
					if( this.arbolGeneral.isRoot(posAscendente) ) {
						seCompleto = true;
					} else {
						posAscendente = this.arbolGeneral.parent(posAscendente);
					}
				}
			} catch (InvalidPositionException | BoundaryViolationException e) {}
		}
		return camino;
		
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
	
	private void verificarGradoDelArbol(String rotuloDelNodoAVerificar, int cantidadDeDescendienteDeUnNodo) {
		//Cada vez que se modifica el arbol, se utilizara el metodo para ver si hay que actualizar la variable gradoDelArbol y cual es el rotulo
		if ( cantidadDeDescendienteDeUnNodo > this.gradoDelArbol ) {
			this.gradoDelArbol = cantidadDeDescendienteDeUnNodo;
			this.rotuloDelNodoConMasDescendiente = rotuloDelNodoAVerificar;
		} else if (rotuloDelNodoAVerificar.equals(rotuloDelNodoConMasDescendiente)) {
			//Si la cantidad pasada por parametro no es mayor al grado del arbol pero es el mismo rotulo --> se modifico el nodo que tenia mayor descendientes
			this.buscarGradoDelArbol();
		}	
	}
	
	private void buscarGradoDelArbol() {
		//Establezco inicialmente que el grado del arbol es cero
		int mayorGradoEncontrado = 0;
		String rotuloDelNodoDeMayorGradoEncontrado = "";
		if(this.arbolGeneral.size() == 1) {
			mayorGradoEncontrado = 1;
			try {
				rotuloDelNodoDeMayorGradoEncontrado  = this.arbolGeneral.root().element().getKey();
			} catch (EmptyTreeException e) {}
			
		} else {
			
			Iterable<Position<Entry<String,Integer>>> positions = this.arbolGeneral.positions();
			//Por cada posicion que referencia a un nodo del arbol
			for( Position<Entry<String,Integer>> pos : positions) {
				//Asumo que tiene cero o mas hijos
				int cantDeHijosDePos = 0;
				boolean esPosInternal = false;
				
				try {
					esPosInternal = this.arbolGeneral.isInternal(pos);
				} catch (InvalidPositionException e1) {}
				
				//Si el nodo que referencia pos es un nodo interno en el arbol
				if(esPosInternal) {
					Iterable<Position<Entry<String,Integer>>> hijosDePos = null;
					try {
						hijosDePos = this.arbolGeneral.children(pos);
					} catch (InvalidPositionException e) {}
					
					for(Position<Entry<String,Integer>> poshijo: hijosDePos) {
						cantDeHijosDePos++;
					}
					
					if(cantDeHijosDePos > mayorGradoEncontrado) {
						mayorGradoEncontrado = cantDeHijosDePos;
						rotuloDelNodoDeMayorGradoEncontrado = pos.element().getKey();
					}
				}
				
			}
		}
		
		//Luego de la busqueda, actualizo las variables por lo encontrado
		this.gradoDelArbol = mayorGradoEncontrado;
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

	

