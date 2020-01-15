package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;
import javafx.util.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DISCONNECT implements Command {
    private int receipt;
    private StompMessagingProtocolImpl protocol;

    public DISCONNECT(int receipt, StompMessagingProtocolImpl protocol) {
        this.receipt = receipt;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
//        if(protocol.getActiveUsername().compareTo("default")==0){ //TODO:ofer: think this error is not needed
//           Error e = new Error("","user is not logged in","","tried to logout but no user is logged in " ,protocol);
//           return e.execute();
//       }
        ConnectionsImpl connections = protocol.getConnections();
        int connectId = protocol.getConnectionId();
        for(Pair<Integer,String> topic :protocol.getMyTopics()){ //delete all subscriptions from topic map
            connections.removeUserfromTopicmap(connectId,topic.getValue());
        }

        protocol.getConnections().getActiveUsers().replace(protocol.getActiveUsername(),false);
        protocol.setActiveUsername("default");
        protocol.setShouldTerminate(true);
        return "RECEIPT"+'\n'+"receipt-id:"+receipt+'\n'+'\n'+'\u0000';
    }
}
