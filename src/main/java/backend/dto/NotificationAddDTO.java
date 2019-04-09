package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationAddDTO
{
    private int userID;
    private String message;
}

