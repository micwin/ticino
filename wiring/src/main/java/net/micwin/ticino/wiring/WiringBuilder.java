package net.micwin.ticino.wiring;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A builder to easily create wirings. Not reusable
 * 
 * @author micwin
 *
 */
public class WiringBuilder {

	private Map<Class<?>, Object> fMap = new HashMap<Class<?>, Object>();

	/**
	 * Adds a new silbling to the wiring.
	 */
	public WiringBuilder add(Object pSilbling) {

		fMap.put(pSilbling.getClass(), pSilbling);
		return this;

	}

	/**
	 * Does the actual wiring.
	 * 
	 * @return
	 */
	public Wiring wire() {

		for (Iterator<?> lIterator = fMap.values().iterator(); lIterator
				.hasNext();) {
			Object lObject = lIterator.next();
			pWire(lObject, lObject.getClass());

		}
		return new Wiring(fMap);
	}

	/**
	 * Wires the fields declared on given class level and recursively descends
	 * to superclass.
	 * 
	 * @param pObject
	 * @param pClassLevel
	 */
	private void pWire(Object pObject, Class<? extends Object> pClassLevel) {

		if (pClassLevel == Object.class || pClassLevel.isInterface()
				|| pClassLevel.isPrimitive()) {
			return;
		}

		Field[] lFields = pClassLevel.getDeclaredFields();
		for (Field lField : lFields) {
			int lModifiers = lField.getModifiers();
			if (Modifier.isNative(lModifiers)
					|| Modifier.isAbstract(lModifiers)
					|| Modifier.isFinal(lModifiers)) {
				// dont set, continue with next
				continue;
			}

			// do we have something to set in?
			Class<?> lFieldType = lField.getType();

			// handle boxing
			if (lFieldType.isPrimitive()) {
				if (lFieldType == int.class) {
					lFieldType = Integer.class;
				} else if (lFieldType == double.class) {
					lFieldType = Double.class;
				} else if (lFieldType == boolean.class) {
					lFieldType = Boolean.class;
				} else if (lFieldType == long.class) {
					lFieldType = Long.class;
				} else if (lFieldType == float.class) {
					lFieldType = Float.class;
				} else if (lFieldType == char.class) {
					lFieldType = Character.class;
				} else if (lFieldType == byte.class) {
					lFieldType = Byte.class;
				} else if (lFieldType == short.class) {
					lFieldType = Short.class;
				}
			}

			Object lCandidate = fMap.get(lFieldType);
			if (lCandidate == null) {
				// nothing to inject, goto next
				continue;
			}

			boolean lOldAccessible = lField.isAccessible();

			try {
				lField.setAccessible(true);
				lField.set(pObject, lCandidate);
				if (!lOldAccessible) {
					lField.setAccessible(false);
				}
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}

		// descent
		pWire(pObject, pClassLevel.getSuperclass());
	}
}
