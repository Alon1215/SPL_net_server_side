package bgu.spl.net.impl.stomp;


public class CONNECT implements Command {

    private String loginUser;
    private String passcode;

    @Override
    public String execute() {
        return null;
    }

    public CONNECT(String loginUser, String passcode) {
        this.loginUser = loginUser;
        this.passcode = passcode;
    }
}
