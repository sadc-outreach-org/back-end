package backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordReset
{
    private String email;
    private String oldPassword;
    private String newPassword;
}