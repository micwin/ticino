# Welcome to the ticino context module!

the ticino context module is java library that adresses the grey-ness of meaning
when programmers talk about context, collections and containers.

In ticino, a _context_ is a container for objects of certain means.

The base class _net.micwin.ticino.context.IContext_ only provides
methods to find out how many elements are in it, how much it can take and when 
it starts to restructure.

To lookup elements you have to have an instance of type 
_net.micwin.ticino.context.IReadableContext_. Looking up an element
 that meets a specific criteria is as simple as writing a lambda that returns 
 _true_ or _false_.

If you want a Context to  successively put elements into, you have to have an
instance of type _net.micwin.ticino.context.IModifyableContext_.

Contexts where you can limit the element to a specific sort of common criteria 
have to implement the interface _net.micwin.ticino.context.IValidatable_.

Currently, there only is one type of context implementation - _net.micwin.ticino.context.DefaultContext_.

	DefaultContext <String> lCtx = new DefaultContext<>(Arrays.asList("A","b","C")) ;
	
	// find all capitals 
	DefaultContext  lAllContainedCapitals = lCtx.lookup ((lElem)->{lElem.equals(lElem.toUpperCase()});  
	
	// find "A"
	boolean lHasA = lCtx.lookup ((lElem) -> "A".equals(lElem) , 1).getCurrentCount ()== 1 ;
	
