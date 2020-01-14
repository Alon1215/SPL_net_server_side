package bgu.spl.net.impl.stomp;

import javafx.util.Pair;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Subscribe implements Command {

    private String destination;
    private int subs_id;
    private int receipt_num;
    private StompMessagingProtocolImpl protocol;

    public Subscribe(String destination, int id, int receipt, StompMessagingProtocolImpl protocol) {
        this.destination = destination;
        this.subs_id = id;
        this.receipt_num = receipt;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
        if(protocol.getActiveUsername().compareTo("default")==0){
            Error e = new Error(""+ receipt_num,"user is not logged in","","no user is logged in,please log in before subscribing to genere "+destination,protocol );
            protocol.setShouldTerminate(true);
            return e.execute();
        }
        for(Pair<Integer,String> p:protocol.getMyTopics()){
            if(p.getValue().compareTo(destination)==0){
                return ("RECEIPT"+'\n'+"receipt-id:"+receipt_num+'\n'+'\n'+'\u0000');
            }
        }
        //
        Pair<Integer,String> pairToAdd = new Pair<> (subs_id,destination);
        protocol.getMyTopics().push(pairToAdd); // add to users topic list
        protocol.getConnections().getTopicMap().putIfAbsent(destination,new ConcurrentLinkedQueue<>());
        protocol.getConnections().getTopicMap().get(destination).add(new Pair(protocol.getConnectionId(), subs_id));
        return ("RECEIPT"+'\n'+"receipt-id:"+ receipt_num +'\n'+'\n'+'\u0000');

    }
}
