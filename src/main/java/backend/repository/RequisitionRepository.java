package backend.repository;

import backend.model.Requisition;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface RequisitionRepository extends JpaRepository<Requisition, Integer> , JpaSpecificationExecutor<Requisition>
{
    Requisition findById(int id);

    @Query("From Requisition Where jobID =:jobID ORDER BY requisitionID ASC")
    List<Requisition> findOpenRequisitionByJobID(@Param("jobID") long jobID);
}