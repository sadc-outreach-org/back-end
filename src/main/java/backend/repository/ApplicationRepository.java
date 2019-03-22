package backend.repository;
import backend.model.Application;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface ApplicationRepository extends JpaRepository<Application, Integer>, JpaSpecificationExecutor<Application>{

    Application findById(int id);
}