package backend.repository;

import backend.model.Admin;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface AdminRepository extends CrudRepository<Admin, Integer> {

    Admin findById(int id);
}