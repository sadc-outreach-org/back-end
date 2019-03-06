package backend.repository;

import backend.model.Job;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface JobRepository extends JpaRepository<Job, Integer> , JpaSpecificationExecutor<Job>
{
    Job findById(int id);
}