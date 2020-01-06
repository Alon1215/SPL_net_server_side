package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;

public class DISCONNECT implements Command {
    private int receipt;
    private ConnectionsImpl connections;

    public DISCONNECT(int receipt, ConnectionsImpl connections) {
        this.receipt = receipt;
        this.connections = connections;
    }

    @Override
    public String execute() {
        return null;
    }
}
