package net.micwin.tindata.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.micwin.tindata.TinData;

/**
 * Configurator class to knot some events to receivers.
 * 
 * @author MicWin
 * 
 */
public class Configurator {

	public Configurator(Map<Class<?>, List<?>> registrations) {

		// register listener
		for (Entry<Class<?>, List<?>> entry : registrations.entrySet()) {
			Class<?> evtClass = entry.getKey();
			List<?> value = entry.getValue();
			for (Object listener : value) {
				TinData.register(evtClass, listener);
			}
		}
	}
}
