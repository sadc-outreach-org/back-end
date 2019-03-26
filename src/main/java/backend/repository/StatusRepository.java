package backend.repository;

import backend.model.Status;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface StatusRepository extends JpaRepository<Status, Integer>, JpaSpecificationExecutor<Status> {
    Status findById(int id);
}