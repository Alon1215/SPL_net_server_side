package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {
    private int connectionId ;
    private ConnectionsImpl <String> connections;
    private boolean shouldTerminate;
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl)connections;
        shouldTerminate = false;
    }

    @Override
    public void process(String message) {
        String [] parse = message.split(" ");
        String opCode= parse[0];
        String toSend;
        switch (opCode){
            case "CONNECT":
                String  loginuser= parse[3].split(":")[1];
                String  pass= parse[4].split(":")[1];
                Command connect = new CONNECT(loginuser,pass,connections);
                toSend = connect.execute();
                connections.send(connectionId,toSend);
                break;
            case "Subscribe":
                String destination = parse[1].split(":")[1];
                String id = parse[2].split(":")[1];
                String receipt = parse[3].split(":")[1];
                Command subscribe = new Subscribe(destination,Integer.parseInt(id),Integer.parseInt(receipt),connections);
                toSend = subscribe.execute();
                connections.send(destination,toSend);
                break;
            case "Send":
                String destination2 = parse[1].split(":")[1];
                String body = parse[3];
                Command send = new Send(destination2,body);
                toSend = send.execute();
                connections.send(destination2,toSend);
                break;
            case "DISCONNECT":
                String receipt2 = parse[1].split(":")[1];
                Command disconnect = new DISCONNECT(Integer.parseInt(receipt2),connections);
                toSend = disconnect.execute();
                connections.send(connectionId,toSend);
        }



    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
