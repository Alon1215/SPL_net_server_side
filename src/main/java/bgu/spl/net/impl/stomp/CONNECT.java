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
        ConnectionsImpl<String> connections = this.protocol.getConnections();
        if(!connections.getUsers().containsKey(loginUser)){
            connections.getUsers().put(loginUser,passcode); //add new user to map
            connections.getActiveUsers().put(loginUser,true);
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)==0 && !connections.getActiveUsers().get(loginUser)){
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)!=0){
            Error e = new Error("","wrong Password","","");
            output = e.execute();
            protocol.setShouldTerminate(true);
        }
        else{
            Error e=new Error("","User already logged in","","");
            output = e.execute();
            protocol.setShouldTerminate(true);
        }
        return output;
    }


}
