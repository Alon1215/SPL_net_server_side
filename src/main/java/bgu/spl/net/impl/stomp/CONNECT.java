package bgu.spl.net.impl.stomp;


import bgu.spl.net.srv.ConnectionsImpl;

public class CONNECT implements Command {

    private String loginUser;
    private String passcode;
    private ConnectionsImpl<String> connections;
    public CONNECT(String loginUser, String passcode,ConnectionsImpl<String> connections) {
        this.loginUser = loginUser;
        this.passcode = passcode;
        this.connections = connections;
    }
    @Override
    public String execute() {
        String output;
        if(!connections.getUsers().containsKey(loginUser)){
            connections.getUsers().put(loginUser,passcode); //add new user to map
            connections.getActiveUsers().put(loginUser,true);
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)==0 && !connections.getActiveUsers().get(loginUser)){
            output="CONNECTED"+'\n'+"version:1.2"+'\n'+'\n'+'\u0000';
        }
        else if(connections.getUsers().get(loginUser).compareTo(passcode)!=0){
            Error e = new Error(-1,"wrong Password","","");
            output = e.execute();

        }
        else{
            Error e=new Error(-1,"User already logged in","","");
            output = e.execute();
        }
        return output;
    }


}
