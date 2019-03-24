package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateSummaryDTO
{
    private int candidateID;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNum;
}