package backend.error;

public class UserNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 701L;

    public UserNotFoundException()
    {
        super();
    }
}