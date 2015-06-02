# assumptions

## nodes can go down

net nodes can go down, hence the stability of the net relies on the ability to reconnect and catching up.

Since effective processing of updates may happen hours later, calculations and their results  must be asynchronously processed too.

Since nodes may go down in the middle of a processing, there must be a timeout in which a processing must end.

## nodes can move

rotating ip adresses are  a fact, so are moving virtual machines. There must be a means of refinding a peer

## some data still hasto be delivered and/or fast and/or stable with an absolute guarantee

Despite of the inherent instability of the net, there still *is* information that must get distributed along specific nodes with absolute certainity, minimal speed and reliability.

There still is no reason why these guarsantees have to be given for all informations and processes.

## the importance of stability, deliverance and speed is a matter of context

There are contexts where stability, speed and the guarantee of delivery is not an issue at all, while there also might be contexts where it is.

hence the context is of crucial importance th the overall performance of the net.

## end points handle application specific encryption by themselves

the peers only use encryption to provide infrastructure, not to encrypt domain specific application data.

Encryption nowadays is everywhere. There is no reason why to limit an application provider in which encryption it has to use.