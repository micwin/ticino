# Changelog


## Version 0.3.4

* ticino events now also work asynchronously.

* reorganized project. ticino now becomes a container for multiple, simplicity targeted aspects, while the events part now is called _ticino events_. 

## Version 0.3.3

  * changed to the more liberal MIT License

  * added EventScope.unregisterListener to unregister listeners

  * added a class parameter to the constructor to check more precisely event types at runtime.

  * gave the method EventScope.register an additional regex pattern parameter to better define 
    the handler method. This enables e.g. LinkedList to receive ticino events.

  * removed spring module again - not satisfying 

## Version 0.3.2

  * enabled chaining when dispatching an event
  
  * enabled chaining when registering a listener
  
  * moved static context Ticino to sub project ticino-static
  
  * ticino-spring: Configurator now not connecting with static Ticino context; instead now in need of a EventScope as 
    constructor param. In consequence, you can use multiple configurators to configure multiple event scopes.

## Version 0.3.1

  * Introduced EventScope
  
  * example code module

  * renamed to ticino

## Version 0.3

  * event inheritance
  
  * spring support
  
## Version 0.2.1

  * finally going to maven central repository