package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String streetAddress;
    private String zipCode;
    private String state;
    private String city;
    private String phoneNum;
}