package codeEditor.dataControl;

import codeEditor.operation.Operation;

public interface DataControlLayer {
    String getUserId();
    void performOperation(Operation operation); 
   
    //Debugging Information
    int getSize();
    char charAt(int positionToErase);
    
}
