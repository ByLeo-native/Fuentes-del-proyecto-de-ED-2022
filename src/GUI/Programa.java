package GUI;

import java.awt.Container;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import Auxiliar.Queue;
import Auxiliar.QueueEnlazada;
import Excepciones.BoundaryViolationException;
import Excepciones.EmptyQueueException;
import Excepciones.EmptyStackException;
import Excepciones.EmptyTreeException;
import Excepciones.GInvalidOperationException;
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
	
	/**
	 * Constructor para la logica de la interfaz grafica.
	 */
	public Programa () {
		this.seCreoArbol = false;
		this.arbolGeneral = null;
	}
	
	/**
	 * Crea un objeto de tipo arbol general y lo instancia a una de las variables,
	 */
	public void crearArbol() {
		this.arbolGeneral = new ArbolGeneral<Entry<String,Integer>>();
		this.seCreoArbol = true;
	}
	
	/**
	 * Crea la raiz del arbol con un rotulo pasado por parametro y 
	 * @param rotuloDeLaRaiz
	 * @param tree
	 * @param panel
	 * @param contenedor
	 * @return
	 * @throws InvalidOperationException
	 */
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
	
	/**
	 * Agrega un nodo a un nodo que sera el nodo padre y devuelve el grado actualizado del nodo padre.
	 * @param rotuloDeNuevoNodo String del rotulo del nodo a agregar.
	 * @param rotuloDelNodoAncestro String del rotulo del nodo padre del nodo a agregar.
	 * @return entero del grado del nodo ancestro luego de agregar el nuevo nodo.
	 * @throws InvalidPositionException si no se encuentra la posicion del nodo con rotulo del que seria el nodo padre.
	 * @throws GInvalidOperationException si aun no se creo el árbol y el árbol no tiene raiz.
	 */
	public int agregarNodo( String rotuloDeNuevoNodo, String rotuloDelNodoAncestro, JTree tree, JScrollPane panel, Container contenedor) throws InvalidPositionException, GInvalidOperationException {
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
				this.actualizarArbolDeLaGUI( tree, panel, contenedor);
			}
			
			
			
			return nuevoGradoDelNodoAncestro;
		}
	}
	
	/**
	 * Elimina el nodo con el rotulo que pasa por parametro
	 * @param rotuloDelNodo String del rotulo del nodo.
	 * @throws InvalidPositionException si el rotulo pasado por parametro es invalido.
	 */
	public void eliminarNodo( String rotuloDelNodo) throws InvalidPositionException {
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
		
	}
	
	/**
	 * Devuelve una cadena de texto donde se muestran todos los grados y todos los rótulos de los nodos que posean cada grado.
	 * @return una cadena de texto con todos los grados y todos los rotulos de los nodos que posea cada grado.
	 */
	public String obtenerGrados() {
		String textoCompleto = "";
		int gradoDelArbol = 0;
		
		Dictionary<Integer, String> diccionario = new DiccionarioConHashAbierto<Integer,String>();
		Iterator<Entry<String,Integer>> it = this.arbolGeneral.iterator();
		
		//Cada entrada del arbol, las inserto al diccionario donde la clave es el grado de la entrada y el valor el rotulo de la entrada. Ademas se registra el grado del arbol.
		while(it.hasNext()) {
			Entry<String,Integer> entrada = it.next();
			try {
				diccionario.insert(entrada.getValue(), entrada.getKey());
			} catch (InvalidKeyException e) {}
			
			if( gradoDelArbol < entrada.getValue()) {
				gradoDelArbol = entrada.getValue();
			}
		}
		
		//Desde el grado 0 hasta el grado del arbol, se iran añadiendo los rotulo segun la cantidad de grados que tenga.
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
	 * Devuelve el grado del árbol.
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
	 * @param rotulo String con el rotulo a buscar camino.
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

	/**
	 * Devuelve una cadena de texto con los rotulos de los nodos del arbol con un recorrido de preorden.
	 * @return cadena de texto con los rotulos de los nodos del arbol con un recorrido de preorden.
	 */
	public String mostrarRecorridoPreorden() {
		String recorrido = "";
		try {
			recorrido = this.PreOrden(this.arbolGeneral.root());
		} catch (EmptyTreeException e) {}
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
			
		} catch (EmptyTreeException e) {}
		return recorrido;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de los nodos del arbol donde en cada linea se mostraran los rotulos con el mismo nivel (altura).
	 * @return cadena de texto con los rotulos por niveles.
	 */
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
	
	public String eliminarNodosGradoK(int k) {
		String textoDeLosRotulos = "";
		
		Dictionary<Integer,String> diccionario = new DiccionarioConHashAbierto<Integer,String>();
		Iterator<Entry<String,Integer>> it = this.arbolGeneral.iterator();
		
		//Cada entrada del arbol, las inserto al diccionario donde la clave es el grado de la entrada y el valor el rotulo de la entrada. Ademas se registra el grado del arbol.
		while(it.hasNext()) {
			Entry<String,Integer> entrada = it.next();
			try {
				diccionario.insert(entrada.getValue(), entrada.getKey());
			} catch (InvalidKeyException e) {}
		}
		
		try {
			Iterable<Entry<Integer,String>> iterableDeGradoK = diccionario.findAll(k);
			for(Entry<Integer,String> entrada : iterableDeGradoK) {
				textoDeLosRotulos += " "+entrada.getValue()+",";
				this.eliminarNodo(entrada.getValue());
			}
		} catch (InvalidKeyException | InvalidPositionException e) {e.fillInStackTrace();}
		
		if(textoDeLosRotulos.length() != 0) {
			textoDeLosRotulos = textoDeLosRotulos.substring(0, textoDeLosRotulos.length() -1)+".";
		}
		return textoDeLosRotulos;
	}
	
	/**
	 * Devuelve la cantidad de nodos que tiene el árbol.
	 * @return entero que corresponde al tamaño del árbol.
	 */
	public int obtenerTamañoDelArbol() {
		return this.arbolGeneral.size();
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de forma preorden.
	 * @param v posicion desde que se empieza a recorrer de forma preorden.
	 * @return cadena de texto con los rotulos de manera preorden desde la posicion de la entrada pasada por parametro.
	 */
	private String PreOrden(Position<Entry<String,Integer>> v) {
		String recorrido = ""+v.element().getKey();
		try {
			for (Position<Entry<String, Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += "-"+PreOrden(hijo);
			}
		} catch (InvalidPositionException e) {}
		return recorrido;
	}
	
	/**
	 * Devuelve una cadena de texto con los rotulos de forma posOrden.
	 * @param v posicion desde que se empieza a recorrer de forma posorden.
	 * @return cadena de texto con los rotulos de manera posorden desde la posicion de la entrada pasada por parametro.
	 */
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
	
	public void actualizarArbolDeLaGUI(JTree tree, JScrollPane panel, Container contenedor) {
		try {
			Position<Entry<String,Integer>> posDeLaRaiz = this.arbolGeneral.root();
			Entry<String,Integer> raiz = posDeLaRaiz.element();
			
			DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode("("+raiz.getKey()+", "+raiz.getValue()+")");
			
			
			for(Position<Entry<String,Integer>> posHijo : this.arbolGeneral.children(posDeLaRaiz)) {
				this.actualizarArbolDeLaGUIAux(tree, posHijo, nodoRaiz);
			}
			
			tree = new JTree(nodoRaiz);
			
			for (int i = 0; i < tree.getRowCount(); i++) {
			    tree.expandRow(i);
			}
			
			panel = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			panel.setBounds(8, 16, 206, 262);
			contenedor.add(panel);
			
		} catch (EmptyTreeException | InvalidPositionException e) {}
	}
	
	private void actualizarArbolDeLaGUIAux(JTree tree, Position<Entry<String,Integer>> hijo, DefaultMutableTreeNode nodoAncestro) {
		Entry<String,Integer> entrada = hijo.element();
		DefaultMutableTreeNode nodo = new DefaultMutableTreeNode("("+entrada.getKey()+", "+entrada.getValue()+")");
		nodoAncestro.add(nodo);
		
		try {
			if(this.arbolGeneral.isInternal(hijo)) {
				
				for(Position<Entry<String,Integer>> posHijo : this.arbolGeneral.children(hijo)) {
					this.actualizarArbolDeLaGUIAux(tree, posHijo, nodo);
				}
			}
		} catch (InvalidPositionException e) {}
	}
	
	/**
	 * Establece los parametros tal cual estan cuando se solicita el constructor de la clase.
	 */
	public void condicionesIniciales() {
		this.seCreoArbol = false;
		this.seCreoRaiz = false;
		this.arbolGeneral = null;
	}
}

	

