package bgu.spl.net.srv;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl<T> implements Connections<T>
{
    HashMap <Integer,ConnectionHandler<T>> handlerMap ;
    HashMap <String,String> users ;
    HashMap <String,Boolean> activeUsers;
    ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<String,String>> > topicMap ;
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

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<String, String>>> getTopicMap() {
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

    }
    public int addhandler(ConnectionHandler<T> hanlder){
        handlerMap.put(nextid,hanlder);
        nextid++;
        return nextid-1;
    }

    @Override
    public void disconnect(int connectionId) {

    }
}
