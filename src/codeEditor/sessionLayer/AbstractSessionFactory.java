package codeEditor.sessionLayer;

import codeEditor.dataControl.DataControlLayer;
import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.networkLayer.NetworkCallHandler;
import codeEditor.transform.TransformationThread;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.NotificationInterface;
import codeEditor.eventNotification.NotificationService;

public abstract class AbstractSessionFactory {
    public abstract Buffer createBuffer();
    public abstract NotificationService createNotificationService();
    public abstract NetworkCallHandler createPollingThread(String userId, String docId, Buffer responseBuffer);
    public abstract NetworkCallHandler createRequestHandlerThread(String userId, String docId, Buffer requestBuffer);
    public abstract DataControlLayer createEditorInstance(String userId, String docId, NotificationInterface notificationService);
    public abstract TransformationThread createTranformationThread(String userId, Buffer responseBuffer, Buffer operationBuffer);
    public abstract ExecuteOperationsThread createExecuteOperationThread(DataControlLayer editorCore, Buffer operationBuffer);
}
