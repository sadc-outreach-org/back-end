package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO 
{
    private int adminID;
    private String position;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNum;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
}