package codeEditor.networkLayer;

import codeEditor.buffer.BufferInterface;
import java.io.IOException;
import org.apache.http.HttpResponse;

public final class PushService extends Thread implements NetworkCallHandler{
    public static boolean DO_RETRY = true;
    
    private final String userId;
    private final String docId;
    
    private BufferInterface buffer;
    
    public PushService(String userId, String docId, BufferInterface buffer){    
        this.userId = userId;
        this.docId = docId;
        setBuffer(buffer);
    }
    
    @Override
    public void setBuffer(BufferInterface buffer){
        this.buffer = buffer;
    }
    
    @Override
    public void handleRequest(Request request) {
        boolean retry = DO_RETRY;
        do {
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
                handleResponse(response);
                retry = false;
            } catch (IOException e) {
                System.err.println("Unable to send HTTP request. Connection Refused. Retrying...");
            }
        } while (retry);
    }
   
    @Override
    public void handleResponse(HttpResponse response) {
    }
    
    private volatile boolean isRunning = true;
    @Override
    public void run(){
        while (isRunning) {
            Request request = (Request) buffer.take();
            handleRequest(request);
        }
    }

    @Override
    public void close() {
        isRunning = false;
    }

}
