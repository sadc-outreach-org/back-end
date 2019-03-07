package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandSummaryWithStatus
{
    private CandidateSummaryDTO candidate;
    private String status;
}