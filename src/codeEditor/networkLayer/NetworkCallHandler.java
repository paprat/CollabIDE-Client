package codeEditor.networkLayer;

import codeEditor.buffer.Buffer;
import org.apache.http.HttpResponse;

public interface NetworkCallHandler {
    void setBuffer(Buffer buffer);
    void handleRequest(Request request);
    void handleResponse(HttpResponse response);
    
    void run();
    void start();
    void close();
}
