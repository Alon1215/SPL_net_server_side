# SPL_net_server_side
A books-club platform, reactor based server - client project, based on STOMP protocol.

Project in System Programming course ( in Ben Gurion University) , writing Reactor server in java, and client side in C++.
a tpc server is also provided, and can be chosen as an argument while running the server.
the 2 commands for running the server (via terminal):
* Thread per client server: ` mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -
Dexec.args="<port> tpc’
* Reactor server: ` mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -
Dexec.args="<port> reactor’





