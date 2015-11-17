package codeEditor.sessionLayer;

import codeEditor.dataControl.ExecuteOperationsThread;
import codeEditor.networkLayer.Request;
import codeEditor.networkLayer.SendPostRequest;
import codeEditor.operation.RegisterUserOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.utility.RandomGen;
import static config.NetworkConfig.REGISTER_URL;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class RegisterUser {
    private final String userId;
    private final String docId;
    private final ExecuteOperationsThread executeOperationThread;
    
    public RegisterUser(String userId, String docId,  ExecuteOperationsThread executeOperationThread) {
        this.userId = userId;
        this.docId = docId;
        this.executeOperationThread = executeOperationThread; 
    }
    
    public final void registerUserOnDoc() {
        String registerMessage = REGISTER_URL + "?userId=" + userId + "&docId=" + docId;
        RegisterUserOperation registerOperation = new RegisterUserOperation(RandomGen.getRandom(), userId);
        Request registerRequest = new Request(registerMessage, registerOperation.serialize()); 
        boolean retry = true;
        do {
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(registerRequest.getRequestUrl(), registerRequest.getSerializedRequest());
                HttpEntity httpEntity = response.getEntity();
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);
                content = content.replaceAll("\r\n", "\n");
                for (int i = 0; i < content.length(); i++) {
                    executeOperationThread.pushOperation(new InsertOperation(RandomGen.getRandom(), "0", i, content.charAt(i)));
                }
                retry = false;
            } catch (IOException | UnsupportedOperationException ex) {
                System.err.println("Unable to send HTTP request. Connection Refused. Retrying...");
            }
        } while (retry);
    }
}
