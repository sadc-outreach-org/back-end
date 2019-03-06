package backend.repository;

import backend.model.Candidate;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface CandidateRepository extends JpaRepository<Candidate, Integer>, JpaSpecificationExecutor<Candidate> {

    Candidate findById(int id);

    @Query ("FROM Candidate c WHERE c.profile.email = :email")
    Candidate findByEmail(@Param("email") String email);
 
}