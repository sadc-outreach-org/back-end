package backend.response;

import org.springframework.http.HttpStatus;

public class ResponseSingle<T> extends APIResponse implements SingleObject<T>
{
    private T result;
    public ResponseSingle(HttpStatus status, String message, T result)
    {
        super(status, message);
        this.message = message;
        this.result = result;
    }

    public T getResult()
    {
        return result;
    }

    public void setResult(T result)
    {
        this.result = result;
    }
}