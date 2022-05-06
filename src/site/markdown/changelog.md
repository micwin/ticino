# Changelog

## 0.9.0

- removed *.context and *.global without replacement (yet)

## 0.4.0 

- moving over to io.metafence.ticino

- removed *.context.IReadableContext and *.context.IModifyableContext without replacement (yet)

## 0.3.7

- public (static) scopes (see _ticino-static_ class net.micwin.ticino.events.PublicScopes)

- ticino.events.EventScope gets method_ dispatchNoMatterWhat_ that reaches all receiver no matter if a receiver before threw an exception.


### open issues

- EventScope.dispatch -> dispatchVote
- EventScope.dispatchNoMatterWhat -> dispatchAll
- EventScope.registerDispatchListener or else


## 0.3.6 

- removed some package issues
- documentation for ticino context

## 0.3.5 

- Cleanup
- introduced ticino context
- gone java 1.8

