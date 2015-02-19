package net.micwin.ticino.wiring;

import java.util.Map;

/**
 * A done wiring.
 * 
 * @author micwin
 *
 */
public class Wiring {

	private Map<Class<?>, Object> fMap;

	Wiring(Map<Class<?>, Object> pMap) {
		this.fMap = pMap;
	}

	public <T> T get(Class<T> pClass) {
		return (T) fMap.get(pClass);
	}

}
