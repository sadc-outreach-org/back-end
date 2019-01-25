package backend.error;

public class EmailInUseException extends RuntimeException
{
    private static final long serialVersionUID = 101L;

    public EmailInUseException()
    {
        super();
    }
}