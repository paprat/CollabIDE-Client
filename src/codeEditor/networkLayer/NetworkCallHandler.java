package codeEditor.networkLayer;

import codeEditor.buffer.BufferInterface;
import org.apache.http.HttpResponse;

public interface NetworkCallHandler {
    void setBuffer(BufferInterface buffer);
    void handleRequest(Request request);
    void handleResponse(HttpResponse response);
    
    void run();
    void start();
    void close();
}
