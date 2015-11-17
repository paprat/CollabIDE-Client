package codeEditor.buffer;

import codeEditor.networkLayer.NetworkCallHandler;

public interface BufferInterface {
    public void put(Object element);
    public Object take();
    public boolean isEmpty();
    
    public void setHandler(NetworkCallHandler requestHandler);
}

