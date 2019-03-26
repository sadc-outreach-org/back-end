package backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import backend.dto.AdminDTO;
import backend.dto.ApplicationAddDTO;
import backend.dto.ApplicationDTO;
import backend.dto.CandidateDTO;
import backend.dto.CandidateSortDTO;
import backend.dto.JobDTO;
import backend.dto.RequisitionAddDTO;
import backend.dto.RequisitionDTO;
import backend.error.RecordNotFoundException;
import backend.mapper.AdminMapper;
import backend.mapper.ApplicationMapper;
import backend.mapper.CandidateMapper;
import backend.mapper.JobMapper;
import backend.mapper.RequisitionMapper;
import backend.model.Application;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Requisition;
import backend.model.Status;
import backend.repository.AdminRepository;
import backend.repository.ApplicationRepository;
import backend.repository.CandidateRepository;
import backend.repository.JobRepository;
import backend.repository.RequisitionRepository;
import backend.repository.StatusRepository;
import backend.response.APIResponse;
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
    private ApplicationRepository applicationRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StatusRepository statusRepository;

    @PersistenceContext
    private EntityManager em;

    /*
    @GetMapping("/users")
    public ResponseEntity<ResponseMult<CandidateSummaryDTO>> getCands()
     {
        List<CandidateSummaryDTO> lstCandDTO = candidateRepository.findAll().stream().map(cand -> modelMapper.map(cand, CandidateSummaryDTO.class)).collect(Collectors.toList());
        ResponseMult<CandidateSummaryDTO> res = new ResponseMult<CandidateSummaryDTO>(HttpStatus.OK, "Success", lstCandDTO);
        return ResponseEntity.ok(res);
     }*/

    // Testing, getting all admins accounts
    @GetMapping("/admins")
    public ResponseEntity<ResponseMult<AdminDTO>> getAdminss() 
    {
        List<AdminDTO> lstAdminDTO = adminRepository.findAll().stream().map(ad -> AdminMapper.MAPPER.adminToAdminDTO(ad)).collect(Collectors.toList());
        ResponseMult<AdminDTO> res = new ResponseMult<AdminDTO>(HttpStatus.OK, "Success", lstAdminDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResponseMult<JobDTO>> getJobs()
    {
        List<JobDTO> lstJobDTO = jobRepository.findAll().stream().map(job -> JobMapper.MAPPER.jobToJobDTO(job)).collect(Collectors.toList());
        ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", lstJobDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ResponseSingle<JobDTO>> getJob(@PathVariable("id") int id)
    {
        Job job = jobRepository.findById(id);
        if (job == null) throw new RecordNotFoundException();
        JobDTO jobDTO = JobMapper.MAPPER.jobToJobDTO(job);
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
        List<RequisitionDTO> lstReqDTO = job.getRequisitions().stream().map(req -> RequisitionMapper.MAPPER.requisitionToRequisitionDTO(req)).collect(Collectors.toList());
        ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/requisitions")
    public ResponseEntity<ResponseMult<RequisitionDTO>> getReqs()
    {
        List<RequisitionDTO> lstReqDTO = requisitionRepository.findAll().stream().map(req -> RequisitionMapper.MAPPER.requisitionToRequisitionDTO(req)).collect(Collectors.toList());
        ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/requisitions")
    public ResponseEntity<APIResponse> addRequisition(@RequestBody RequisitionAddDTO requisitionAddDTO)
    {
        Requisition requisition = RequisitionMapper.MAPPER.requisitionAddDTOToRequisition(requisitionAddDTO);
        requisitionRepository.save(requisition);
        APIResponse res = new APIResponse(HttpStatus.OK, "A requisition has been added");
        return ResponseEntity.ok(res);

    }


    @GetMapping("/requisitions/{reqID}")
    public ResponseEntity<ResponseSingle<RequisitionDTO>> getReq(@PathVariable("reqID") int reqID)
    {
        Requisition req = requisitionRepository.findById(reqID);
        RequisitionDTO reqDTO = RequisitionMapper.MAPPER.requisitionToRequisitionDTO(req);
        ResponseSingle<RequisitionDTO> res = new ResponseSingle<RequisitionDTO>(HttpStatus.OK, "Success", reqDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/requisitions/{id}/applications")
    public ResponseEntity<ResponseMult<ApplicationDTO>> getReqApps(@PathVariable("id") int id)
    {
        Requisition req = requisitionRepository.findById(id);
        Hibernate.initialize(req.getApplications());
        List<ApplicationDTO> lstAppDTO = req.getApplications().stream().map(app -> ApplicationMapper.MAPPER.applicationToApplicationDTO(app)).collect(Collectors.toList());
        ResponseMult<ApplicationDTO> res = new ResponseMult<ApplicationDTO>(HttpStatus.OK, "Success", lstAppDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/applications")
    public ResponseEntity<APIResponse> addApplication(@RequestBody ApplicationAddDTO appAddDTO)
    {
        Application app = ApplicationMapper.MAPPER.applicationAddDTOToApplication(appAddDTO);
        Status status   = statusRepository.findById(1);
        app.setStatus(status);
        applicationRepository.save(app);
        APIResponse res = new APIResponse(HttpStatus.OK, "An application has been added");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> getApp(@PathVariable("id") int id)
    {
        Application app = applicationRepository.findById(id);
        ApplicationDTO appDTO = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Success", appDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/test")
    public CandidateDTO getCandsSort()
    {
        Candidate cand = candidateRepository.findById(3);
        CandidateDTO candDTO = CandidateMapper.MAPPER.candidateToCandidateDTO(cand);
        return candDTO;
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseMult<CandidateSortDTO>> getCandsSortQuery
    (@RequestParam(name = "orderBy", defaultValue = "candidateID") String orderBy, @RequestParam (name = "sort", defaultValue = "ASC") String sort)
    {
        // SQL since candidate table does not hold a reference to latest status
        if (orderBy.equalsIgnoreCase("status"))
            orderBy = "statusID";
        String query = 
        " Select c.*, s.status FROM "
        +   "(Select cand.*, appMax.statusID FROM "
        +       "(Select candidateID, MAX(statusID) as statusID "
        +           "From `Application` "
        +           "Group By candidateID) appMax "
        +       "Right Outer Join "
        +           "(Select c.candidateID, u.email, u.firstName, u.LastName "
        +           "FROM `Candidate` c "
        +           "Inner Join " 
        +                "User u "
        +           "On c.userID = u.userID) cand "
        +        "On cand.candidateID = appMax.candidateID "
        +   ") c "
        +"Left Outer Join "
        +   "`Status` s "
        +"On c.statusId = s.statusID "
        +"Order By " + orderBy  + " " + sort;
        List<CandidateSortDTO> lstCandSortDTO = em.createNativeQuery(query, "CandidateSortDTO").getResultList();
        ResponseMult<CandidateSortDTO>  res = new ResponseMult<CandidateSortDTO>(HttpStatus.OK, "Success", lstCandSortDTO);
        return ResponseEntity.ok(res);
    }
}