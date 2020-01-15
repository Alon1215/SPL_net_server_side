package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;

public class Send implements Command {
    private String destination;
    private String body;
    private StompMessagingProtocolImpl protocol;

    public Send(String destination, String body,StompMessagingProtocolImpl protocol) {
        this.destination = destination;
        this.body = body;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
        return
                "MESSAGE\n" + "destination:"+destination+"\n\n"+ //TODO: ofer:changed send Frame to Message frame, check if message id should be added here
                body + "\n"+'\u0000';                                                                                         //TODO: need to add sub's id for each user when sending to him in connectionsImpl

    }
}
