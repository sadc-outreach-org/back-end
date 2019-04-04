package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInResultDTO
{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}