package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApplicationNoCandidateDTO 
{
    private int applicationID;
    private RequisitionSummaryDTO requisition;
    private AdminDTO admin;
    private String status;
}