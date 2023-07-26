package TDADiccionario;

import Programa.Pair;

/**
 * Interface Entry.
 * @author Leonardo Paillamilla, UNS.
 * @param <K> tipo generico de la clave de la entrada.
 * @param <V> tipo generico del valor de la entrada.
 */
public interface Entry <K,V> extends Pair<K,V> {
	/**
	 * Devuelve la clave de la entrada.
	 * @return clave de tipo generico K.
	 */
	public K getKey();
	
	/**
	 * Devuelve el valor de la entrada.
	 * @return valor de tipo generico V.
	 */
	public V getValue();
}
