package projectManager;

import static config.NetworkConfig.PROJECT_ADD_NODE_URL;
import static config.NetworkConfig.PROJECT_VIEW_URL;
import codeEditor.networkLayer.SendPostRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.ConnectivityFailureException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import utility.Request;

public class Collections extends Node {
    public Collections(String name, String path) {
        super(name, path, Type.COLLECTION);
    }
    
    public Node createNode(String name, Type type) throws ConnectivityFailureException, UnableToCreateException {
        System.out.println(name);
        String projectPath = getPath() + "." + getName();
        Node node = new Node(name, projectPath, type); 
        return addNode(node);
    }
    
    private Node addNode(Node node) throws ConnectivityFailureException, UnableToCreateException {
        String nodePath = PROJECT_ADD_NODE_URL + "?path=" + getPath() + "." + getName();
        Request request = new Request(nodePath, node.toJson());
        try {
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());        
            HttpEntity httpEntity = response.getEntity();
            try {
                InputStream inStream = httpEntity.getContent();
                String serializedContent = IOUtils.toString(inStream);
                if (serializedContent.equals("")) {
                    throw new UnableToCreateException("Unable To Create");
                } else {
                    if (node.getType() == Type.COLLECTION) {
                        Collections newCollection = new Gson().fromJson(serializedContent, Collections.class);
                        return newCollection;
                    } else {
                        Doc newDoc = new Gson().fromJson(serializedContent, Doc.class);
                        return newDoc;
                    }
                }
            } catch (IOException | UnsupportedOperationException ex) {
                ex.printStackTrace(System.err);
            }
        } catch(IOException e) {
            throw new ConnectivityFailureException("Unable to Connect");
        }
        return null;
    }
    
    public ArrayList getContent() throws ConnectivityFailureException {
        try {
            ArrayList<Collections> node = new ArrayList<>();
            String projectViewURL = PROJECT_VIEW_URL + "?path=" + getPath() + "." + getName();
            Request request = new Request(projectViewURL, "");
            HttpResponse response = SendPostRequest.sendPostRequest(request.getRequestUrl(), request.getSerializedRequest());
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

                    java.lang.reflect.Type listType = new TypeToken<ArrayList<Node>>() {}.getType();
                    ArrayList<Node> list = gson.fromJson(serializedContent, listType);
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
    
    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Collections other = (Collections) obj;
        
        return (this.getPath() + this.getName()).equals(other.getPath() + other.getName());
    }
}
