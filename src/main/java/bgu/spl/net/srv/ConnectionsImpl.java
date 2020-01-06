package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl<T> implements Connections<T>
{
    Map <Integer,ConnectionHandler<T>> handlerMap =new HashMap<>();
    Map <String,String> users = new HashMap<>();
    Map <String,Boolean> activeUsers = new HashMap<>();
    ConcurrentHashMap<String, ConcurrentLinkedQueue > topicMap = new ConcurrentHashMap<>();
    AtomicInteger messageId = new AtomicInteger(0);

    private int nextid=0;

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
