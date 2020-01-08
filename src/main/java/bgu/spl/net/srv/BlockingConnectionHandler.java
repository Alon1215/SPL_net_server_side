package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol protocol;
    private final MessageEncoderDecoder<String> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private int connection_id;
    private Connections<T> connections;


    public void setConnection_id(int connection_id) {
        this.connection_id = connection_id;
    }




    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<String> reader, StompMessagingProtocol protocol, Integer connection_id,Connections<T> connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connection_id = connection_id;
        this.connections = connections;
    }

    @Override
    public void run() {
        protocol.start(connection_id,(Connections<String>) connections); //TODO: check if cast is ok
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                String nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);

                    //TODO ALON: 7.1 not valid code for our ass
//                    if (response != null) {
//                        out.write(encdec.encode(response));
//                        out.flush();
//                    }
                }
            }

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
    public void send(T msg) {
        byte[] byteMsg = encdec.encode((String)msg);
        try {
            out.write(byteMsg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
