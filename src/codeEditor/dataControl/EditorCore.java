package codeEditor.dataControl;

import codeEditor.buffer.SynchronizedBuffer;
import codeEditor.buffer.Buffer;
import codeEditor.dataAccessLayer.Model;
import codeEditor.dataAccessLayer.Treap;
import codeEditor.eventNotification.NotificationSubject;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import exception.OperationNotExistException;

public class EditorCore implements DataControlLayer{
    
    Buffer operationBuffer = new SynchronizedBuffer();
    
    private final Model dataModel;
    private final String userId;
    private final String docId;
    
    private final NotificationSubject notificationService; 
    
    public EditorCore(String userId, String docId, NotificationSubject notificationService) {
        this.userId = userId;
        this.docId = docId;    
        this.dataModel = new Treap();
        this.notificationService = notificationService;
    }
    
    
    @Override
    public String getUserId() {
        return userId;
    }
   
    private void insertCharacter(String userId, int positionToInsert, char character) {
        dataModel.insert(positionToInsert, character);
    }

    private void eraseCharacter(String userId, int positionToErase) {
        dataModel.erase(positionToErase);
    }
    
    @Override
    public char charAt(int positionToSeek) {
        return dataModel.charAt(positionToSeek);
    }
    
    @Override
    public int getSize() {
        return dataModel.getSize();
    }
    
    @Override
    public void performOperation(Operation operation) {
        try {
            //System.out.println(operation.getType().toString());
            switch(operation.getType().toString()) {
                case "INSERT":{
                    InsertOperation insertOperation = (InsertOperation) operation;
                    this.insertCharacter(insertOperation.userId, insertOperation.position, insertOperation.charToInsert);   
                } break;
                case "ERASE": {
                    EraseOperation eraseOperation = (EraseOperation) operation;
                    this.eraseCharacter(eraseOperation.userId, eraseOperation.position);
                } break;
                case "REPOSITION": {
                } break;    
                default: {
                    throw new OperationNotExistException("Operation doesnot Exist.");
                }
            }
            if (operation.userId.equals(getUserId())) {
                //ignore if character belong to the same user.
            } else {
                notificationService.notifyObservers(operation);
            }
        } catch (OperationNotExistException e){
            e.printStackTrace(System.err);
        }
    }
}
