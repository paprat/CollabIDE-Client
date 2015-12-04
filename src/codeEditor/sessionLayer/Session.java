package codeEditor.sessionLayer;

import codeEditor.dataControl.DataControlLayer;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.networkLayer.NetworkCallHandler;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.UserOperations;
import codeEditor.transform.TransformationThread;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.Observer;
import config.Configuration;
import static config.NetworkConfig.PUSH_OPERATIONS_URL;
import codeEditor.networkLayer.Request;
import codeEditor.operation.EditOperations;
import codeEditor.operation.userOperations.RepositionOperation;
import exception.OperationNotExistException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {
    
    private final String docId;
    private final String userId;
    
    public final DataControlLayer editorInstance;
    
    private final ExecuteOperationsThread executeOperationThread;
    private final TransformationThread transformationThread;
   
    private final NetworkCallHandler requestHandlerThread; 
    private final NetworkCallHandler pollingServiceThread;
    
    public final NotificationSubject notificationService;
    
    private final Buffer requestBuffer, responseBuffer, operationBuffer;
    
    public static volatile int lastSyncStamp;
    
    public Session(String userId, String docId) {
        Configuration.getConfiguration();
        AbstractSessionFactory sessionFactory = new SessionFactory();
        notificationService = sessionFactory.createNotificationService();
        
        editorInstance = sessionFactory.createEditorInstance(userId, docId, notificationService);
        this.userId = userId;
        this.docId = docId;
        
        requestBuffer = sessionFactory.createBuffer();
        responseBuffer = sessionFactory.createBuffer();
        operationBuffer= sessionFactory.createBuffer();
        
        requestHandlerThread = sessionFactory.createRequestHandlerThread(userId, docId, requestBuffer);
        pollingServiceThread = sessionFactory.createPollingThread(userId, docId, responseBuffer); 
        executeOperationThread = sessionFactory.createExecuteOperationThread(editorInstance, operationBuffer);
        
        transformationThread = sessionFactory.createTranformationThread(userId, responseBuffer, operationBuffer);   
    
        //Register the user on Doc
        RegisterUser r = new RegisterUser(userId, docId, executeOperationThread);
        r.registerUserOnDoc();
    }
    
    public String startSession() {
        requestHandlerThread.start();
        pollingServiceThread.start();
        executeOperationThread.start();
        transformationThread.start();
        return userId;
    }
    
    public void stopSession() {
        requestHandlerThread.interrupt();
        pollingServiceThread.interrupt();
        executeOperationThread.interrupt();
        transformationThread.interrupt();
    }
       
    public void register(Observer observer) {
        this.notificationService.addObserver(observer);
    }
    
    public void pushOperation(UserOperations userOperation){
        
        //Spin lock to let operationBuffer or responseBuffer go empty before any operation can be pushed.
        while(!responseBuffer.isEmpty() || !operationBuffer.isEmpty());
        try {
            String pushUrl = PUSH_OPERATIONS_URL + "?userId=" + userId + "&docId=" + docId;
            if (userOperation.getType() == EditOperations.INSERT){
                System.out.println("Op pushed");
        
                InsertOperation insertOperation = (InsertOperation) userOperation;
                insertOperation.lastSyncStamp = Session.lastSyncStamp;
                executeOperationThread.pushOperation((Operation) userOperation);
                transformationThread.addOperation(insertOperation);
                requestBuffer.put(new Request(pushUrl, insertOperation.serialize()));
            
            } else if (userOperation.getType() == EditOperations.ERASE){
                System.out.println("Op pushed");
                
                EraseOperation eraseOperation = (EraseOperation) userOperation;
                eraseOperation.lastSyncStamp = Session.lastSyncStamp;
                executeOperationThread.pushOperation((Operation) userOperation);
                transformationThread.addOperation(eraseOperation);
                requestBuffer.put(new Request(pushUrl, eraseOperation.serialize()));    
            
            } else if (userOperation.getType() == EditOperations.REPOSITION) {
            
                RepositionOperation repositionOperation = (RepositionOperation) userOperation;
                requestBuffer.put(new Request(pushUrl, repositionOperation.serialize()));    
            
            } else {
            
                throw new OperationNotExistException("Operation Doesnot Exist.");
            
            }
        } catch (OperationNotExistException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}