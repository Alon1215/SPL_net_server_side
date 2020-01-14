package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.NonBlockingConnectionHandler;
import javafx.util.Pair;

import java.util.LinkedList;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {

    private int connectionId ;
    private ConnectionsImpl  connections;
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

    public ConnectionsImpl getConnections() {
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
        this.connections = (ConnectionsImpl)connections;
        shouldTerminate = false;
        myTopics = new LinkedList<>();
        activeUsername="default";
    }

    @Override
    public void process(String message) {
        System.out.println("inside proccess! "+message);
        String [] parse = message.split("\n");
        String opCode= parse[0];
        String toSend;
        switch (opCode){
            case "CONNECT": {
                System.out.println("proccesing msg of type CONNECT");
                String loginUser = parse[3].split(":")[1];
                String pass = parse[4].split(":")[1];
                Command connect = new CONNECT(loginUser, pass, this);
                toSend = connect.execute();
                connections.send(connectionId, toSend);
                break;
            }
            case "SUBSCRIBE": {
                System.out.println("proccesing msg of type Subscribe");
                String destination = parse[1].split(":")[1];
                String id = parse[2].split(":")[1];
                String receipt = parse[3].split(":")[1];
                Command subscribe = new Subscribe(destination, Integer.parseInt(id), Integer.parseInt(receipt), this);
                toSend = subscribe.execute();
                connections.send(destination, toSend);
                break;
            }
            case "SEND": { //TODO: ALON: 7.1 2000 - is it suppose to be in capital letters? (same for subscribe)
                System.out.println("proccesing msg of type Send");
                String destination2 = parse[1].split(":")[1];
                String body = parse[3];
                Command send = new Send(destination2, body, this);
                toSend = send.execute();
                connections.send(destination2, toSend);
                break;
            }
            case "UNSUBSCRIBE": {
                String subs_id = parse[1].split(":")[1];
                String receipt = parse[2].split(":")[1]; //TODO:: make sure it all good with the location
                Command unsubscribe = new Unsubscribe(subs_id, this,Integer.parseInt(receipt));
                toSend = unsubscribe.execute();
                connections.send(connectionId, toSend);
                break;
            }
            case "DISCONNECT": {
                System.out.println("proccesing msg of type disconnect");
                shouldTerminate = true;
                String receipt2 = parse[1].split(":")[1];
                Command disconnect = new DISCONNECT(Integer.parseInt(receipt2), this);
                toSend = disconnect.execute();
                connections.disconnect(connectionId); //TODO: Ofer: check if ok
                connections.send(connectionId, toSend);

                break;
            }
            default: {//TODO: Alon impl 7.1 200:00 NOT SURE IF VALID
                System.out.println("proccesing msg of type default case - invalid msg recieved -> " + (message.equals("\n") + " " + message.equals("")));
                break;
            }
        }



    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
