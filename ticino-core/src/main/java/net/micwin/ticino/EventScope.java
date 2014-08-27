
package net.micwin.ticino;


import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * A eventScope in which an event should get dispatched and receivers should register to their specified events..
 * 
 * @author MicWin
 * 
 */
public class EventScope<T> {

    Map<Class<? extends T>, List<ReceiverDescriptor>> receiverMap = new HashMap<Class<? extends T>, List<ReceiverDescriptor>>();
    private final Class<T> baseClass;

    /**
     * Creates a eventScope with given name.
     * 
     * @param pBaseClass
     */
    public EventScope(final Class<T> pBaseClass) {
        this.baseClass = pBaseClass;
    }

    /**
     * Holds a soft reference to a receiver. This will not prevent the receiver to get garbage collected, so make sure
     * to store the receiver elsewhere, for example you could pass in a spring bean.
     * 
     * @author MicWin
     * 
     */
    private class ReceiverDescriptor {
        SoftReference<Object> receiverReference;
        Method method;
        long creationTime = System.currentTimeMillis();
    }

    /**
     * The public method to register a new event receiver.
     * 
     * @param eventClass the class of event to be received.
     * @param receiver The receiver to get the event.
     * @return this to enable chaining.
     */
    public synchronized EventScope<T> register(final Class<? extends T> eventClass, final Object receiver) {
        return this.register(eventClass, receiver, (Pattern) null);

    }

    /**
     * The public method to register a new event receiver.
     * 
     * @param eventClass the class of event to be received.
     * @param receiver The receiver to get the event.
     * @param handler handler method to handle specified event.
     * @return this to enable chaining.
     */
    public synchronized EventScope<T> register(final Class<? extends T> eventClass, final Object receiver,
            final Method handler) {
        this.registerInternal(receiver, handler, eventClass);
        return this;

    }

    /**
     * The public method to register a new event receiver.
     * 
     * @param eventClass the class of event to be received.
     * @param receiver The receiver to get the event.
     * @param namePattern a name pattern for the method used as handler.
     * @return this to enable chaining.
     */
    public synchronized EventScope<T> register(final Class<? extends T> eventClass, final Object receiver,
            final Pattern namePattern) {
        final Method method = this.detectReceiverMethod(receiver, eventClass, namePattern);
        this.registerInternal(receiver, method, eventClass);
        return this;
    }

    /**
     * Registers a receiver to an internal map.
     * 
     * @param receiver
     * @param method
     * @param eventClass
     */
    private void registerInternal(final Object receiver, final Method method, final Class<? extends T> eventClass) {
        List<ReceiverDescriptor> receivers = this.receiverMap.get(eventClass);
        if (receivers == null) {
            receivers = new LinkedList<ReceiverDescriptor>();
            this.receiverMap.put(eventClass, receivers);
        }

        final ReceiverDescriptor rd = new ReceiverDescriptor();
        rd.receiverReference = new SoftReference<Object>(receiver);
        rd.method = method;

        receivers.add(rd);
    }

    /**
     * Analyzes the receiver for Methods that satisfy to receive events of type <code>eventClass</code>.
     * 
     * @param receiver the receiver
     * @param eventClass the event class
     * @param namePattern Optional. A method name regex pattern to better select the handler method.
     * @return
     */
    private Method detectReceiverMethod(final Object receiver, final Class<? extends T> eventClass,
            final Pattern namePattern) {
        final Method[] methods = receiver.getClass().getMethods();
        final List<Method> results = new LinkedList<Method>();

        for (final Method method : methods) {

            if (method.getParameterTypes() == null || method.getParameterTypes().length != 1) {
                // wrong amount of parameters -> try next
                continue;
            }

            if (namePattern != null && !namePattern.matcher(method.getName()).find()) {
                // wrong method name - try next
                continue;
            }

            if (this.canHandle(method, eventClass)) {
                // hit!
                results.add(method);
            }
        }

        if (results.size() == 1) {
            // only one hit - perfect
            return results.get(0);
        }

        if (results.size() > 1) {
            throw new IllegalArgumentException("receiver '" + receiver
                    + "' has more than one accessible method with a single parameter of type '" + eventClass.getName()
                    + "' " + (namePattern != null ? " and name pattern '" + namePattern.pattern() + "' " : "") + ": "
                    + results.toString());
        }

        // no hit _> exception
        throw new IllegalArgumentException("receiver '" + receiver
                + "' does not have an accessible method with a single parameter of type '" + eventClass.getName()
                + "' " + (namePattern != null ? " and name pattern '" + namePattern.pattern() + "' " : ""));

    }

