package backend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import backend.Utility.CustomLocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ApplicationDTO 
{
    private int applicationID;
    private RequisitionDTO requisition;
    private CandidateSummaryDTO candidate;
    private String status;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;
    private String gitLink;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime interviewTime;
}
