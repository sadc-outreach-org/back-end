package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.ExampleDTO;
import backend.model.Example;

@Mapper
public interface ExampleMapper
{
    ExampleMapper MAPPER = Mappers.getMapper( ExampleMapper.class);

    @Mapping(source = "codingChallenge.ccID", target = "ccID")
    ExampleDTO exampleToExampleDTO(Example example);

    @InheritInverseConfiguration
    Example exampleDTOToExample(ExampleDTO exampleDTO);
}