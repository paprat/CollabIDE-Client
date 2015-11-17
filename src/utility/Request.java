package utility;

public class Request {
    private String requestUrl;
    private String serializedRequest;

    public Request(String requestUrl, String serializedRequest) {
        this.requestUrl = requestUrl;
        this.serializedRequest = serializedRequest;
    }
    
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getSerializedRequest() {
        return serializedRequest;
    }

    public void setSerializedRequest(String serializedRequest) {
        this.serializedRequest = serializedRequest;
    }
}