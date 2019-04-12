package backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Getter
@Setter
public class RequisitionFullDTO
{
    private int requisitionID;
    private String title;
    private String description;
    private String requirements;
    @JsonProperty("isOpen")
    @Getter(AccessLevel.NONE)
    private boolean isOpen;
    private AdminDTO admin;
    private List<CCDTO> codingChallenges;
}