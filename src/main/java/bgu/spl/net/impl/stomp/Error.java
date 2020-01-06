package bgu.spl.net.impl.stomp;

public class Error implements Command {
    private int receipt_id;
    private String message;
    private String messageText;
    private String details;

    /**
     * contructor for error msg, to create an error according
     * to the stomp protocol
     * @param receipt_id
     * @param message
     * @param messageText
     * @param details
     */


    public Error(int receipt_id, String message, String messageText, String details) {
        this.receipt_id = receipt_id;
        this.message = message;
        this.messageText = messageText;
        this.details = details;
    }

    @Override

    public String execute() {
        //build error msg according to stomp protocol:
        return "ERROR \n" +
                "receipt-id: message-" +receipt_id + "\n \n" +
                "message: " + message +
                "The message: \n" +
                "-----\n" +
                messageText +
                "\n-----\n" +
                details + "\n^@";
    }
}
