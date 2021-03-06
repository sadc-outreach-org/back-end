package backend.repository;

import backend.model.UserType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface UserTypeRepository extends JpaRepository<UserType, Integer>, JpaSpecificationExecutor<UserType> {
    UserType findById(int id);
}