package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import backend.dto.JobDTO;
import backend.model.Job;

@Mapper
public interface JobMapper {

    JobMapper MAPPER = Mappers.getMapper( JobMapper.class);

    Job jobDTOToJob(JobDTO jobDTO);

    @InheritInverseConfiguration
    JobDTO jobToJobDTO(Job job);
}