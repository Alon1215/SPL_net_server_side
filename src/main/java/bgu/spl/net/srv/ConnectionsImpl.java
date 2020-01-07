package bgu.spl.net.srv;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;



public class ConnectionsImpl<T> implements Connections<T>{

    //TODO: ALON 7.1 1100: can you add description to each field? what is the pair & key/value?
    //TODO: check if String or int is the right impl for them
    private HashMap <Integer,ConnectionHandler<T>> handlerMap ;
    private HashMap <String,String> users ; // maps
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
    public boolean removeUserfromTopicmap(int connectionId,String genre){
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
