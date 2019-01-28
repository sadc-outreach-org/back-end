package backend.repository;

import backend.model.Candidate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface CandidateRepository extends CrudRepository<Candidate, Integer> {

    
    Candidate findOneByemail(String email);

    //First match and list of matching last name
    Candidate findBylastName(String lastName);
    List<Candidate> findBylastNameLike(String lastname);

    //First match and list of matching first name
    Candidate findFirstByfirstName(String firstName);
    List<Candidate> findByfirstNameLike(String firstName);

    Long deleteByEmail(String email);
}