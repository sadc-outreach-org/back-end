package backend.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.NullValuePropertyMappingStrategy;

import backend.dto.CandidateDTO;
import backend.dto.CandidateSummaryDTO;
import backend.dto.LogInResultDTO;
import backend.model.Candidate;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CandidateMapper {

    CandidateMapper MAPPER = Mappers.getMapper( CandidateMapper.class);

    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.phoneNum", target = "phoneNum")
    CandidateDTO candidateToCandidateDTO(Candidate candidate);

    @InheritInverseConfiguration
    @Mapping(target = "candidateID", ignore = true)
    Candidate candidateDTOToCandidate(CandidateDTO canddiateDTO);

    @InheritConfiguration
    Candidate updateCandidateFromCandidateDTO(CandidateDTO candidateDTO, @MappingTarget Candidate candidate);
    
    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.phoneNum", target = "phoneNum")
    CandidateSummaryDTO candidateToCandidateSummaryDTO(Candidate cand);

    @Mapping(source = "candidateID", target ="id")
    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    LogInResultDTO candidateToLogInResultDTO(Candidate cand);


}