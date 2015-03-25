# best practices

* **Dont use basic types as events** : Althougt ticino allows to use ie java.lang.String or java.lang.Integer as event types, we strongly discourage this approach. In general, use event whose names describe the purpose of the event, or its inner structure. This said, what's the purpose of dispatching a String?
    
* **Name your events according to their domain purpose** : Since it is a technical driven naming, IncomingXmlEvent from Example 1 is a discouraged naming for an event. If possible, give more insight into the data provided with this event. In case of _IncomingXmlEvent_ you probably might say ie _UpdateFlightData_ instead.
    
* **Be careful what you dispatch** : Since ticino senders have no meanings to control who catches their events, you probably 
should avoid including such secret things like passwords etc in your events. When using multiple event scopes, use the base type constructor to better control which events are fired on which event scope. Also, since they mostly come along with a whole bunch of references, you should think twice including database entities, transaction managers, EJBs, session data et al in your events. It probably might be most 
useful and of a specific charm throwing a NewRequestEvent, a CreateUserEvent or a LoadMandateEvent 
as long the drawbacks are well considered.
	
* **use a ci container like spring or cdi** : Using spring to connect dispatcher and receiver enforces the loose end characteristics of ticino and hence is highly recommended. Of course you might use another DI or IOC framework as well. Although the spring integration is on rework with 0.3.3, you surely can define beans from event scopes and register listeners in an init method.