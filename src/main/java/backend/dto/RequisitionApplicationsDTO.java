package backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionApplicationsDTO
{
    private int requisitionID;
    private String title;
    private AdminDTO admin;
    private List<CandSummaryWithStatus> applications;
}