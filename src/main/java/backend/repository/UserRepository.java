package backend.repository;

import backend.model.User;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {

    User findOneByemail(String email);

    //First match and list of matching last name
    User findBylastName(String lastName);

    List<User> findBylastNameLike(String lastname);

    //First match and list of matching first name
    User findFirstByfirstName(String firstName);

    List<User> findByfirstNameLike(String firstName);

    List<User> findByUserType(String userType);

    Long deleteByEmail(String email);
}