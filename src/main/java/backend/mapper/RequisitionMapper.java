package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.RequisitionDTO;
import backend.dto.RequisitionWithoutAdminDTO;
import backend.model.Requisition;

@Mapper (uses = {AdminMapper.class})
public interface RequisitionMapper {

    RequisitionMapper MAPPER = Mappers.getMapper( RequisitionMapper.class);

    @Mapping(source = "job.title", target = "title")
    @Mapping(source = "job.description", target = "description")
    @Mapping(source = "job.requirements", target = "requirements")
    RequisitionDTO requisitionToRequisitionDTO(Requisition requisition);

    @InheritInverseConfiguration
    Requisition requisitionDTOToRequisition(RequisitionDTO requisitionDTO);

    @Mapping(source = "job.title", target = "title")
    @Mapping(source = "job.description", target = "description")
    @Mapping(source = "job.requirements", target = "requirements")
    RequisitionWithoutAdminDTO requisitionToRequisitionWithoutAdminDTO(Requisition requisition);
}