package bgu.spl.net.impl.stomp;


import bgu.spl.net.srv.ConnectionsImpl;

public class CONNECT implements Command {

    private String loginUser;
    private String passcode;
    private StompMessagingProtocolImpl protocol;
    public CONNECT(String loginUser, String passcode,StompMessagingProtocolImpl protocol) {
        this.loginUser = loginUser;
        this.passcode = passcode;
        this.protocol = protocol;
    }
    @Override
    public String execute() {
        String output;
        ConnectionsImpl connections = this.protocol.getConnections();
        if(!connections.getUsers().containsKey(loginUser)){
            connections.getUsers().put(loginUser,passcode); //add new user to map
            connections.getActiveUsers().put(loginUser,true);
            connections.getConnectIdtoNameMap().put(protocol.getConnectionId(),loginUser);
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
            protocol.setActiveUsername(loginUser);
            add_or_replace_to_ConnectIdtoUserMap();
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)==0 && !connections.getActiveUsers().get(loginUser)){ //connected to exsiting user
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
            protocol.setActiveUsername(loginUser);
            add_or_replace_to_ConnectIdtoUserMap();
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)!=0){
            Error e = new Error("","wrong Password","","",protocol); //TODO ALON 7.1: fix details and msg? (same for next else)
            output = e.execute();
        }
        else{
            Error e = new Error("","User already logged in","","",protocol);
            output = e.execute();
        }
        return output;
    }

    void add_or_replace_to_ConnectIdtoUserMap(){
        if(!protocol.getConnections().getConnectIdtoNameMap().contains(protocol.getConnectionId()))
            protocol.getConnections().getConnectIdtoNameMap().put(protocol.getConnectionId(),loginUser);
        else
            protocol.getConnections().getConnectIdtoNameMap().replace(protocol.getConnectionId(),loginUser);
    }


}
