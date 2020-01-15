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
        protocol.getConnections().getActiveUsers().replace(protocol.getActiveUsername(),false);
        protocol.setActiveUsername("default");
        protocol.setShouldTerminate(true);
        return "RECEIPT"+'\n'+"receipt-id:"+receipt+'\n'+'\n'+'\u0000';
    }
}
