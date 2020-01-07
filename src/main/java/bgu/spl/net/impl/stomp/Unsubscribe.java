package bgu.spl.net.impl.stomp;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Unsubscribe implements Command {
    private String subscriptionId;
    private StompMessagingProtocolImpl protocol;

    public Unsubscribe(String subscriptionId, StompMessagingProtocolImpl protocol){
        this.subscriptionId = subscriptionId;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
        LinkedList<Pair<Integer,String>> topics = protocol.getMyTopics();
        if(protocol.getActiveUsername().compareTo("default")==0){
            Error e = new Error("","user is not logged in","","no user is logged in,please log in before unsubscribing subscription number "+ subscriptionId);
            protocol.setShouldTerminate(true);  //TODO: check maybe we shouldnt throw error
            return e.execute();
        }
        Pair toRemove=null;
        for(Pair<Integer,String> p:protocol.getMyTopics()){
            if((""+p.getKey()).compareTo(subscriptionId)==0){
                toRemove = p;
                break;
            }
        }
        if(toRemove == null) {
               Error e = new Error("", "user not subscribed", "", "user " + protocol.getActiveUsername() + " tried to unsubscribe subscription "+subscriptionId
             +"but no such subscription exists");
            protocol.setShouldTerminate(true); //TODO: check maybe we shouldnt throw error
                return e.execute();
      }

//        ConcurrentLinkedQueue<Pair<Integer,Integer>> subscribers = protocol.getConnections().getTopicMap().get(toRemove.getValue());
//        for(Pair <Integer,Integer> p:subscribers){ //find subscription pair in the topics queue, and remove it
//            if(p.getKey() == protocol.getConnectionId()){
//                subscribers.remove(p);
//                System.out.println("removed subscriber pair from TopicMap queue"); //TODO:print for us, maybe remove later
//            }
//        }
        protocol.getConnections().removeUserfromTopicmap(protocol.getConnectionId(),(String)toRemove.getValue()); //find subscription pair in the topics queue, and remove it
        topics.remove(toRemove); //remove subscription pair from user's topic list
        return "Subscription number" + subscriptionId+ "Successfully removed" +'\n'+'\n'+'\u0000'; //TODO:: check if this Frame is ok because instructions don't say what to return

    }
}
