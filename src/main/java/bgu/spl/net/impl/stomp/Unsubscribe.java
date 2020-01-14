package bgu.spl.net.impl.stomp;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Unsubscribe implements Command {
    private String subscriptionId;
    private StompMessagingProtocolImpl protocol;
    private int receipt_id;

    public Unsubscribe(String subscriptionId, StompMessagingProtocolImpl protocol,int receipt_id){
        this.subscriptionId = subscriptionId;
        this.protocol = protocol;
        this.receipt_id = receipt_id;
    }

    @Override
    public String execute() {
        LinkedList<Pair<Integer,String>> topics = protocol.getMyTopics();
        Pair toRemove=null;
        for(Pair<Integer,String> p:protocol.getMyTopics()){
            if((""+p.getKey()).compareTo(subscriptionId)==0){
                toRemove = p;
                break;
            }
        }
        if(toRemove != null) {
            protocol.getConnections().removeUserfromTopicmap(protocol.getConnectionId(), (String) toRemove.getValue()); //find subscription pair in the topics queue, and remove it
            topics.remove(toRemove); //remove subscription pair from user's topic list
            return "RECEIPT\nreceipt-id:"+receipt_id+ "\n\nSubscription number" + subscriptionId + "Successfully removed\n" +'\u0000'; //TODO:: check if this Frame is ok because instructions don't say what to return
        }
        return "RECEIPT\nreceipt-id:"+receipt_id+ "\n\nuser tried to unsubscribe, not not subscribed, receipt:" +receipt_id+'\n'+  '\u0000';
    }





}



//        ConcurrentLinkedQueue<Pair<Integer,Integer>> subscribers = protocol.getConnections().getTopicMap().get(toRemove.getValue());
//        for(Pair <Integer,Integer> p:subscribers){ //find subscription pair in the topics queue, and remove it
//            if(p.getKey() == protocol.getConnectionId()){
//                subscribers.remove(p);
//                System.out.println("removed subscriber pair from TopicMap queue"); //TODO:print for us, maybe remove later
//            }
//        }
