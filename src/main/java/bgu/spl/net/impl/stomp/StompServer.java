package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        Server.threadPerClient(
               1234, //port
               () -> new StompMessagingProtocolImpl(), //protocol factory
                ()-> new MessageEncoderDecoderImpl() //message encoder decoder factory
        ).serve();
        //Server.reactor(4,7777,() -> new StompMessagingProtocolImpl(),  ()-> new MessageEncoderDecoderImpl()).serve();


    }


}
