package codeEditor.buffer;


import codeEditor.networkLayer.NetworkCallHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Buffer implements BufferInterface {   
    BlockingQueue buffer = new LinkedBlockingQueue() {};
    NetworkCallHandler requestHandler;
    
    @Override
    public void setHandler(NetworkCallHandler requestHandler){
        this.requestHandler = requestHandler;
    }
    
    @Override
    public void put(Object request) {
        try {
            buffer.put(request);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    
    @Override
    public Object take() {
        try {
            return buffer.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }
}