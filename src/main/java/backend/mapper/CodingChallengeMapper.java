package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import backend.dto.CCDTO;
import backend.dto.CCSummaryDTO;
import backend.model.CodingChallenge;

@Mapper
public interface CodingChallengeMapper
{
    CodingChallengeMapper MAPPER = Mappers.getMapper( CodingChallengeMapper.class);

    CCDTO ccToCCDTO(CodingChallenge codingChallenge);

    @InheritInverseConfiguration
    CodingChallenge ccDTOToCC(CCDTO ccDTO);

    CCSummaryDTO ccToCCSummaryDTO(CodingChallenge codingChallenge);
    
}