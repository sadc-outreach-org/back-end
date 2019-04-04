package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Getter
@Setter
public class RequisitionSummaryDTO
{
    private int requisitionID;
    private String title;
    @JsonProperty("isOpen")
    @Getter(AccessLevel.NONE)
    private boolean isOpen;
}