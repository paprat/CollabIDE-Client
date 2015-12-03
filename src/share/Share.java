package share;

import authenticate.entities.User;
import codeEditor.networkLayer.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static config.NetworkConfig.GET_USERS_URL;
import static config.NetworkConfig.SHARE_URL;
import static config.NetworkConfig.SHARE_VIEW_URL;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import projectManager.Collections;
import projectManager.Node;
import utility.SendPostRequest;

public class Share {
    public static ArrayList<User> getUsers() throws ConnectivityFailureException { 
        String url = GET_USERS_URL;
        Request request = new Request(url, "");
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String content = IOUtils.toString(inStream);
                System.out.println(content);
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                ArrayList<User> userList = gson.fromJson(content, listType);
                for (User user: userList) {
                    System.out.println(user.getUsername());
                }
                return userList;
            } catch (UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            }
        } catch(IOException ex) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
        return null;
    }
    
    public static void shareWith(String userId, String projectName, ArrayList<String> shareIds) throws ConnectivityFailureException {
        String url = SHARE_URL + "?userId=" + userId + "&docId=" + projectName + "&shareId=" + shareIds.get(0);
        Request request = new Request(url, "");
        try {
            SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
        } catch(IOException ex) {
            throw new ConnectivityFailureException("Unable to connect.");
        }
    }

    public static ArrayList<Collections> getSharedProjects(String userId) throws ConnectivityFailureException { 
        try {
            ArrayList<Collections> node = new ArrayList<>();
            String projectViewURL = SHARE_VIEW_URL + "?userId=" + userId;
            utility.Request request = new utility.Request(projectViewURL, "");
            HttpResponse response = codeEditor.networkLayer.SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String serializedContent = IOUtils.toString(inStream);
                if (serializedContent.equals("")) {
                } else {
                    //System.out.println(serializedContent);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Node.class, new projectManager.Deserializer());
                    Gson gson = gsonBuilder.create();

                    java.lang.reflect.Type listType = new TypeToken<ArrayList<Collections>>() {}.getType();
                    ArrayList<Collections> list = gson.fromJson(serializedContent, listType);
                    return list;
                 }
            } catch (IOException | UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            } 
        } catch (IOException e) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        return null;
    }
}
