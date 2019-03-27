package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.CandidateDTO;
import backend.dto.CandidateSummaryDTO;
import backend.model.Candidate;

@Mapper
public interface CandidateMapper {

    CandidateMapper MAPPER = Mappers.getMapper( CandidateMapper.class);

    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.phoneNum", target = "phoneNum")
    @Mapping(source = "profile.password", target = "password")
    CandidateDTO candidateToCandidateDTO(Candidate candidate);

    @InheritInverseConfiguration
    Candidate candidateDTOToCandidate(CandidateDTO canddiateDTO);

    
    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.phoneNum", target = "phoneNum")
    CandidateSummaryDTO candidateToCandidateSummaryDTO(Candidate cand);
}