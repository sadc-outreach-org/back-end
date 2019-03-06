package backend.repository;

import backend.model.Admin;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin>{

    Admin findById(int id);

    @Query ("FROM Admin a WHERE a.profile.email = :email")
    Admin findByEmail(@Param("email") String email);
}