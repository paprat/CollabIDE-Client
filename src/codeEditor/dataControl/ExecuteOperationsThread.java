package codeEditor.dataControl;

import codeEditor.operation.Operation;
import codeEditor.buffer.Buffer;

interface ExecuteOperations {
    public void setBuffer(Buffer buffer);
    public void doOperations();
}

public class ExecuteOperationsThread extends Thread{
    DataControlLayer editorCore;
    Buffer operationBuffer;
    
    public ExecuteOperationsThread(DataControlLayer editorCore, Buffer operationBuffer) {
        this.editorCore = editorCore;
        this.operationBuffer = operationBuffer; 
    }
    
    public void pushOperation(Operation operation) {
        operationBuffer.put(operation);
    }
    
    private volatile boolean isRunning = true;
    @Override
    public void run() {
        while (isRunning) {
            try {
                Operation operation = (Operation) operationBuffer.take();
                if (operation != null) {
                    editorCore.performOperation(operation);
                } else {
                    throw new NullPointerException("Operation is null.");
                }
            } catch (NullPointerException e) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    public void close() {
        isRunning = false;
    }
}
