package backend.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import backend.error.APIError;
import backend.error.CandidateNotFoundException;
import backend.error.InvalidLoginException;
import backend.error.MissingInfomationException;
import backend.error.ResumeNotFoundException;
import backend.error.EmailInUseException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CandidateExceptionController extends ResponseEntityExceptionHandler {




    // Custom exceptions

    @ExceptionHandler(CandidateNotFoundException.class)
    protected ResponseEntity<APIError> handleCandidateNotFound(CandidateNotFoundException ex)
    {
        HttpStatus status = HttpStatus.NOT_FOUND;
        APIError error = new APIError(status, "Email not found", ex);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResumeNotFoundException.class)
    protected ResponseEntity<APIError> handleResumeNotFound(ResumeNotFoundException ex)
    {
        HttpStatus status = HttpStatus.NOT_FOUND;
        APIError error = new APIError(status, "Email not found");
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidLoginException.class)
    protected ResponseEntity<APIError> handleInvalidLogin(InvalidLoginException ex)
    {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        APIError error = new APIError(status, "Email or password is incorrect");
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MissingInfomationException.class)
    protected ResponseEntity<APIError> handleMissingInfomation(MissingInfomationException ex)
    {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        APIError error = new APIError(status, "Missing required infomation");
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmailInUseException.class)
    protected ResponseEntity<APIError> handleEmailInUse(EmailInUseException ex)
    {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        APIError error = new APIError(status, "Missing required infomation");
        return ResponseEntity.status(status).body(error);
    }
}