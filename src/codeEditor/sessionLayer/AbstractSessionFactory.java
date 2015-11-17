package codeEditor.sessionLayer;

import codeEditor.dataControl.DataControlLayer;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.networkLayer.NetworkCallHandler;
import codeEditor.transform.TransformationThread;
import codeEditor.buffer.BufferInterface;
import codeEditor.eventNotification.NotificationInterface;
import codeEditor.eventNotification.NotificationService;

public abstract class AbstractSessionFactory {
    public abstract BufferInterface createBuffer();
    public abstract NotificationService createNotificationService();
    public abstract NetworkCallHandler createPollingThread(String userId, String docId, BufferInterface responseBuffer);
    public abstract NetworkCallHandler createRequestHandlerThread(String userId, String docId, BufferInterface requestBuffer);
    public abstract DataControlLayer createEditorInstance(String userId, String docId, NotificationInterface notificationService);
    public abstract TransformationThread createTranformationThread(String userId, BufferInterface responseBuffer, BufferInterface operationBuffer);
    public abstract ExecuteOperationsThread createExecuteOperationThread(DataControlLayer editorCore, BufferInterface operationBuffer);
}
