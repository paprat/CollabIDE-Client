package exception;

public class OperationNotExistException extends Exception{
    public OperationNotExistException(String errorMessage) {   
        super(errorMessage);
    } 
}
