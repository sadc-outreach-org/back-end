package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Setter
@Getter
public class RequisitionWithoutAdminDTO
{
    private int requisitionID;
    private String title;
    private String description;
    private String requirements;
    @JsonProperty("isOpen")
    @Getter(AccessLevel.NONE)
    private boolean isOpen;
}