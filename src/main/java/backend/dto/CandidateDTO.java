package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO
{
    private int candidateID;
    private String email;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String zipCode;
    private String state;
    private String city;
    private String phoneNum;
}