package codeEditor.operation;

public enum EditOperations {
    INSERT("INSERT"),
    ERASE("ERASE"),
    REGISTER("REGISTER"),
    REPOSITION("REPOSITION");
    
    private final String operation;
    
    private EditOperations(String operation){
        this.operation = operation;
    }
    
    @Override
    public String toString(){
        return this.operation;
    }
}