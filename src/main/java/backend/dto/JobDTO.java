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

    public JobDTO() {}

    public JobDTO(int jobID, String title, String description, String requirements)
    {
        this.jobID          = jobID;
        this.title          = title;
        this.description    = description;
        this.requirements   = requirements;
    }
}