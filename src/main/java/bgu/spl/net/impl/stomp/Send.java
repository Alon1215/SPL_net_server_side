package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;

public class Send implements Command {
    private String destination;
    private String body;
    private ConnectionsImpl<?> connections;

    public Send(String destination, String body) {
        this.destination = destination;
        this.body = body;
    }

    @Override
    public String execute() {
        return
                "SEND\n" +
                "destination:" + destination + "\n\n" +
                body + "\n" +
                "^@";
    }
}
