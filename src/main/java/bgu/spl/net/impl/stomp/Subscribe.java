package bgu.spl.net.impl.stomp;

import javafx.util.Pair;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Subscribe implements Command {

    private String destination;
    private int id;
    private int receipt;
    private StompMessagingProtocolImpl protocol;

    public Subscribe(String destination, int id, int receipt, StompMessagingProtocolImpl protocol) {
        this.destination = destination;
        this.id = id;
        this.receipt = receipt;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
        if(protocol.getActiveUsername().compareTo("default")==0){
            Error e = new Error(""+receipt,"user is not logged in","","no user is logged in,please log in before subscribing to genere "+destination );
            protocol.setShouldTerminate(true);
            return e.execute();
        }
        for(Pair<Integer,String> p:protocol.getMyTopics()){
            if(p.getValue().compareTo(destination)==0){
                Error e= new Error(""+receipt,"user already subscribed","","user "+protocol.getActiveUsername()+" tried to subscribe to genre "+ destination + " but already subscribed");
                protocol.setShouldTerminate(true);
                return e.execute();

            }
        }
        Pair pairToAdd = new Pair (id,destination);
        protocol.getMyTopics().push(pairToAdd); // add to users topic list
        protocol.getConnections().getTopicMap().putIfAbsent(destination,new ConcurrentLinkedQueue<>());
        protocol.getConnections().getTopicMap().get(destination).add(pairToAdd);
        return ("RECEIPT"+'\n'+"receipt-id:"+id+'\n'+'\n'+'\u0000');

    }
}
