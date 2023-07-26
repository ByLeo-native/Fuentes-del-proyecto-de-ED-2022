package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import Auxiliar.LimitadorCaracteres;
import Excepciones.EmptyTreeException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import Programa.Pair;
import Programa.Programa;
import TDAArbol.Tree;
import TDALista.Position;

/**
 * GUI de un programa de manipulacion de un Arbol general de pares de un caracter y un entero.
 * @author Leonardo Paillamilla, UNS.
 */
@SuppressWarnings("serial")
public class InterfazGrafica extends JFrame {
	
	private Programa programa;
	private JPanel pnOperacion, pnDisplay, pnDisplayRegistro, pnDisplayTree;
	private JComboBox<String> cbAction;
	private JLabel lbNuevoRotulo, lbRotuloDeNodoDefinido;
	private JTextField tfNuevoRotulo, tfRotuloDeNodoDefinido;
	private JButton btnIngresarValores;
	private JTextArea taDisplay, taDisplayRegistro;
	private boolean seCreoArbol;
	private int cantidadDeOperacionesEjecutadas;
	private DateTimeFormatter time;
	private JTree arbol;
	private JScrollPane scroolPnArbol;
	
	/**
	 * Constructor de la intefaz grafica.
	 */
	public InterfazGrafica() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10,10, 854, 480);
		setResizable(false);
		setTitle("Operador de arbol general");
		getContentPane().setLayout(null);
		
		this.seCreoArbol = false;
		this.cantidadDeOperacionesEjecutadas = 1;
		this.time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		
		this.programa = new Programa();
		this.armarComboBox();
		this.armarPanelIngresarValores();
		this.armarPanelDeTexto();
		this.armarPanelDeRegistro();
		this.armarVisualizacionDelArbol();
	}
	
	/**
	 * Arma el panel donde se ingresan los rotulos de los nodos.
	 */
	private void armarPanelIngresarValores() {
		pnOperacion = new JPanel();
		pnOperacion.setLayout(null);
		pnOperacion.setBounds( 8, 40, 252, 152);
		pnOperacion.setBorder(BorderFactory.createTitledBorder("Ingresa valores"));
		getContentPane().add(pnOperacion);
		
		//Creo el label de ingresar rotulo del nodo
		lbNuevoRotulo = new JLabel("Rotulo del nuevo nodo");
		lbNuevoRotulo.setBounds( 8, 20 , 180, 20);
		lbNuevoRotulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el rotulo del nodo
		tfNuevoRotulo = new JTextField();
		tfNuevoRotulo.setBounds( 8, 40, 236, 20);
		tfNuevoRotulo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfNuevoRotulo.setEditable(true);
		tfNuevoRotulo.setDocument(new LimitadorCaracteres(tfNuevoRotulo, 1));//Limito a que solo es pueda ingresar un caracter.
		
		//Creo el label de ingresar rotulo del nodo
		lbRotuloDeNodoDefinido = new JLabel("Rotulo del nodo");
		lbRotuloDeNodoDefinido.setBounds( 8, 68, 180, 20);
		lbRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el rotulo del nodo
		tfRotuloDeNodoDefinido = new JTextField();
		tfRotuloDeNodoDefinido.setBounds( 8, 88, 236, 20);
		tfRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfRotuloDeNodoDefinido.setEditable(false);
		tfRotuloDeNodoDefinido.setDocument(new LimitadorCaracteres(tfRotuloDeNodoDefinido,1));
		
		//Se añade las componentes a su correspondiente panel
		pnOperacion.add(lbNuevoRotulo);
		pnOperacion.add(tfNuevoRotulo);
		pnOperacion.add(lbRotuloDeNodoDefinido);
		pnOperacion.add(tfRotuloDeNodoDefinido);
		
		btnIngresarValores = new JButton("Crear árbol");
		btnIngresarValores.setBounds( 8, 116, 236, 24);
		btnIngresarValores.setFont(new Font("Tahoma", Font.PLAIN, 15));

		this.armarOyenteBoton();
		
		pnOperacion.add(btnIngresarValores);
	}
	
	/**
	 * Arma el panel donde se muestra el resultado de los metodos de recorridos y grados.
	 */
	private void armarPanelDeTexto() {
		pnDisplay = new JPanel();
		pnDisplay.setBounds( 276, 8, 324, 286);
		pnDisplay.setBorder(BorderFactory.createTitledBorder("Consola"));
		pnDisplay.setLayout(null);
		
		taDisplay = new JTextArea();
		taDisplay.setBounds(8, 16, 308, 262);
		taDisplay.setEditable(false);
		
		pnDisplay.add(taDisplay);
		
		getContentPane().add(pnDisplay);
	}
	
	/**
	 * Arma el panel donde se hallara la representación del árbol.
	 */
	private void armarVisualizacionDelArbol() {
		
		pnDisplayTree = new JPanel();
		pnDisplayTree.setBounds(608, 8, 222, 286);
		pnDisplayTree.setBorder(BorderFactory.createTitledBorder("Visualización del árbol"));
		pnDisplayTree.setLayout(null);

		getContentPane().add(pnDisplayTree);
		
	}
	
	/**
	 * Arma el panel donde se muestran las operaciones ejecutadas.
	 */
	private void armarPanelDeRegistro() {
		pnDisplayRegistro = new JPanel();
		pnDisplayRegistro.setBounds( 8, 300, 824, 134);
		pnDisplayRegistro.setBorder(BorderFactory.createTitledBorder("Historial"));
		pnDisplayRegistro.setLayout(null);
		
		taDisplayRegistro = new JTextArea();
		taDisplayRegistro.setBounds(8, 16, 808, 110);
		taDisplayRegistro.setEditable(false);
		
		JScrollPane scroll = new JScrollPane (taDisplayRegistro, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(8,16,808,110);
		
		pnDisplayRegistro.add(scroll);
		
		getContentPane().add(pnDisplayRegistro);
	}
	
	/**
	 * Arma la combobox de las distintas operaciones.
	 */
	private void armarComboBox() {
		cbAction = new JComboBox<String>();
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbAction.setBounds( 8, 8, 250, 24);
		cbAction.setBackground(Color.white);
		
		cbAction.addItem("1- Crear árbol");
		cbAction.addItem("2- Agregar nodo");
		cbAction.addItem("3- Eliminar nodo");
		cbAction.addItem("4- Obtener grados");
		cbAction.addItem("5- Obtener grado del árbol");
		cbAction.addItem("6- Obtener camino");
		cbAction.addItem("7- Mostrar recorrido pre-orden");
		cbAction.addItem("8- Mostrar recorrido por niveles");
		cbAction.addItem("9- Mostrar recorrido pos-orden");
		cbAction.addItem("10- Eliminar nodos grado k");
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		this.armarOyenteComboBox();
		
		getContentPane().add(cbAction);
	}
		
	/**
	 * Añade un oyente al boton al hacer click.
	 */
	private void armarOyenteBoton() {
		btnIngresarValores.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Obtener el indice de la opcion seleccionada en el combobox
				int intComboBox = cbAction.getSelectedIndex();
				
				/* Numero de la opcion coincide con el indice de la combo box
				 * 0- Crear árbol
				 * 1- Agregar nodo
				 * 2- Eliminar nodo
				 * 3- Obtener grados
				 * 4- Obtener grado del árbol
				 * 5- Obtener camino
				 * 6- Mostrar recorrido pre-orden
				 * 7- Mostrar recorrido por niveles
				 * 8- Mostrar recorrido pos-orden
				 * 9- Eliminar nodos grado k
				 */
				
				if( intComboBox == 0) {
					//Obtener los strings de los JTextField
					String sRotuloIngresado = tfNuevoRotulo.getText();
					if(sRotuloIngresado.length()==0) {
						crearVentanaEmergenteFallida("Hubo un error al ingresar los datos.");
					} else {
						Character cRotuloIngresado = tfNuevoRotulo.getText().charAt(0);
						
						try {
							seCreoArbol = programa.crearRaiz(cRotuloIngresado);
						} catch (InvalidOperationException e1) {
							crearVentanaEmergenteFallida("Hubo un error al ingresar los datos");
						}
						
						if(seCreoArbol) {
							actualizarHistorial("- Operacion 1: Se creo el nodo raiz del arbol con rotulo "+cRotuloIngresado+".");
							crearVentanaEmergenteExitosa("<html>¡Se creo el nodo raiz del arbol de forma exitosa!"+
									"<p>* Rotulo de la raiz: "+cRotuloIngresado+"<p>* Grado de la raiz: "+0+"</html>");
							actualizarArbolDeLaGUI();
							tfRotuloDeNodoDefinido.setEditable(true);//Establezco editables a los textField de nodo ingresado
							cbAction.setSelectedIndex(1); //Establezco a la segunda opcion del combo box para crear raiz
							btnIngresarValores.setText("Ingresar valores");//Altero el texto del boton
							limpiarInputs();//Limpio los JTextField
						}
					}
					
				} else if( intComboBox == 1 ) { //Añadir nodo
					try {
						
						if(tfRotuloDeNodoDefinido.getText().length()==0 || tfNuevoRotulo.getText().length()==0) {
							crearVentanaEmergenteFallida("No se ha ingresado los datos correctamente.");
						} else {
							Character cRotuloDeNodoDefinido = tfRotuloDeNodoDefinido.getText().charAt(0);
							//Para añadir un nuevo nodo debo obtener datos del panel de nuevo nodo
							Character cRotuloIngresado = tfNuevoRotulo.getText().charAt(0);
							int nuevoGradoDelNodoAncestro = programa.agregarNodo(cRotuloIngresado, cRotuloDeNodoDefinido);
							actualizarHistorial("- Operacion 2: Se añadio el nodo ("+cRotuloIngresado+", "+0+") que tiene como padre al nodo ("+cRotuloDeNodoDefinido+", "+nuevoGradoDelNodoAncestro+").");
							
							//Ventana emergente de que se logro añadir un nodo
							crearVentanaEmergenteExitosa("<html>"
									+ "Se añadio un nuevo hijo al nodo ( "+cRotuloDeNodoDefinido+", "+nuevoGradoDelNodoAncestro+")"
											+ "<p>* Rotulo del nuevo nodo: "+cRotuloIngresado+"<p>* Grado del nuevo nodo: "+0+"</html>");
							actualizarArbolDeLaGUI();
						}
						
					} catch (InvalidPositionException e1) {crearVentanaEmergenteFallida(e1.getMessage());} 
					
				} else if( intComboBox == 2 ) { //Eliminar nodo
					try {
						if(tfRotuloDeNodoDefinido.getText().length()==0) {
							crearVentanaEmergenteFallida("No se ha ingresado los datos correctamente.");
						} else {
							Character cRotuloDeNodoDefinido = tfRotuloDeNodoDefinido.getText().charAt(0);
							programa.eliminarNodo(cRotuloDeNodoDefinido);
							actualizarHistorial("- Operacion 3: Se elimino el nodo raiz del arbol con rotulo "+cRotuloDeNodoDefinido+".");
							
							crearVentanaEmergenteExitosa("<html>¡Se elimino el nodo "+cRotuloDeNodoDefinido+" del arbol!</html>");
							
							actualizarArbolDeLaGUI();
						}
					} catch (InvalidPositionException error) {crearVentanaEmergenteFallida(error.getMessage());}
				} else if( intComboBox == 3) { //Obtener grados
						taDisplay.setText(programa.obtenerGrados());
						actualizarHistorial("- Operacion 4: Se solicito obtener los grados de los nodos del arbol.");
				} else if( intComboBox == 4) { //Obtener grado del arbol
					int gradoDelArbol = programa.obtenerGradoDelArbol();
					crearVentanaEmergenteExitosa("Grado actual del árbol: "+gradoDelArbol);
					actualizarHistorial("- Operacion 5: Se solicito obtener el grado del arbol. Se obtuvo como resultado: "+gradoDelArbol);
				} else if( intComboBox == 5) { //Obtener camino
					try {
						if(tfRotuloDeNodoDefinido.getText().length()==0) {
							crearVentanaEmergenteFallida("No se ha ingresado los datos correctamente");
						} else {
							Character cRotuloDeNodoDefinido = tfRotuloDeNodoDefinido.getText().charAt(0);
							taDisplay.setText("Camino desde la raiz al nodo con rotulo "+cRotuloDeNodoDefinido+": \n" +programa.obtenerCamino(cRotuloDeNodoDefinido));
							actualizarHistorial("- Operacion 6: Se solicito mostrar el camino desde la raiz al nodo con rotulo "+cRotuloDeNodoDefinido+".");
						}
						
					} catch (InvalidPositionException e1) {crearVentanaEmergenteFallida(e1.getMessage());}
				} else if( intComboBox == 6) {
					taDisplay.setText("Recorrido Preorden: "+programa.mostrarRecorridoPreorden());
					actualizarHistorial("- Operacion 7: Se solicito mostrar los rotulos del árbol con un recorrido preOrden.");
				} else if( intComboBox == 7) {
					taDisplay.setText("Recorrido por niveles: \n"+programa.mostrarPorNiveles());
					actualizarHistorial("- Operacion 8: Se solicito mostrar los rotulos del árbol por niveles.");
				} else if( intComboBox == 8) {
					taDisplay.setText("Recorrido Posorden: "+programa.mostrarRecorridoPosorden());
					actualizarHistorial("- Operacion 9: Se solicito mostrar los rotulos del árbol con un recorrido posOrden.");
				} else if( intComboBox == 9) {}
				limpiarInputs();
			}
		});
	}
	
	/**
	 * Añade un oyente a la combobox al seleccionar una opción.
	 */
	private void armarOyenteComboBox() {
		cbAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Numero de la opcion coincide con el indice de la combo box
				 * 0- Crear árbol
				 * 1- Agregar nodo
				 * 2- Eliminar nodo
				 * 3- Obtener grados
				 * 4- Obtener grado del árbol
				 * 5- Obtener camino
				 * 6- Mostrar recorrido pre-orden
				 * 7- Mostrar recorrido por niveles
				 * 8- Mostrar recorrido pos-orden
				 * 9- Eliminar nodos grado k
				 */
				//Obtengo el indice de la opcion selecciona de la combo box
				int indexComboBox = cbAction.getSelectedIndex();
				if(!seCreoArbol && indexComboBox != 0) {
					crearVentanaEmergenteFallida("¡Aun no se creo el árbol! \nPor favor, ingrese un rotulo haga clic en el boton para crear el árbol.");
					//Establezco que debe elegir la primera opcion del combobox
					cbAction.setSelectedIndex(0);
				} 
			
				if(seCreoArbol) {
					
					if(indexComboBox == 0) {
						crearVentanaEmergenteFallida("Ya se creo el árbol");
						cbAction.setSelectedIndex(1);
					} else if(indexComboBox == 1) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(true);
						tfRotuloDeNodoDefinido.setEditable(true);
						btnIngresarValores.setText("Ingresar valores");
					} else if(indexComboBox == 2) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(true);
						//Altero el texto del boton
						btnIngresarValores.setText("Ingresar valores");
					} else if(indexComboBox == 3) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						//Altero el texto del boton
						btnIngresarValores.setText("Obtener grados ...");
					} else if(indexComboBox == 4) {
						limpiarInputs();
					
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						btnIngresarValores.setText("Obtener grado del árbol ...");
						
					} else if(indexComboBox == 5) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(true);
						btnIngresarValores.setText("Mostrar camino");
					} else if(indexComboBox == 6) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						btnIngresarValores.setText("Mostrar recorrido pre-orden");
						
					} else if(indexComboBox == 7) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						btnIngresarValores.setText("Mostrar recorrido por niveles");
						
					} else if(indexComboBox == 8) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						btnIngresarValores.setText("Mostrar recorrido pos-orden");
						
					} else if(indexComboBox == 9) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						
						boolean seSalio = false;
						boolean seElimino = false;
						while(!seSalio) {
							String k = JOptionPane.showInputDialog( null, "Indica el grado k que deseas utilizar:");
							
							if( k == null ) {
								seSalio = true;
							} else if( k != "" && k.matches("[+-]?\\d*(\\.\\d+)?")) {
								int intK = Integer.parseInt(k);
								String textoDeLosRotulos = programa.eliminarNodosGradoK(intK);
								
								if( textoDeLosRotulos.length() != 0) {
									crearVentanaEmergenteExitosa("¡Se eliminaron todos los nodos de grado "+intK+" del árbol! \n Los nodos eliminados fueron: "+textoDeLosRotulos+".");
									actualizarHistorial("- Operacion 10: Se solicito eliminar a todos los nodos de grado "+intK+" del árbol. Los nodos eliminados fueron: "+textoDeLosRotulos+".");
									seSalio = true;
									seElimino = true;
								} else {
									crearVentanaEmergenteFallida("No se encontraron nodos que cuenten con "+k+" descendientes directos.");
									actualizarHistorial("- Operacion 10: Se solicito eliminar a todos los nodos de grado "+intK+" del árbol, sin embargo, el arbol no cuenta con nodos con el grado indicado.");
								}
							} else {
								crearVentanaEmergenteFallida("Se ingreso un valor invalido.\nAsegurese de ingresar un valor correctamente.");
							}
						}
						
						if(!seElimino) {
							crearVentanaEmergenteFallida("¡No se ha completado la función!\nNo se eliminaron nodos.");
						} else {
							actualizarArbolDeLaGUI();
						}
						
						//Pongo ya para utilizar la funcion de agregar nodo.
						cbAction.setSelectedIndex(1);
						limpiarInputs();
						tfNuevoRotulo.setEditable(true);
						tfRotuloDeNodoDefinido.setEditable(true);
						btnIngresarValores.setText("Ingresar valores");
					}
				}
			}
		});
	}
	
	/**
	 * Actualiza la representacion visual del arbol.
	 * @param tree JTree referencia del árbol de la GUI.
	 * @param panel JScrollPane referencia del panel con scroll de la GUI.
	 * @param contenedor JPanel contenedor donde se añadira el panel.
	 */
	private void actualizarArbolDeLaGUI() {
		try {
			
			Tree<Pair<Character,Integer>> referenciaDelArbol = this.programa.referenciaDelArbol();
			if(referenciaDelArbol.isEmpty()) {
				this.condicionesIniciales();
			} else {
				Position<Pair<Character,Integer>> posDeLaRaiz = referenciaDelArbol.root();
				Pair<Character,Integer> raiz = posDeLaRaiz.element();
				
				DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode("("+raiz.getKey()+", "+raiz.getValue()+")");
		
				for(Position<Pair<Character,Integer>> posHijo : referenciaDelArbol.children(posDeLaRaiz)) {
					this.actualizarArbolDeLaGUIAux(posHijo, nodoRaiz, referenciaDelArbol);
				}
				
				this.arbol = new JTree(nodoRaiz);
				
				for (int i = 0; i < this.arbol .getRowCount(); i++) {
					this.arbol.expandRow(i);
				}
				
				scroolPnArbol = new JScrollPane(this.arbol, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroolPnArbol.setBounds(8, 16, 206, 262);
				pnDisplayTree.add(scroolPnArbol);
			}
		} catch (EmptyTreeException | InvalidPositionException e) {e.fillInStackTrace();}
	}
	
	/**
	 * Metodo auxiliar que añade un nodo hijo a su correspondiente nodo padre de un árbol JTree.
	 * @param tree JTree referencia del árbol de la GUI.
	 * @param hijo Posición del nodo en el árbol.
	 * @param nodoAncestro DefaultMutableTreeNode referencia del nodo padre.
	 */
	private void actualizarArbolDeLaGUIAux(Position<Pair<Character,Integer>> hijo, DefaultMutableTreeNode nodoAncestro, Tree<Pair<Character, Integer>> referenciaDelArbol) {
		Pair<Character,Integer> entrada = hijo.element();
		DefaultMutableTreeNode nodo = new DefaultMutableTreeNode("("+entrada.getKey()+", "+entrada.getValue()+")");
		nodoAncestro.add(nodo);
		try {
			if(referenciaDelArbol.isInternal(hijo)) {
				
				for(Position<Pair<Character,Integer>> posHijo : referenciaDelArbol.children(hijo)) {
					this.actualizarArbolDeLaGUIAux(posHijo, nodo, referenciaDelArbol);
				}
			}
		} catch (InvalidPositionException e) {e.fillInStackTrace();}
	}
	
	/**
	 * Metodo que establece las condiones iniciales al borrar todos los nodos de un árbol.
	 */
	private void condicionesIniciales() {
		this.programa.condicionesIniciales();
		this.seCreoArbol = false;
		cbAction.setSelectedIndex(0);
		tfNuevoRotulo.setEditable(true);
		tfRotuloDeNodoDefinido.setEditable(false);
		this.btnIngresarValores.setText("Crear árbol");
	}
	
	/**
	 * Crea una ventana emergente de informacion con un mensaje.
	 * @param mensaje String que se mostrara en la ventana.
	 */
	private void crearVentanaEmergenteExitosa(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Crea una ventana emergente de error con un mensaje.
	 * @param mensaje String que se mostrara en la ventana.
	 */
	private void crearVentanaEmergenteFallida(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion fallida", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Borra el texto que se encuentre en los inputs de rotulos.
	 */
	private void limpiarInputs() {
		tfNuevoRotulo.setText(null);
		tfRotuloDeNodoDefinido.setText(null);
	}
	
	/**
	 * Actualiza el registro de operaciones ejecutadas.
	 * @param msg String resultado de la ejecución de una función.
	 */
	private void actualizarHistorial(String msg) {
		taDisplayRegistro.setText(taDisplayRegistro.getText()+this.cantidadDeOperacionesEjecutadas+" - "+time.format(LocalDateTime.now())+" " +msg+"\n");
		this.cantidadDeOperacionesEjecutadas++;
	}
	
	public static void main (String [] args) {
		InterfazGrafica interfazGrafica = new InterfazGrafica();
		interfazGrafica.setVisible(true);
	}
}
