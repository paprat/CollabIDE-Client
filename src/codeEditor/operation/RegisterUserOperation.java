package codeEditor.operation;

import com.google.gson.Gson;

public class RegisterUserOperation extends Operation {
    public RegisterUserOperation(String operationId, String userIdentifier) {
        super(operationId, userIdentifier, EditOperations.REGISTER);
    }
    
    @Override
    public String serialize() {
        return new Gson().toJson(this);
    }
}