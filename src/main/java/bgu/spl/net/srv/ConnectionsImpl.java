package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.MessageEncoderDecoderImpl;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;



public class ConnectionsImpl implements Connections<String>{

    //TODO: ALON 7.1 1100: can you add description to each field? what is the pair & key/value?
    //TODO: check if String or int is the right impl for them
    private ConcurrentHashMap <Integer,ConnectionHandler<String>> handlerMap ; // 1st = connection id,  2nd = connection handle
    private ConcurrentHashMap <Integer,String> connectIdtoNameMap; //1st = connectId, 2nd = UserName
    private ConcurrentHashMap <String, String> users ; // 1st = userName , 2nd = password
    private ConcurrentHashMap <String,Boolean> activeUsers; // 1st = userName , 2nd = isActive
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer,Integer>>> topicMap ; //maps Topic to: queue of pairs: <1st = connection id , 2nd = sub's id> (important)
    private AtomicInteger messageId ;


    public ConnectionsImpl(){
        handlerMap = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
        activeUsers = new ConcurrentHashMap<>();
        topicMap = new ConcurrentHashMap<>();
        messageId = new AtomicInteger(0);
        connectIdtoNameMap = new ConcurrentHashMap<>();
    }


    public ConcurrentHashMap<Integer, ConnectionHandler<String>> getHandlerMap() {
        return handlerMap;
    }

    public ConcurrentHashMap<String, String> getUsers() {
        return users;
    }

    public ConcurrentHashMap<String, Boolean> getActiveUsers() {
        return activeUsers;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer, Integer>>> getTopicMap() {
        return topicMap;
    }

    public int getMessageId() {
        return messageId.getAndIncrement();
    }


    @Override
    public boolean send(int connectionId, String msg) {
        if(!handlerMap.containsKey(connectionId))
            return false;
        handlerMap.get(connectionId).send(msg);
        return true; //TODO: check why we should return boolean
    }

    @Override
    public void send(String channel, String msg) {
        String[] parser = msg.split("\n"); //
        topicMap.putIfAbsent(channel,new ConcurrentLinkedQueue<>());
        for (Pair<Integer,Integer> p : topicMap.get(channel) ){
            String toSend = parser[0]+"\n"+"subscription:"+p.getValue()+"\n"+parser[1]+"\n"+parser[2]+"\n\n"+parser[4]+"\n"+"\u0000"; //make personal msg with sub's id
            send(p.getKey(),  toSend); // send using send method by connectId
        }
    }
    public void addHandler(ConnectionHandler handler,Integer connectionId){
        handlerMap.put(connectionId,handler);
    }

    @Override
    public void disconnect(int connectionId) {
        if(connectIdtoNameMap.contains(connectionId)) {
            String user_to_log_out = connectIdtoNameMap.get(connectionId);
            if (activeUsers.contains(user_to_log_out))
                activeUsers.replace(user_to_log_out, false);
            if (activeUsers.contains(user_to_log_out)) {
                activeUsers.replace(user_to_log_out, false);
            }
        }
        handlerMap.remove(connectionId);

    }

    public ConcurrentHashMap<Integer, String> getConnectIdtoNameMap() {
        return connectIdtoNameMap;
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
