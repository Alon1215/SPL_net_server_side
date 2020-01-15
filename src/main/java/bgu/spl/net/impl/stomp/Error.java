package bgu.spl.net.impl.stomp;

public class Error implements Command {
    private String receipt_id;
    private String message;
    private String messageText;
    private String details;
    private StompMessagingProtocolImpl protocol;


    /**
     * contructor for error msg, to create an error according
     * to the stomp protocol
     * @param receipt_id
     * @param message
     * @param messageText
     * @param details
     */


    public Error(String receipt_id, String message, String messageText, String details, StompMessagingProtocolImpl protocol) {
        this.receipt_id = receipt_id;
        this.message = message;
        this.messageText = messageText;
        this.details = details;
        this.protocol = protocol;
    }

    @Override

    public String execute() {
        //build error msg according to stomp protocol:
        protocol.setShouldTerminate(true);
         //TODO: ofer: added to see if error will log me out
        return "ERROR\n" +
                "receipt-id:" +receipt_id + "\n\n" +
                "message: " + message +
                "\nThe message:\n" +
                "-----\n" +
                messageText +
                "\n-----\n" +
                details + '\n'+'\u0000';
    }
}
