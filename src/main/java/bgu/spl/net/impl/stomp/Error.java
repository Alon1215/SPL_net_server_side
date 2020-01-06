package bgu.spl.net.impl.stomp;

public class Error implements Command {
    private int receipt_id;
    private String message;
    private String messageText;
    private String detalis;


    public Error(int receipt_id, String message, String messageText, String detalis) {
        this.receipt_id = receipt_id;
        this.message = message;
        this.messageText = messageText;
        this.detalis = detalis;
    }

    @Override

    public String execute() {
        return null;
    }
}
