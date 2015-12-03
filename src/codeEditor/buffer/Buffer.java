package codeEditor.buffer;

public interface Buffer {
    public void put(Object element);
    public Object take();
    public boolean isEmpty();
}