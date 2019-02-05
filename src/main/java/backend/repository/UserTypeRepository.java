package backend.repository;

import backend.model.UserType;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface UserTypeRepository extends CrudRepository<UserType, Integer> {

}