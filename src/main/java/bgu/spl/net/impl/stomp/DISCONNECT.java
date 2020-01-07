package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImpl;

public class DISCONNECT implements Command {
    private int receipt;
    private StompMessagingProtocolImpl protocol;

    public DISCONNECT(int receipt, StompMessagingProtocolImpl protocol) {
        this.receipt = receipt;
        this.protocol = protocol;
    }

    @Override
    public String execute() {
        if(protocol.getActiveUsername().compareTo("default")==0){
           Error e = new Error("","user is not logged in","","tried to logout but no user is logged in " );
           protocol.setShouldTerminate(true);   //TODO: check maybe we shouldnt throw error
           return e.execute();
       }


        return null;
    }
}
