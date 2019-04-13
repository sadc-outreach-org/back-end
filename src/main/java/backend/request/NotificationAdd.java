package backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationAdd
{
    private int userID;
    private String message;
}

