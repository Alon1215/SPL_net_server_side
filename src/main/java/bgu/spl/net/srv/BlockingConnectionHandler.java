package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.StompMessagingProtocolImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler implements Runnable, ConnectionHandler<String> {

    private final StompMessagingProtocol protocol;
    private final MessageEncoderDecoder<String> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private int connection_id;
    private Connections<String> connections;


    public void setConnection_id(int connection_id) {
        this.connection_id = connection_id;
    }




    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<String> reader, StompMessagingProtocol protocol, Integer connection_id,Connections<String> connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = (StompMessagingProtocolImpl)protocol;
        this.connection_id = connection_id;
        this.connections = connections;
    }

    @Override
    public void run() {
        protocol.start(connection_id, connections); //TODO: check if cast is ok
        System.out.println("i am a running hander number"+ connection_id);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
            System.out.println("inside try");
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                System.out.println("inside reading message");
                String nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    System.out.println("message read, now begin process");
                    protocol.process(nextMessage);


                    //TODO ALON: 7.1 not valid code for our ass
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
                }
            }
            close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(String msg) {
        byte[] byteMsg = encdec.encode((String)msg);
        try {
            out.write(byteMsg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
