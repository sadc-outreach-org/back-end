package backend.repository;

import backend.model.Example;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface ExampleRepository extends JpaRepository<Example, Integer>, JpaSpecificationExecutor<Example> {
    Example findById(int id);
}