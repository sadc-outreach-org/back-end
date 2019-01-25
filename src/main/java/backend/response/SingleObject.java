package backend.response;

public interface SingleObject<T> {

    public T getResult ();

    public void setResult(T result);
        
}