package codeEditor.eventNotification;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class NotificationService implements NotificationInterface{
    
    ArrayList<ObserverInterface> observerList = new ArrayList<>();  
    
    @Override
    public void addObserver(ObserverInterface observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Operation operation) {
        for (ObserverInterface observer: observerList) {
            observer.notifyObserver(operation);
        }
    }
}
