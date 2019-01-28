package backend.response;

import org.springframework.http.HttpStatus;
import backend.response.APIResponse;

public class ResponseSingle<T> extends APIResponse implements SingleObject<T>
{
    private T result;
    public ResponseSingle(HttpStatus status, String message, T result)
    {
        super(status, message);
        this.result = result;
    }

    @Override
    public T getResult()
    {
        return result;
    }

    @Override
    public void setResult(T result)
    {
        this.result = result;
    }

}