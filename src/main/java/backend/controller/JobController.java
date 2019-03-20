package backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import backend.dto.AdminDTO;
import backend.dto.CandidateDTO;
import backend.dto.JobDTO;
import backend.dto.RequisitionApplicationsDTO;
import backend.dto.RequisitionDTO;
import backend.error.RecordNotFoundException;
import backend.model.Job;
import backend.model.Requisition;
import backend.repository.AdminRepository;
import backend.repository.CandidateRepository;
import backend.repository.JobRepository;
import backend.repository.RequisitionRepository;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
public class JobController
{
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RequisitionRepository requisitionRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/users")
    public ResponseEntity<ResponseMult<CandidateDTO>> getCands()
     {
        List<CandidateDTO> lstCandDTO = candidateRepository.findAll().stream().map(cand -> modelMapper.map(cand, CandidateDTO.class)).collect(Collectors.toList());
        ResponseMult<CandidateDTO> res = new ResponseMult<CandidateDTO>(HttpStatus.OK, "Success", lstCandDTO);
        return ResponseEntity.ok(res);
    }

    // Testing, getting all admins accounts
    @GetMapping("/admins")
    public ResponseEntity<ResponseMult<AdminDTO>> getAdminss() 
    {
        List<AdminDTO> lstAdminDTO = adminRepository.findAll().stream().map(ad -> modelMapper.map(ad, AdminDTO.class)).collect(Collectors.toList());
        ResponseMult<AdminDTO> res = new ResponseMult<AdminDTO>(HttpStatus.OK, "Success", lstAdminDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResponseMult<JobDTO>> getJobs()
    {
        List<JobDTO> lstJobDTO = jobRepository.findAll().stream().map(job -> modelMapper.map(job, JobDTO.class)).collect(Collectors.toList());
        ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", lstJobDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ResponseSingle<JobDTO>> getJob(@PathVariable("id") int id)
    {
        Job job = jobRepository.findById(id);
        if (job == null) throw new RecordNotFoundException();
        JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        ResponseSingle<JobDTO> res = new ResponseSingle<JobDTO>(HttpStatus.OK, "Success", jobDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs/{id}/requisitions")
    public ResponseEntity<ResponseMult<RequisitionDTO>> getJobReq(@PathVariable("id") int id)
    {
        Job job = jobRepository.findById(id);
        if (job == null) throw new RecordNotFoundException();
        //Force initialization
        Hibernate.initialize(job.getRequisitions());
        List<RequisitionDTO> lstReqDTO = job.getRequisitions().stream().map(req -> modelMapper.map(req, RequisitionDTO.class)).collect(Collectors.toList());
        ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/requisitions")
    public ResponseEntity<ResponseMult<RequisitionDTO>> getReqs()
    {
        List<RequisitionDTO> lstReqDTO = requisitionRepository.findAll().stream().map(req -> modelMapper.map(req, RequisitionDTO.class)).collect(Collectors.toList());
        ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/requisitions/{reqID}")
    public ResponseEntity<ResponseSingle<RequisitionDTO>> getReq(@PathVariable("reqID") int reqID)
    {
        Requisition req = requisitionRepository.findById(reqID);
        RequisitionDTO reqDTO = modelMapper.map(req, RequisitionDTO.class);
        ResponseSingle<RequisitionDTO> res = new ResponseSingle<RequisitionDTO>(HttpStatus.OK, "Success", reqDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/requisitions/{id}/applications")
    public ResponseEntity<ResponseSingle<RequisitionApplicationsDTO>> getApps(@PathVariable("id") int id)
    {
        Requisition req = requisitionRepository.findById(id);
        Hibernate.initialize(req.getApplications());
        RequisitionApplicationsDTO reqDTO = modelMapper.map(req, RequisitionApplicationsDTO.class);
        ResponseSingle<RequisitionApplicationsDTO> res = new ResponseSingle<RequisitionApplicationsDTO>(HttpStatus.OK, "Success", reqDTO);
        return ResponseEntity.ok(res);
    }
}