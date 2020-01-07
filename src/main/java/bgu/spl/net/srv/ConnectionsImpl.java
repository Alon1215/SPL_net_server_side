package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.MessageEncoderDecoderImpl;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;



public class ConnectionsImpl<T> implements Connections<T>{

    //TODO: ALON 7.1 1100: can you add description to each field? what is the pair & key/value?
    //TODO: check if String or int is the right impl for them
    private HashMap <Integer,ConnectionHandler<String>> handlerMap ;
    private HashMap <String, java.lang.String> users ; // maps
    private HashMap <String,Boolean> activeUsers;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer,Integer>> > topicMap ; //maps Topic to: queue of pairs: <1st = connection id , 2nd = sub's id> (important)
    private AtomicInteger messageId ;


    public ConnectionsImpl(){
        handlerMap = new HashMap <>();
        users = new HashMap<>();
        activeUsers = new HashMap<>();
        topicMap = new ConcurrentHashMap<>();
        messageId = new AtomicInteger(0);
    }


    public HashMap<Integer, ConnectionHandler<String>> getHandlerMap() {
        return handlerMap;
    }

    public HashMap<java.lang.String, java.lang.String> getUsers() {
        return users;
    }

    public HashMap<java.lang.String, Boolean> getActiveUsers() {
        return activeUsers;
    }

    public ConcurrentHashMap<java.lang.String, ConcurrentLinkedQueue<Pair<Integer, Integer>>> getTopicMap() {
        return topicMap;
    }

    public int getMessageId() {
        return messageId.getAndIncrement();
    }


    @Override
    public boolean send(int connectionId, T msg) {
        if(!handlerMap.containsKey(connectionId))
            return false;
        handlerMap.get(connectionId).send(msg);
        return true; //TODO: check why we should return boolean
    }

    @Override
    public void send(String channel, T msg) {
        java.lang.String[] parser = ((String)msg).split("\n"); //TODO: check if OK to assume T is String / OK to change T to String in this ConnectionsImpl class
        for (Pair<Integer,Integer> p : topicMap.get(channel) ){
            ConnectionHandler<String> ch = handlerMap.get(p.getKey());
            MessageEncoderDecoderImpl med = new MessageEncoderDecoderImpl();
            String toSend = parser[0]+"\n"+"subscription:"+p.getValue()+"\n"+parser[1]+"\n"+parser[2]+"\n\n"+parser[3]+"\n"+"\u0000";
            send(p.getKey(),toSend);
        }
    }
    public void addHandler(ConnectionHandler handler,Integer connectionId){
        handlerMap.put(connectionId,handler);
    }

    @Override
    public void disconnect(int connectionId) {
        handlerMap.remove(connectionId);

    }
    public boolean removeUserfromTopicmap(int connectionId, String genre){
        ConcurrentLinkedQueue<Pair<Integer,Integer>> subscribers =topicMap.get(genre);
        for(Pair <Integer,Integer> p:subscribers){ //find subscription pair in the topics queue, and remove it
            if(p.getKey() == connectionId){
                subscribers.remove(p);
                System.out.println("removed subscriber pair from TopicMap queue"); //TODO:print for us, maybe remove later
                return true;
            }
        }
        return false;
    }
}
