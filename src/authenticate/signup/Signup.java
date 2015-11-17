package authenticate.signup;

import static config.NetworkConfig.SIGNUP_URL;
import authenticate.Status;
import authenticate.User;
import codeEditor.networkLayer.SendPostRequest;
import com.google.gson.Gson;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class Signup {
    private static final boolean DO_RETRY = true;
    
    public static Status doSignUp(User user) throws ConnectivityFailureException {
        String url = SIGNUP_URL;
        boolean retry = !DO_RETRY;
        do {
            utility.Request request = new utility.Request(url, user.serialize());
            try {
                HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
                retry = true;
                HttpEntity httpEntity = response.getEntity();
                try {
                    InputStream inStream = httpEntity.getContent();
                    String content = IOUtils.toString(inStream);
                    Gson gson = new Gson();
                    Status status = gson.fromJson(content, Status.class);
                    return status;
                } catch (IOException | UnsupportedOperationException ex) {
                    ex.printStackTrace(System.err);
                }
            } catch(IOException e) {
                throw new ConnectivityFailureException("Unable to connect.");
            }
        } while (!retry);
        return null;
    }
}
