package authenticate;

import com.google.gson.Gson;

public class User {
    private String name;
    private String userId;
    private String username;
    private String password;
    private String emailId;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserIdentifier() {
        return userId;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userId = userIdentifier;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String serialize(){
        String json = new Gson().toJson(this); 
        return json;
    }
}
