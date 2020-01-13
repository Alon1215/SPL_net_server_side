package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<StompMessagingProtocol> protocolFactory;
    private final Supplier<MessageEncoderDecoder<String>> encdecFactory;
    private ServerSocket sock;
    private ConnectionsImpl connections= new ConnectionsImpl();
    private int connectIdcount;

    public BaseServer(
            int port,
            Supplier<StompMessagingProtocol> protocolFactory,
            Supplier<MessageEncoderDecoder<String>> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
		connectIdcount=0;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();
                System.out.println("connection established");

                BlockingConnectionHandler handler = new BlockingConnectionHandler(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get(),connectIdcount,connections);
                System.out.println("created hander with connect id "+ connectIdcount);
                //our added impl:
                connections.addHandler(handler,connectIdcount);
                connectIdcount++;
                execute(handler);
            }
            close();

        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    protected abstract void execute(BlockingConnectionHandler handler);

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

 


}
