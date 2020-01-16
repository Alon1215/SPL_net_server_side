package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        short port = Short.parseShort(args[0]); //extract port from args
        String serverType = args[1]; // extract type of server

        if (serverType.equals("tpc")) {
            Server.threadPerClient(
                    port, //port
                    () -> new StompMessagingProtocolImpl(), //protocol factory
                    () -> new MessageEncoderDecoderImpl() //message encoder decoder factory
            ).serve();
        } else { //args[1] == reactor
            Server.reactor(4, port, () -> new StompMessagingProtocolImpl(), () -> new MessageEncoderDecoderImpl()).serve();
        }

    }


}
