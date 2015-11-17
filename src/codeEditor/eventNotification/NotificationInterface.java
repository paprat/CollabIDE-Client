package codeEditor.eventNotification;

import codeEditor.operation.Operation;

public interface NotificationInterface {
    public void addObserver(ObserverInterface observer);
    public void notifyObservers(Operation operation);
}
