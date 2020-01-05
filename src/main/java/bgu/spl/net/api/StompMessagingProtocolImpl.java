package bgu.spl.net.api;

import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {
    private int connectionId ;
    private Connections<String>connections;
    private boolean shouldTerminate;
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }

    @Override
    public void process(String message) {

        //connections.send(connectionId,output);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
