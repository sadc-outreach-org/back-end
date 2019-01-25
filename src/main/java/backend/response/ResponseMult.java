package backend.response;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ResponseMult<T> extends APIResponse implements MultObjects<T>
{
    private List<T> result;
    public ResponseMult(HttpStatus status, String message, List<T> result)
    {
        super(status, message);
        this.result = result;
    }

    @Override
    public List<T> getResult ()
    {
        return result;
    }

    @Override
    public void setResult(List<T> result)
    {
        this.result = result;
    }
}