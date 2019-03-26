package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApplicationDTO 
{
    private int applicationID;
    private RequisitionWithoutAdminDTO requisition;
    private CandidateSummaryDTO candidate;
    private AdminDTO admin;
    private String status;
    private String gitLink;
}