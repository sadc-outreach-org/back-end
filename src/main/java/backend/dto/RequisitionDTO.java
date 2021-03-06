package backend.dto;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionDTO
{
    private int requisitionID;
    private String title;
    private String description;
    private String requirements;
    private Boolean isOpen;
    private AdminDTO admin;
    private List<CCDTO> codingChallenges;
}