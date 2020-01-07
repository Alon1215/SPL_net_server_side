package bgu.spl.net.srv;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;



public class ConnectionsImpl<T> implements Connections<T>{

    //TODO: ALON 7.1 1100: can you add description to each field? what is the pair & key/value?
    //TODO: check if String or int is the right impl for them
    HashMap <Integer,ConnectionHandler<T>> handlerMap ;
    HashMap <String,String> users ;
    HashMap <String,Boolean> activeUsers;
    ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer,Integer>> > topicMap ; //1st = my id , 2nd = sub's id (important)
    AtomicInteger messageId ;


    public ConnectionsImpl(){
        handlerMap = new HashMap <>();
        users = new HashMap<>();
        activeUsers = new HashMap<>();
        topicMap = new ConcurrentHashMap<>();
        messageId = new AtomicInteger(0);
    }

    private int nextid=0;

    public HashMap<Integer, ConnectionHandler<T>> getHandlerMap() {
        return handlerMap;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public HashMap<String, Boolean> getActiveUsers() {
        return activeUsers;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer, Integer>>> getTopicMap() {
        return topicMap;
    }

    public int getMessageId() {
        return messageId.getAndIncrement();
    }


    @Override
    public boolean send(int connectionId, T msg) {



        return false;
    }

    @Override
    public void send(String channel, T msg) {
        for (Pair<Integer,Integer> idToSubId : topicMap.get(channel) ){

        }

    }
    public int addHandler(ConnectionHandler<T> handler){
        handlerMap.put(nextid,handler);
        nextid++;
        return nextid-1;
    }

    @Override
    public void disconnect(int connectionId) {

    }
}
