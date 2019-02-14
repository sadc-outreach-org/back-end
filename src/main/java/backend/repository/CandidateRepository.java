package backend.repository;

import backend.model.Candidate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface CandidateRepository extends CrudRepository<Candidate, Integer> {

    Candidate findById(int id);

    @Query ("FROM Candidate c WHERE c.user.email = :email")
    Candidate findByEmail(@Param("email") String email);
 
}