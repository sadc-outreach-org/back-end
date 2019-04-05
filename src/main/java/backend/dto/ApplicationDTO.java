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
    private RequisitionWithoutAdminDTO requisition;
    private CandidateSummaryDTO candidate;
    private AdminDTO admin;
    private String status;
    private String gitLink;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime interviewTime;
}