package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.CandidateDTO;
import backend.model.Candidate;

public interface CandidateMapper {

    CandidateMapper MAPPER = Mappers.getMapper( CandidateMapper.class);

    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "firstName", target = "profile.firstName")
    @Mapping(source = "lastName", target = "profile.lastName")
    @Mapping(source = "password", target = "profile.password")
    Candidate candidateDTOToCandidate(CandidateDTO canddiateDTO);

    @InheritInverseConfiguration
    CandidateDTO candidateToCandidateDTO(Candidate candidate);
}