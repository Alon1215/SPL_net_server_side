package bgu.spl.net.impl.stomp;

public class Subscribe implements Command {

    private String destination;
    private int id;
    private int receipt;

    public Subscribe(String destination, int id, int receipt) {
        this.destination = destination;
        this.id = id;
        this.receipt = receipt;
    }

    @Override
    public String execute() {
        return null;
    }
}
