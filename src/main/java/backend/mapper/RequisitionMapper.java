package backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.request.RequisitionAdd;
import backend.dto.RequisitionDTO;
import backend.dto.RequisitionSummaryDTO;
import backend.model.Requisition;

@Mapper (uses = {AdminMapper.class, CodingChallengeMapper.class})
public interface RequisitionMapper {

    RequisitionMapper MAPPER = Mappers.getMapper( RequisitionMapper.class);

    @Mapping(source = "job.title", target = "title")
    @Mapping(source = "job.description", target = "description")
    @Mapping(source = "job.requirements", target = "requirements")
    RequisitionSummaryDTO requisitionToRequisitionSummaryDTO(Requisition requisition);

    @Mapping(source = "job.title", target = "title")
    @Mapping(source = "job.description", target = "description")
    @Mapping(source = "job.requirements", target = "requirements")
    RequisitionDTO requisitionToRequisitionDTO(Requisition requisition);

    @Mapping(source = "jobID", target = "job.jobID")
    @Mapping(source = "adminID", target = "admin.adminID")
    Requisition requisitionAddToRequisition(RequisitionAdd requisitionAdd);
}