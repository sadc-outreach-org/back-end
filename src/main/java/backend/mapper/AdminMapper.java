package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.dto.AdminDTO;
import backend.dto.LogInResultDTO;
import backend.model.Admin;

@Mapper
public interface AdminMapper {

    AdminMapper MAPPER = Mappers.getMapper( AdminMapper.class);

    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "firstName", target = "profile.firstName")
    @Mapping(source = "lastName", target = "profile.lastName")
    @Mapping(source = "password", target = "profile.password")
    @Mapping(source = "phoneNum", target = "profile.phoneNum")
    Admin adminDTOToAdmin(AdminDTO adminDTO);

    @InheritInverseConfiguration
    AdminDTO adminToAdminDTO(Admin admin);

    @Mapping(source = "adminID", target = "id")
    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    LogInResultDTO adminToLogInResultDTO(Admin admin);
}