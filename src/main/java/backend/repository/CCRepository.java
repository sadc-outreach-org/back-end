package backend.repository;

import backend.model.CodingChallenge;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface CCRepository extends JpaRepository<CodingChallenge, Integer>, JpaSpecificationExecutor<CodingChallenge> {
    CodingChallenge findById(int id);
}