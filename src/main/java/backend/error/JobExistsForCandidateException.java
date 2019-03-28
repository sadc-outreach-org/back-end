package backend.error;

public class JobExistsForCandidateException extends RuntimeException
{
    private static final long serialVersionUID = 801L;

    public JobExistsForCandidateException()
    {
        super();
    }
}