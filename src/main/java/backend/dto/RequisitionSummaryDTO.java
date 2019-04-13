package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequisitionSummaryDTO
{
    private int requisitionID;
    private String title;
    private String description;
    private String requirements;
    private Boolean isOpen;
    private AdminDTO admin;
}