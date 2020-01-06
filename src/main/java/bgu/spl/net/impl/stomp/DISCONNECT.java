package bgu.spl.net.impl.stomp;

public class DISCONNECT implements Command {
    private int receipt;

    public DISCONNECT(int receipt) {
        this.receipt = receipt;
    }

    @Override
    public String execute() {
        return null;
    }
}
