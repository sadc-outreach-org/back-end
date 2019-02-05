package backend.repository;

import backend.model.Candidate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface CandidateRepository extends CrudRepository<Candidate, Integer> {

    Candidate findById(int id);
}