    public boolean canHandle(final Method method, final Class<? extends T> eventClass) {
        return method.getParameterTypes()[0].isAssignableFrom(eventClass) && !"equals".equals(method.getName());
    }

    /**
     * Dispatch an event to receivers.
     * 
     * @param event The event object to dispatch.
     */
    public synchronized <Q extends T> Q dispatch(final Q event) {

        final Collection<ReceiverDescriptor> receivers = new TreeSet<ReceiverDescriptor>(
                new Comparator<ReceiverDescriptor>() {

                    @Override
                    public int compare(final ReceiverDescriptor o1, final ReceiverDescriptor o2) {

                        // direct check descriptor
                        if (o1 == o2) {
                            return 0;
                        }

                        final boolean o1IsNull = o1 == null || o1.receiverReference == null
                                || o1.receiverReference.get() == null;
                        final boolean o2IsNull = o2 == null || o2.receiverReference == null
                                || o2.receiverReference.get() == null;

                        if (o1IsNull && o2IsNull) {
                            return 0;
                        }

                        if (o1IsNull) {
                            return 1;
                        }

                        if (o2IsNull) {
                            return -1;
                        }

                        // direct check receiverReference
                        if (o1.receiverReference == o2.receiverReference) {
                            return 0;
                        }

                        // direct check object
                        if (o1.receiverReference.get() == o2.receiverReference.get()) {
                            return 0;
                        }

                        // check creationDate
                        if (o1.creationTime < o2.creationTime) {
                            return -1;
                        }
                        else if (o1.creationTime > o2.creationTime) {
                            return 1;
                        }

                        // when coming to this point, we really have a problem
                        // to *order* the calls by registration.
                        // so we assume: if two registration happen in the same
                        // millisecond, then they are anonymous adapters

                        final int classComparison = o1.receiverReference.get().getClass().getName()
                                .compareTo(o2.receiverReference.get().getClass().getName());

                        if (classComparison != 0) {
                            return classComparison;
                        }

                        // same class

                        // check wether we can use Comparable

                        try {

                            final Comparable c1 = (Comparable) o1.receiverReference.get();
                            final Comparable c2 = (Comparable) o2.receiverReference.get();
                            final int comparison = c1.compareTo(c2);
                            if (comparison != 0) {
                                return comparison;
                            }

                        }
                        catch (final ClassCastException e) {
                            // no, not a comparable
                        }

                        // same class, so we try to differentiate using the hash
                        // code
                        final Integer h1 = o1.receiverReference.get().hashCode();
                        final Integer h2 = o2.receiverReference.get().hashCode();

                        return h1.compareTo(h2);
                    }
                });

        this.collectReceiver(event.getClass(), receivers);

        // no receivers registered, so bye-bye
        if (receivers == null || receivers.size() < 1) {
            return event;
        }

        // when finding a garbage collected receiver, store here
        final List<ReceiverDescriptor> defunct = new LinkedList<ReceiverDescriptor>();

        try {
            for (final ReceiverDescriptor receiverDescriptor : receivers) {
                final Object receiver = receiverDescriptor.receiverReference.get();
                if (receiver == null) {

                    defunct.add(receiverDescriptor);
                    continue;
                }
                try {
                    receiverDescriptor.method.setAccessible(true);
                    receiverDescriptor.method.invoke(receiver, event);
                }
                catch (final Exception e) {
                    // wrap non runtime exception
                    throw new DispatchException(receiver, event, e);
                }
            }
        }
        finally {
            // throw away gc'ed items
            receivers.removeAll(defunct);
        }
        return event;

    }

    /**
     * collects receiver descriptors of super classes and interfaces.
     * 
     * @param eventClass
     * @param receiverCollection the collection receivers are put in.
     */
    private void collectReceiver(final Class<?> eventClass, final Collection<ReceiverDescriptor> receiverCollection) {
        if (this.receiverMap.get(eventClass) != null) {
            receiverCollection.addAll(this.receiverMap.get(eventClass));
        }
        if (!eventClass.isInterface() && eventClass.getSuperclass() != Object.class) {
            this.collectReceiver(eventClass.getSuperclass(), receiverCollection);
        }
        for (final Class interfaceClass : eventClass.getInterfaces()) {
            this.collectReceiver(interfaceClass, receiverCollection);
        }
    }

    public Class<T> getBaseClass() {

        return this.baseClass;
    }

}
