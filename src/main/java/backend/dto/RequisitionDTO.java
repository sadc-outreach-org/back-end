package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequisitionDTO
{
    private int requisitionID;
    private String title;
    private String description;
    private String requirements;
    private AdminDTO admin;
}