package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import javafx.util.Pair;

import java.util.LinkedList;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {

    private int connectionId ;
    private ConnectionsImpl<String>  connections;
    private boolean shouldTerminate;
    private LinkedList<Pair<Integer,String>> myTopics; //
    private String activeUsername;

    public String getActiveUsername() {
        return activeUsername;
    }

    public void setShouldTerminate(boolean shouldTerminate) {
        this.shouldTerminate = shouldTerminate;
    }

    public void setActiveUsername(String activeUsername) {
        this.activeUsername = activeUsername;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public ConnectionsImpl<String> getConnections() {
        return connections;
    }

    public boolean isShouldTerminate() {
        return shouldTerminate;
    }

    public LinkedList<Pair<Integer, String>> getMyTopics() {
        return myTopics;
    }

//    public StompMessagingProtocolImpl(int connectionId, ConnectionsImpl<String> connections) {
//        this.connectionId = connectionId;
//        this.connections = connections;
//        this.shouldTerminate = false;
//        this.myTopics = new LinkedList<>();
//        this.activeUsername = de;
//    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl<String>)connections;
        shouldTerminate = false;
        myTopics = new LinkedList<>();
        activeUsername="default";
    }

    @Override
    public void process(String message) {
        String [] parse = message.split(" ");
        String opCode= parse[0];
        String toSend;
        switch (opCode){
            case "CONNECT":
                String  loginUser= parse[3].split(":")[1];
                String  pass= parse[4].split(":")[1];
                Command connect = new CONNECT(loginUser,pass,this);
                toSend = connect.execute();
                connections.send(connectionId,toSend);
                break;

            case "SUBSCRIBE":
                String destination = parse[1].split(":")[1];
                String id = parse[2].split(":")[1];
                String receipt = parse[3].split(":")[1];
                Command subscribe = new Subscribe(destination,Integer.parseInt(id),Integer.parseInt(receipt),this);
                toSend = subscribe.execute();
                connections.send(destination,toSend);
                break;

            case "SEND": //TODO: ALON: 7.1 2000 - is it suppose to be in capital letters? (same for subscribe)
                String destination2 = parse[1].split(":")[1];
                String body = parse[3];
                Command send = new Send(destination2,body,this);
                toSend = send.execute();
                connections.send(destination2,toSend);
                break;

            case "UNSUBSCRIBE":
                String subs_id =  parse[1].split(":")[1];
                Command unsubscribe = new Unsubscribe(subs_id,this);
                toSend = unsubscribe.execute();
                connections.send(connectionId,toSend);
                break;

            case "DISCONNECT":
                shouldTerminate=true;
                String receipt2 = parse[1].split(":")[1];
                Command disconnect = new DISCONNECT(Integer.parseInt(receipt2),this);
                toSend = disconnect.execute();
                connections.disconnect(connectionId);
                connections.send(connectionId,toSend);
                break;
            default: //TODO: Alon impl 7.1 200:00 NOT SURE IF VALID
                Error e = new Error("","message received",message,"Invalid message - unable to process");
                break;


        }



    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
