package codeEditor.sessionLayer;

import codeEditor.buffer.Buffer;
import codeEditor.buffer.BufferInterface;
import codeEditor.dataControl.DataControlLayer;
import codeEditor.dataControl.EditorCore;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.eventNotification.NotificationInterface;
import codeEditor.eventNotification.NotificationService;
import codeEditor.networkLayer.NetworkCallHandler;
import codeEditor.networkLayer.PollingService;
import codeEditor.networkLayer.PushService;
import codeEditor.transform.TransformationThread;

public class SessionFactory extends AbstractSessionFactory{

    @Override
    public NetworkCallHandler createPollingThread(String userIdentifier, String docIdentifier, BufferInterface responseBuffer) {
         return new PollingService(userIdentifier, docIdentifier, responseBuffer); 
    }

    @Override
    public NetworkCallHandler createRequestHandlerThread(String userId, String docId, BufferInterface requestBuffer) {
        return  new PushService(userId, docId, requestBuffer); 
    }
    @Override
    public DataControlLayer createEditorInstance(String userId, String docId, NotificationInterface notificationService) {
        return new EditorCore(userId, docId, notificationService);
    }

    @Override
    public BufferInterface createBuffer() {
        return new Buffer();
    }

    @Override
    public TransformationThread createTranformationThread(String userId, BufferInterface responseBuffer, BufferInterface operationBuffer) {
        return new TransformationThread(userId, responseBuffer, operationBuffer);    
    }

    @Override
    public NotificationService createNotificationService() {
        return new NotificationService();
    }

    @Override
    public ExecuteOperationsThread createExecuteOperationThread(DataControlLayer editorCore, BufferInterface operationBuffer) {
        return new ExecuteOperationsThread(editorCore, operationBuffer);
    }
    
}
