package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import Auxiliar.LimitadorCaracteres;
import Excepciones.GInvalidOperationException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;

@SuppressWarnings("serial")
public class InterfazGrafica extends JFrame {
	
	private Programa programa;
	private JPanel pnOperacion, pnDatos, pnDisplay, pnDisplayRegistro, pnDisplayTree;
	private JComboBox<String> cbAction;
	private JLabel lbNuevoRotulo, lbRotuloDeNodoDefinido, lbTamañoDelArbol;
	private JTextField tfNuevoRotulo, tfRotuloDeNodoDefinido;
	private JButton btnIngresarValores;
	private JTextArea taDisplay, taDisplayRegistro;
	private boolean seCreoArbol;
	private int tamañoDelArbol;
	private int cantidadDeOperacionesEjecutadas;
	private DateTimeFormatter time;
	private JTree arbol;
	private JScrollPane scroolPnArbol;
	
	public InterfazGrafica() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10,10, 854, 480);
		setResizable(false);
		setTitle("Operador de arbol general");
		getContentPane().setLayout(null);
		
		this.seCreoArbol = false;
		this.cantidadDeOperacionesEjecutadas = 1;
		this.time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		
		this.tamañoDelArbol = 0;
		this.programa = new Programa();
		this.armarComboBox();
		this.armarPanelIngresarValores();
		this.armarPanelDatos();
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
	 * Arma el panel donde se muestra el tamaño del árbol.
	 */
	private void armarPanelDatos() {
		pnDatos = new JPanel();
		pnDatos.setBounds( 8, 200, 252, 96);
		pnDatos.setBorder(BorderFactory.createTitledBorder("Panel de datos"));
		pnDatos.setLayout(null);
		getContentPane().add(pnDatos);
		
		lbTamañoDelArbol = new JLabel("Tamaño del arbol: "+this.tamañoDelArbol);
		lbTamañoDelArbol.setBounds( 8, 16, 236, 20);
		lbTamañoDelArbol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbTamañoDelArbol.setVisible(true);
		
		pnDatos.add(lbTamañoDelArbol);
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
				//Para todas las opciones requiero los valores de algun nodo definido
				String sRotuloDeNodoDefinido = null;
				//Para las opciones que requieren de los datos de un nodo definido, obtengo los valores de los textField
				if(necesitaLosDatosDeUnNodoDefinido(intComboBox)) {
					sRotuloDeNodoDefinido = tfRotuloDeNodoDefinido.getText();
				}
				
				if( intComboBox == 0) {
					//Obtener los strings de los JTextField
					String sRotuloIngresado = tfNuevoRotulo.getText();
					
					try {
						//Se crea el arbol 
						programa.crearArbol();
						seCreoArbol = programa.crearRaiz(sRotuloIngresado);
						programa.actualizarArbolDeLaGUI(arbol, scroolPnArbol, pnDisplayTree);
					} catch (InvalidOperationException e1) {
						crearVentanaEmergenteFallida("Hubo un error al ingresar los datos");
					}
					
					if(seCreoArbol) {
						//armarVisualizacionDelArbol(new DefaultMutableTreeNode("("+sRotuloIngresado+", 0)"));
						actualizarHistorial("- Operacion 1: Se creo el nodo raiz del arbol con rotulo "+sRotuloIngresado+".");
						//actualizar arbol de la gui
						programa.actualizarArbolDeLaGUI(arbol, scroolPnArbol, pnDisplayTree);
						//Lanzo mensaje de exito
						crearVentanaEmergenteExitosa("<html>¡Se creo el nodo raiz del arbol de forma exitosa!"+
								"<p>* Rotulo de la raiz: "+sRotuloIngresado+"<p>* Grado de la raiz: "+0+"</html>");
						//Establezco editables a los textField de nodo ingresado
						tfRotuloDeNodoDefinido.setEditable(true);
						//Establezco a la segunda opcion del combo box para crear raiz
						cbAction.setSelectedIndex(1);
						//Altero el texto del boton
						btnIngresarValores.setText("Ingresar valores");
						//Limpio los JTextField
						limpiarInputs();
					}
				} else if( intComboBox == 1 ) { //Añadir nodo
					try {
						//Para añadir un nuevo nodo debo obtener datos del panel de nuevo nodo
						String sRotuloIngresado = tfNuevoRotulo.getText();
						int nuevoGradoDelNodoAncestro = programa.agregarNodo(sRotuloIngresado, sRotuloDeNodoDefinido, arbol, scroolPnArbol, pnDisplayTree);
						actualizarHistorial("- Operacion 2: Se añadio el nodo ("+sRotuloIngresado+", "+0+") que tiene como padre al nodo ("+sRotuloDeNodoDefinido+", "+nuevoGradoDelNodoAncestro+").");
						
						//actualizar arbol de la gui
						programa.actualizarArbolDeLaGUI(arbol, scroolPnArbol, pnDisplayTree);
						
						//Ventana emergente de que se logro añadir un nodo
						crearVentanaEmergenteExitosa("<html>"
								+ "Se añadio un nuevo hijo al nodo ( "+sRotuloDeNodoDefinido+", "+nuevoGradoDelNodoAncestro+")"
										+ "<p>* Rotulo del nuevo nodo: "+sRotuloIngresado+"<p>* Grado del nuevo nodo: "+0+"</html>");
					} catch (InvalidPositionException | GInvalidOperationException e1) {crearVentanaEmergenteFallida(e1.getMessage());} 
					
				} else if( intComboBox == 2 ) { //Eliminar nodo
					try {
						programa.eliminarNodo(sRotuloDeNodoDefinido);
						actualizarHistorial("- Operacion 3: Se elimino el nodo raiz del arbol con rotulo "+sRotuloDeNodoDefinido+".");
						
						//actualizar arbol de la gui
						programa.actualizarArbolDeLaGUI(arbol, scroolPnArbol, pnDisplayTree);
						
						crearVentanaEmergenteExitosa("<html>¡Se elimino el nodo "+sRotuloDeNodoDefinido+" del arbol!</html>");
						
					} catch (InvalidPositionException error) {crearVentanaEmergenteFallida(error.getMessage());}
				} else if( intComboBox == 3) { //Obtener grados
					taDisplay.setText(programa.obtenerGrados());
					actualizarHistorial("- Operacion 4: Se solicito obtener los grados de los nodos del arbol");
				} else if( intComboBox == 4) { //Obtener grado del arbol
					actualizarHistorial("- Operacion 5: Se solicito obtener el grado del arbol");
					crearVentanaEmergenteExitosa("Grado actual del árbol: "+programa.obtenerGradoDelArbol());
				} else if( intComboBox == 5) { //Obtener camino
					try {
						taDisplay.setText("Camino desde la raiz al nodo con rotulo "+sRotuloDeNodoDefinido+": \n" +programa.obtenerCamino(sRotuloDeNodoDefinido));
						actualizarHistorial("- Operacion 6: Se solicito mostrar el camino desde la raiz al nodo con rotulo "+sRotuloDeNodoDefinido);
					} catch (InvalidPositionException e1) {crearVentanaEmergenteFallida(e1.getMessage());}
				} else if( intComboBox == 6) {
					taDisplay.setText("Recorrido Preorden: "+programa.mostrarRecorridoPreorden());
					actualizarHistorial("- Operacion 7: Se solicito mostrar los rotulos del árbol con un recorrido preOrden");
				} else if( intComboBox == 7) {
					taDisplay.setText("Recorrido por niveles: \n"+programa.mostrarPorNiveles());
					actualizarHistorial("- Operacion 8: Se solicito mostrar los rotulos del árbol por niveles");
				} else if( intComboBox == 8) {
					taDisplay.setText("Recorrido Posorden: "+programa.mostrarRecorridoPosorden());
					actualizarHistorial("- Operacion 9: Se solicito mostrar los rotulos del árbol con un recorrido posOrden");
				} else if( intComboBox == 9) {
					
				}
				limpiarInputs();
				//Luego de cualquier accion -> actualizo el panel de datos
				actualizarPanelDatos();
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
					crearVentanaEmergenteFallida("Aun no se creo el árbol");
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
						
						String k = JOptionPane.showInputDialog("Indica el grado k que deseas utilizar");
						int intK = Integer.parseInt(k);
						String textoDeLosRotulos = programa.eliminarNodosGradoK(intK);
						
						actualizarHistorial("- Operacion 10: Se solicito eliminar a todos los nodos de grado "+intK+" del árbol. Los nodos eliminados fueron: "+textoDeLosRotulos);
						
						//actualizar arbol de la gui
						programa.actualizarArbolDeLaGUI(arbol, scroolPnArbol, pnDisplayTree);
						
						crearVentanaEmergenteExitosa("¡Se eliminaron todos los nodos de grado "+intK+" del árbol! \n Los nodos eliminados fueron: "+textoDeLosRotulos);
					}
				}
				
			}
		});
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
	 * Indica si el metodo seleccionado en la combobox necesita el rotulo de un nodo existente en el árbol.
	 * @param index indice de la opción seleccionada de la combobox.
	 * @return verdadero si necesita el rotulo de un nodo existente, falso en caso contrario.
	 */
	private boolean necesitaLosDatosDeUnNodoDefinido(int index) {
		return index == 1 || index == 2 || index == 5;
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
	 * Actualiza los valores del panel de
	 */
	private void actualizarPanelDatos() {
		this.tamañoDelArbol = this.programa.obtenerTamañoDelArbol();
		lbTamañoDelArbol.setText("Tamaño del arbol: "+this.tamañoDelArbol);
		if(this.tamañoDelArbol == 0) {
			condicionesIniciales();
		}
	}
	
	private void actualizarHistorial(String msg) {
		taDisplayRegistro.setText(taDisplayRegistro.getText()+this.cantidadDeOperacionesEjecutadas+" - "+time.format(LocalDateTime.now())+" " +msg+"\n");
		this.cantidadDeOperacionesEjecutadas++;
	}
	
	public static void main (String [] args) {
		InterfazGrafica interfazGrafica = new InterfazGrafica();
		interfazGrafica.setVisible(true);
	}
}
