package bgu.spl.net.impl.stomp;

public class Send implements Command {
    private String destination;
    private String body;

    public Send(String destination, String body) {
        this.destination = destination;
        this.body = body;
    }

    @Override
    public String execute() {
        return null;
    }
}
