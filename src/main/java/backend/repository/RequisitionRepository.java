package backend.repository;

import backend.model.Job;
import backend.model.Requisition;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//This is a crudrepository for query candidate infos from mysql database
@Transactional
public interface RequisitionRepository extends JpaRepository<Requisition, Integer> , JpaSpecificationExecutor<Requisition>
{
    Requisition findById(int id);
}