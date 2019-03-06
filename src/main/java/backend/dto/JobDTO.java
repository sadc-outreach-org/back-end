package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDTO
{
    private int jobID;
    private String title;
    private String description;
    private String requirements;
}