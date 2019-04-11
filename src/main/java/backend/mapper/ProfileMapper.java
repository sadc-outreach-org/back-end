package backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.LogInResultDTO;
import backend.model.Profile;

@Mapper
public interface ProfileMapper {

    ProfileMapper MAPPER = Mappers.getMapper( ProfileMapper.class);

    @Mapping(source = "userType.type", target = "role")
    @Mapping(source = "userID", target = "id")
    LogInResultDTO profileToLogInResultDTO(Profile profile);
}