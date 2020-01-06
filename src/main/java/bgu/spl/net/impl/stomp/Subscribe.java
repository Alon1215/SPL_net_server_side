package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;

public class Subscribe implements Command {

    private String destination;
    private int id;
    private int receipt;
    private ConnectionsImpl connections;

    public Subscribe(String destination, int id, int receipt, ConnectionsImpl connections) {
        this.destination = destination;
        this.id = id;
        this.receipt = receipt;
        this.connections = connections;
    }

    @Override
    public String execute() {
        return null;
    }
}
