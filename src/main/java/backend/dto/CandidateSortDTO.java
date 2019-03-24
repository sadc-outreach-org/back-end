package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateSortDTO
{
    private int candidateID;
    private String email;
    private String firstName;
    private String lastName;
    private String status;

    public CandidateSortDTO(int candidateID, String email, String firstName, String lastName, String status)
    {
        this.candidateID    = candidateID;
        this.email          = email;
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.status         = status;
    }
}