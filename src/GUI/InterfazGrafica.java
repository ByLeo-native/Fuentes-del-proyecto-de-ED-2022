package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Excepciones.GInvalidOperationException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;

public class InterfazGrafica extends JFrame {
	
	private Programa programa;
	private JPanel pnOperacion, pnDatos, pnDisplay;
	private JComboBox<String> cbAction;
	private JLabel lbNuevoRotulo, lbRotuloDeNodoDefinido, lbTama�oDelArbol;
	private JTextField tfNuevoRotulo, tfRotuloDeNodoDefinido;
	private JButton btnIngresarValores;
	private JTextArea taDisplay;
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private int tama�oDelArbol;
	
	public InterfazGrafica() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10,10, 854, 480);
		setResizable(false);
		setTitle("Operador de arbol general");
		getContentPane().setLayout(null);
		
		this.seCreoArbol = false;
		this.seCreoRaiz = false;
		
		this.tama�oDelArbol = 0;
		this.programa = new Programa();
		this.armarComboBox();
		this.armarPanelIngresarValores();
		this.armarPanelDatos();
		this.armarPanelDeTexto();
	}
	
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
		
		//Creo el label de ingresar rotulo del nodo
		lbRotuloDeNodoDefinido = new JLabel("Rotulo del nodo");
		lbRotuloDeNodoDefinido.setBounds( 8, 68, 180, 20);
		lbRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el rotulo del nodo
		tfRotuloDeNodoDefinido = new JTextField();
		tfRotuloDeNodoDefinido.setBounds( 8, 88, 236, 20);
		tfRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfRotuloDeNodoDefinido.setEditable(false);
		
		pnOperacion.add(lbNuevoRotulo);
		pnOperacion.add(tfNuevoRotulo);
		pnOperacion.add(lbRotuloDeNodoDefinido);
		pnOperacion.add(tfRotuloDeNodoDefinido);
		
		btnIngresarValores = new JButton("Crear �rbol");
		btnIngresarValores.setBounds( 8, 116, 236, 24);
		btnIngresarValores.setFont(new Font("Tahoma", Font.PLAIN, 16));

		this.armarOyenteBoton();
		
		pnOperacion.add(btnIngresarValores);
	}
	
	private void armarPanelDatos() {
		pnDatos = new JPanel();
		pnDatos.setBounds( 8, 200, 252, 96);
		pnDatos.setBorder(BorderFactory.createTitledBorder("Panel de datos"));
		pnDatos.setLayout(null);
		getContentPane().add(pnDatos);
		
		lbTama�oDelArbol = new JLabel("Tama�o del arbol: "+this.tama�oDelArbol);
		lbTama�oDelArbol.setBounds( 8, 16, 236, 20);
		lbTama�oDelArbol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbTama�oDelArbol.setVisible(true);
		
		pnDatos.add(lbTama�oDelArbol);
	}
	
	private void armarPanelDeTexto() {
		pnDisplay = new JPanel();
		pnDisplay.setBounds( 276, 8, 250, 250);
		pnDisplay.setBorder(BorderFactory.createTitledBorder(""));
		pnDisplay.setLayout(null);
		
		taDisplay = new JTextArea();
		taDisplay.setBounds(8, 8, 234, 234);
		taDisplay.setEditable(false);
		
		pnDisplay.add(taDisplay);
		
		getContentPane().add(pnDisplay);
	}
	
	private void armarComboBox() {
		cbAction = new JComboBox<String>();
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbAction.setBounds( 8, 8, 250, 24);
		cbAction.setBackground(Color.white);
		
		cbAction.addItem("1- Crear �rbol");
		cbAction.addItem("2- Agregar nodo");
		cbAction.addItem("3- Eliminar nodo");
		cbAction.addItem("4- Obtener grados");
		cbAction.addItem("5- Obtener grado del �rbol");
		cbAction.addItem("6- Obtener camino");
		cbAction.addItem("7- Mostrar recorrido pre-orden");
		cbAction.addItem("8- Mostrar recorrido por niveles");
		cbAction.addItem("9- Mostrar recorrido post-orden");
		cbAction.addItem("10- Eliminar nodos grado k");
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		this.armarOyenteComboBox();
		
		getContentPane().add(cbAction);
	}
		
	private void armarOyenteBoton() {
		btnIngresarValores.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Obtener el indice de la opcion seleccionada en el combobox
				int intComboBox = cbAction.getSelectedIndex();
				
				/* Numero de la opcion coincide con el indice de la combo box
				 * 0- Crear �rbol
				 * 1- Agregar nodo
				 * 2- Eliminar nodo
				 * 3- Obtener grados
				 * 4- Obtener grado del �rbol
				 * 5- Obtener camino
				 * 6- Mostrar recorrido pre-orden
				 * 7- Mostrar recorrido por niveles
				 * 8- Mostrar recorrido post-orden
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
					} catch (InvalidOperationException e1) {
						crearVentanaEmergenteFallida("Hubo un error al ingresar los datos");
					}
					
					if(seCreoArbol) {
						//Lanzo mensaje de exito
						crearVentanaEmergenteExitosa("<html>�Se creo el nodo raiz del arbol de forma exitosa!"+
								"<p>* Rotulo de la raiz: "+sRotuloIngresado+"<p>* Valor de la raiz: "+0+"</html>");
						//Establezco editables a los textField de nodo ingresado
						tfRotuloDeNodoDefinido.setEditable(true);
						//Establezco a la segunda opcion del combo box para crear raiz
						cbAction.setSelectedIndex(1);
						//Altero el texto del boton
						btnIngresarValores.setText("Ingresar valores");
						//Limpio los JTextField
						limpiarInputs();	
					}
				} else if( intComboBox == 1 ) { //A�adir nodo
					
					try {
						//Para a�adir un nuevo nodo debo obtener datos del panel de nuevo nodo
						String sRotuloIngresado = tfNuevoRotulo.getText();
						int nuevoGradoDelNodoAncestro = programa.agregarNodo(sRotuloIngresado, sRotuloDeNodoDefinido);
						//Ventana emergente de que se logro a�adir un nodo
						crearVentanaEmergenteExitosa("<html>"
								+ "Se a�adio un nuevo hijo al nodo ( "+sRotuloDeNodoDefinido+", "+nuevoGradoDelNodoAncestro+")"
										+ "<p>* Rotulo del nuevo nodo: "+sRotuloIngresado+"<p>* Grado del nuevo nodo: "+0+"</html>");
					} catch (InvalidPositionException | GInvalidOperationException e1) {crearVentanaEmergenteFallida(e1.getMessage());} 
					
				} else if( intComboBox == 2 ) { //Eliminar nodo

					try {
						programa.eliminarNodo(sRotuloDeNodoDefinido);
						crearVentanaEmergenteExitosa("<html>�Se elimino el nodo "+sRotuloDeNodoDefinido+" del arbol!</html>");
					} catch (InvalidPositionException error) {crearVentanaEmergenteFallida(error.getMessage());}
				} else if( intComboBox == 3) { //Obtener grados
					taDisplay.setText(programa.obtenerGrados());
				} else if( intComboBox == 4) { //Obtener grado del arbol
					crearVentanaEmergenteExitosa("Grado actual del �rbol: "+programa.obtenerGradoDelArbol());
				} else if( intComboBox == 5) { //Obtener camino
					try {
						taDisplay.setText("Camino desde la raiz al nodo con rotulo "+sRotuloDeNodoDefinido+": \n" +programa.obtenerCamino(sRotuloDeNodoDefinido));
					} catch (InvalidPositionException e1) {crearVentanaEmergenteFallida(e1.getMessage());}
				} else if( intComboBox == 6) {
					taDisplay.setText("Recorrido Preorden: "+programa.mostrarRecorridoPreorden());
				} else if( intComboBox == 7) {
					taDisplay.setText("Recorrido por niveles: \n"+programa.mostrarPorNiveles());
				} else if( intComboBox == 8) {
					taDisplay.setText("Recorrido Postorden: "+programa.mostrarRecorridoPostorden());
				} else if( intComboBox == 9) {
				
				}
				limpiarInputs();
				//Luego de cualquier accion -> actualizo el panel de datos
				actualizarPanelDatos();
			}
		});
	}
	
	private void armarOyenteComboBox() {
		cbAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Numero de la opcion coincide con el indice de la combo box
				 * 0- Crear �rbol
				 * 1- Agregar nodo
				 * 2- Eliminar nodo
				 * 3- Obtener grados
				 * 4- Obtener grado del �rbol
				 * 5- Obtener camino
				 * 6- Mostrar recorrido pre-orden
				 * 7- Mostrar recorrido por niveles
				 * 8- Mostrar recorrido post-orden
				 * 9- Eliminar nodos grado k
				 */
				//Obtengo el indice de la opcion selecciona de la combo box
				int indexComboBox = cbAction.getSelectedIndex();
				if(!seCreoArbol && indexComboBox != 0) {
					crearVentanaEmergenteFallida("Aun no se creo el �rbol");
					//Establezco que debe elegir la primera opcion del combobox
					cbAction.setSelectedIndex(0);
				} 
			
				if(seCreoArbol) {
					
					if(indexComboBox == 0) {
						crearVentanaEmergenteFallida("Ya se creo el �rbol");
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
						btnIngresarValores.setText("Obtener grado del �rbol ...");
						
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
						btnIngresarValores.setText("Mostrar recorrido post-orden");
					} else if(indexComboBox == 9) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(false);
						
						String k = JOptionPane.showInputDialog("Indica el grado k que deseas utilizar");
						int intK = Integer.parseInt(k);
						programa.eliminarNodosGradoK(intK);
						crearVentanaEmergenteExitosa("�Se eliminaron todos los nodos de grado"+intK+" del �rbol!");
					}
				}
				
			}
		});
	}
	
	private void condicionesIniciales() {
		this.programa.condicionesIniciales();
		this.seCreoArbol = false;
		cbAction.setSelectedIndex(0);
		tfNuevoRotulo.setEditable(true);
		tfRotuloDeNodoDefinido.setEditable(false);
		this.btnIngresarValores.setText("Crear �rbol");
	}
	
	private boolean necesitaLosDatosDeUnNodoDefinido(int index) {
		return index == 1 || index == 2 || index == 5;
	}
	
	private void crearVentanaEmergenteExitosa(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void crearVentanaEmergenteFallida(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion fallida", JOptionPane.ERROR_MESSAGE);
	}
	
	private void limpiarInputs() {
		tfNuevoRotulo.setText(null);
		tfRotuloDeNodoDefinido.setText(null);
	}
	
	private void actualizarPanelDatos() {
		this.tama�oDelArbol = this.programa.obtenerTama�oDelArbol();
		lbTama�oDelArbol.setText("Tama�o del arbol: "+this.tama�oDelArbol);
		if(this.tama�oDelArbol == 0) {
			condicionesIniciales();
		}
	}
	
	public static void main (String [] args) {
		InterfazGrafica interfazGrafica = new InterfazGrafica();
		interfazGrafica.setVisible(true);
	}
}
