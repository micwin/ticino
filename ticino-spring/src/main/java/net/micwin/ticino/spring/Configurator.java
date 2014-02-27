package net.micwin.ticino.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.micwin.ticino.EventScope;

/**
 * Configurator class to knot some events to receivers.
 * 
 * @author MicWin
 * 
 */
public class Configurator <T> {

	public Configurator(Map<Class<? extends T>, List<?>> registrations,
			EventScope<Object> eventScope) {

		// register listener
		for (Entry<Class<?extends T>, List<?>> entry : registrations.entrySet()) {
			Class<?> evtClass = entry.getKey();
			List<?> value = entry.getValue();
			for (Object listener : value) {
				eventScope.register(evtClass, listener);
			}
		}
	}
}
