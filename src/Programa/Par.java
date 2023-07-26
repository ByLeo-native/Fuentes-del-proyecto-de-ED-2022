package Programa;

import TDADiccionario.Entry;

public class Par <K,V> implements Pair<K,V>, Entry<K,V> {
	//Atributos
		private K clave;
		private V valor;
		
		/**
		 * Constructor de una entrada con clave de tipo generico K y valor de tipo generico V.
		 * @param clave tipo generico K.
		 * @param valor tipo generico V.
		 */
		public Par(K clave, V valor) { 
			this.clave = clave; this.valor = valor; 
		}
		
		/**
		 * Devuelve la clave de la entrada.
		 * @return clave de tipo generico K.
		 */
		public K getKey() { 
			return clave; 
		} 
		
		/**
		 * Devuelve el valor de la entrada.
		 * @return valor de tipo generico V.
		 */
		public V getValue() { 
			return valor; 
		}
		
		/**
		 * Establece la clave de la entrada.
		 * @param clave de tipo generico K.
		 */
		public void setKey( K clave ) { 
			this.clave = clave; 
		} 
		
		/**
		 * Establece el valor de la entrada.
		 * @param valor de tipo generico V.
		 */
		public void setValue(V valor) { 
			this.valor = valor; 
		}
		
		/**
		 * Devuelve una cadena de texto con la clave y valor de la entrada.
		 * @return String con la clave y valor de la entrada.
		 */
		public String toString() { // Para mostrar entradas
			return "(" + getKey() + "," + getValue() + ")" ;
		}
}
