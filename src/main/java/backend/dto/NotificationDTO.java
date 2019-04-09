package backend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import backend.Utility.CustomLocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO
{
    private int notificationID;
    private int userID;
    private String message;
    private boolean hasRead;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
}