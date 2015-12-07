package codeEditor.sessionLayer;

import codeEditor.dataControl.Editor;
import codeEditor.dataControl.Executor;
import codeEditor.eventNotification.Subject;
import codeEditor.transform.Transformation;
import codeEditor.buffer.Buffer;
import codeEditor.eventNotification.Observer;
import codeEditor.networkLayer.PollService;
import codeEditor.networkLayer.PushService;
import config.Configuration;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractSession {
    
    protected final String docId;
    protected final String userId;
    
    protected volatile int lastSynchronized;
    protected final ReentrantLock updateState = new ReentrantLock();
    
    protected final Editor model;
    
    protected final Buffer pushBuffer;
    protected final Buffer executeBuffer;
   
    protected final Executor executor;
    
    protected final Transformation transformation;
   
    protected final PushService pushService; 
    protected final PollService pollService;
    
    protected final Subject eventNotification;
    
    
    public AbstractSession(String userId, String docId) {
        
        Configuration.getConfiguration();
        
        this.userId = userId;
        this.docId = docId;
        
        AbstractSessionFactory sessionFactory = new SessionFactory();

        eventNotification = sessionFactory.createNotificationService();
        model = sessionFactory.createEditorInstance(userId, docId, eventNotification);
        pushBuffer = sessionFactory.createBuffer();
        executeBuffer = sessionFactory.createBuffer();
        transformation = sessionFactory.createTranformation(userId);
        pushService = sessionFactory.createPushService(userId, docId, pushBuffer);
        pollService = sessionFactory.createPollService()
                    .setUserId(userId)
                    .setDocId(docId)
                    .setBuffer(executeBuffer)
                    .setTranformation(transformation)
                    .setSession(this);
        executor = sessionFactory.createExecutor(model, executeBuffer);
    }
    
    //Starts and Stops the session
    public String startSession() {
        //Register the user on Doc
        new RegisterUser(userId, docId, executor).registerUserOnDoc();
  
        pushService.start();
        pollService.start();
        executor.start();
       
        return userId;
    }
    
    public void stopSession() {
        pushService.interrupt();
        pollService.interrupt();
        executor.interrupt();
    }
       
    //Register the user for updates from remote
    public void register(Observer observer) {
        this.eventNotification.addObserver(observer);
    }
        
    
    //Get the last time session is synchronized with remote
    public int getLastSynchronized() {
        return lastSynchronized;
    }

    public void setLastSynchronized(int lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
    }
    
 
    //Lock and Unlock Session
    public void lock() throws InterruptedException {
        //flushes the operation buffer
        while (!executeBuffer.isEmpty());
        
        //guarantees that the no operation is done on session untile the session is unlocked again 
        updateState.lock();
    }
    
    public void unlock() {
        updateState.unlock();
    }
}
