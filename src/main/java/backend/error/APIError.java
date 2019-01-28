package backend.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.http.HttpStatus;

import backend.response.APIResponse;

@JsonInclude(Include.NON_EMPTY)
public class APIError extends APIResponse {

    private String debugMessage;

    public APIError(HttpStatus status, String message)
    {
        super(status, message);
    }

    public APIError(HttpStatus status, String message, Exception ex)
    {
        super(status, message);
        this.debugMessage = ex.getLocalizedMessage();
    }

    public String getDebugMessage()
    {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage)
    {
        this.debugMessage = debugMessage;
    }
}