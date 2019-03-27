package backend.repository;

import backend.model.Profile;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Integer>, JpaSpecificationExecutor<Profile> {

    Profile findByEmail(String email);

    //First match and list of matching last name
    Profile findBylastName(String lastName);

    List<Profile> findBylastNameLike(String lastname);

    //First match and list of matching first name
    Profile findFirstByfirstName(String firstName);

    List<Profile> findByfirstNameLike(String firstName);

    List<Profile> findByUserType(String userType);

    Long deleteByEmail(String email);
}