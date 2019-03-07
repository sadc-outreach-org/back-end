package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApplicationDTO 
{
    private RequisitionSummaryDTO requisition;
    private CandidateSummaryDTO candidate;
    private AdminDTO admin;
    private String status;
}