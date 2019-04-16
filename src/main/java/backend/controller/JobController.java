package backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.Utility.EmailService;
import backend.dto.JobDTO;
import backend.dto.RequisitionSummaryDTO;
import backend.error.JobNotFoundException;
import backend.error.RecordNotFoundException;
import backend.error.UserNotFoundException;
import backend.mapper.JobMapper;
import backend.mapper.RequisitionMapper;
import backend.model.Application;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Requisition;
import backend.repository.ApplicationRepository;
import backend.repository.CandidateRepository;
import backend.repository.JobRepository;
import backend.repository.RequisitionRepository;
import backend.repository.StatusRepository;
import backend.request.ApplicationJob;
import backend.response.APIResponse;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
@RequestMapping("/jobs")
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
    private StatusRepository statusRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public ResponseEntity<ResponseMult<JobDTO>> getJobs(@RequestParam(name = "open", defaultValue = "none") String open)
    {
        List<JobDTO> lstJobDTO;
        if (open.equalsIgnoreCase("none"))
            lstJobDTO = jobRepository.findAll().stream().map(job -> JobMapper.MAPPER.jobToJobDTO(job)).collect(Collectors.toList());
        else
        {
            String query = 
            "SELECT Job.*" 
            + " FROM (SELECT DISTINCT jobID FROM Requisition WHERE isOpen=" + open + ") as openJobs"
            + " LEFT OUTER JOIN Job"
            + " ON openJobs.jobID = Job.jobID;";
            lstJobDTO = em.createNativeQuery(query, "JobDTO").getResultList();
        }
        ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", lstJobDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<JobDTO>> getJob(@PathVariable("id") int id)
    {
        Job job = jobRepository.findById(id);
        if (job == null) throw new RecordNotFoundException();
        JobDTO jobDTO = JobMapper.MAPPER.jobToJobDTO(job);
        ResponseSingle<JobDTO> res = new ResponseSingle<JobDTO>(HttpStatus.OK, "Success", jobDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/requisitions")
    public ResponseEntity<ResponseMult<RequisitionSummaryDTO>> getJobReq(@PathVariable("id") int id)
    {
        Job job = jobRepository.findById(id);
        if (job == null) throw new RecordNotFoundException();
        //Force initialization
        Hibernate.initialize(job.getRequisitions());
        List<RequisitionSummaryDTO> lstReqDTO = job.getRequisitions().stream().map(req -> RequisitionMapper.MAPPER.requisitionToRequisitionSummaryDTO(req)).collect(Collectors.toList());
        ResponseMult<RequisitionSummaryDTO> res = new ResponseMult<RequisitionSummaryDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }



    @PostMapping("/{jobID}/applications")
    public ResponseEntity<APIResponse> addApplicationJob(@PathVariable("jobID") int jobID, @RequestBody ApplicationJob appJob)
    {
        
        Candidate cand          = candidateRepository.findById(appJob.getCandidateID());
        if (cand == null) throw new UserNotFoundException();
        Hibernate.initialize(cand.getJobs());
        Set<Job> jobs = cand.getJobs();
        Job job = jobRepository.findById(appJob.getJobID());
        if (job == null) throw new JobNotFoundException();
        //if (jobs.contains(job)) throw new JobExistsForCandidateException();
        jobs.add(job);
        candidateRepository.save(cand);
        List<Requisition> reqs  = requisitionRepository.findOpenRequisitionByJobID(appJob.getJobID());
        Application app         = new Application();
        app.setCandidate(cand);
        app.setRequisition(reqs.get(0));
        app.setStatus(statusRepository.findById(1));
        app.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(app);
        emailService.sendApplicationEmail(cand.getProfile().getEmail(), cand.getProfile().getFirstName(), app);
        APIResponse res         = new APIResponse(HttpStatus.OK, "An application has been added for candidate with ID " + app.getCandidate().getCandidateID() 
                                        + " for requisition with ID: " + app.getRequisition().getRequisitionID());
        return ResponseEntity.ok(res);
    }
}