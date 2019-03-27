package backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.ApplicationAddDTO;
import backend.dto.ApplicationDTO;
import backend.model.Application;

@Mapper (uses = {AdminMapper.class, CandidateMapper.class, RequisitionMapper.class})
public interface ApplicationMapper {

    ApplicationMapper MAPPER = Mappers.getMapper( ApplicationMapper.class);

    @Mapping(source = "status.status", target  = "status")
    @Mapping(source = "requisition.admin", target = "admin")
    ApplicationDTO applicationToApplicationDTO(Application application);

    @Mapping(source = "requisitionID", target = "requisition.requisitionID")
    @Mapping(source = "candidateID", target = "candidate.candidateID")
    Application applicationAddDTOToApplication(ApplicationAddDTO applicationAddDTO);

}