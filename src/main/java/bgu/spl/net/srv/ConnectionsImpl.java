package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.Map;

public class ConnectionsImpl<T> implements Connections<T>
{
    Map <Integer,ConnectionHandler<T>> handlerMap =new HashMap<>();
    private int nextid=0;

    @Override
    public boolean send(int connectionId, T msg) {
        return false;
    }

    @Override
    public void send(String channel, T msg) {

    }
    public int addhandler(ConnectionHandler<T> hanlder){
        handlerMap.put(nextid,hanlder);
        nextid++;
        return nextid-1;
    }

    @Override
    public void disconnect(int connectionId) {

    }
}
