package codeEditor.transform;

import codeEditor.buffer.Buffer;
import codeEditor.operation.EditOperations;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.transform.operationalTransform.CompoundOT;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransformationThread extends Thread{
    String userId;
    Buffer responseBuffer;
    Buffer operationBuffer;
    
    public static Mutex mutex = new Mutex();
    
    public TransformationThread(String userId, Buffer responseBuffer, Buffer operationBuffer) {
        this.localOperations = new ArrayList<>();
        this.userId = userId;
        this.responseBuffer = responseBuffer;
        this.operationBuffer = operationBuffer;
    }  
   
    private volatile boolean isRunning = true;
    
    @Override
    public void run(){
        while (isRunning) {
            ArrayList<Operation> operations = (ArrayList<Operation>) responseBuffer.take();
            ArrayList<Operation> transformedOperations = transform(operations);
            if (transformedOperations != null) {
                for (Operation operation: transformedOperations ) {
                    operationBuffer.put(operation);
                }
            }
        }
    }
    
    private ArrayList<Operation> localOperations;
    private synchronized ArrayList<Operation> transform(ArrayList<Operation> remoteOperations) {
        ArrayList<Operation> transformedOperations;
        
            /*for (Operation operation: remoteOperations) {
                //System.out.println("Operation Id:" + operation.operationId);
                if (operation.type == EditOperations.INSERT) {
                    //System.out.println("CharToInsert:" + ((InsertOperation) operation).charToInsert);
                    //System.out.println("Position:" + ((InsertOperation) operation).position);
                } else if (operation.type == EditOperations.ERASE){
                    //System.out.println("Position:" + ((EraseOperation) operation).position);
                }
            }*/
            
        if (localOperations.isEmpty()) {
            transformedOperations = remoteOperations;
        } else {
            transformedOperations = CompoundOT.performTransform(userId, localOperations, remoteOperations);
        }
        
            /*for (Operation operation: transformedOperations) {
                //System.out.println(operation.operationId);
                if (operation.type == EditOperations.INSERT) {
                    //System.out.println("CharToInsert:" + ((InsertOperation) operation).charToInsert);
                    //System.out.println("Position:" + ((InsertOperation) operation).position);
                } else if (operation.type == EditOperations.ERASE){
                    //System.out.println("Position:" + ((EraseOperation) operation).position);
                }
            }*/
        
        return transformedOperations;
    }
    
    public synchronized void addOperation(Operation operation) {
        try {
            mutex.acquire();
            localOperations.add(operation);
        } catch (InterruptedException ex) {
            Logger.getLogger(TransformationThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            mutex.release();
        }
    }

    public void close() {
        isRunning = false;
    }
}
