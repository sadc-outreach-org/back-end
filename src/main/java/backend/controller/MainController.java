package backend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.Utility.TimeUtility;
import backend.dto.AdminDTO;
import backend.dto.ApplicationAddJobDTO;
import backend.dto.ApplicationAddReqDTO;
import backend.dto.ApplicationDTO;
import backend.dto.CandidateDTO;
import backend.dto.CandidateSortDTO;
import backend.dto.JobDTO;
import backend.dto.LogInResultDTO;
import backend.dto.NotificationAddDTO;
import backend.dto.NotificationDTO;
import backend.dto.RequisitionAddDTO;
import backend.dto.RequisitionDTO;
import backend.error.InvalidLoginException;
import backend.error.JobExistsForCandidateException;
import backend.error.JobNotFoundException;
import backend.error.RecordNotFoundException;
import backend.error.UnexpectedDateTimeFormatException;
import backend.error.UserNotFoundException;
import backend.mapper.AdminMapper;
import backend.mapper.ApplicationMapper;
import backend.mapper.CandidateMapper;
import backend.mapper.JobMapper;
import backend.mapper.NotificationMapper;
import backend.mapper.RequisitionMapper;
import backend.model.Admin;
import backend.model.Application;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Notification;
import backend.model.Profile;
import backend.model.Requisition;
import backend.repository.AdminRepository;
import backend.repository.ApplicationRepository;
import backend.repository.CandidateRepository;
import backend.repository.JobRepository;
import backend.repository.NotificationRepository;
import backend.repository.ProfileRepository;
import backend.repository.RequisitionRepository;
import backend.repository.StatusRepository;
import backend.request.Login;
import backend.request.GitLink;
import backend.request.InterviewTime;
import backend.response.APIResponse;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;
import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@EnableEmailTools
@RestController
public class MainController
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

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    // Testing, getting all admins accounts
    @GetMapping("/admins")
    public ResponseEntity<ResponseMult<AdminDTO>> getAdminss() 
    {
        List<AdminDTO> lstAdminDTO = adminRepository.findAll().stream().map(ad -> AdminMapper.MAPPER.adminToAdminDTO(ad)).collect(Collectors.toList());
        ResponseMult<AdminDTO> res = new ResponseMult<AdminDTO>(HttpStatus.OK, "Success", lstAdminDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/jobs")
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
        requisition.setOpen(true);
        requisitionRepository.save(requisition);
        APIResponse res = new APIResponse(HttpStatus.OK, "A requisition has been added for job with ID : " + requisition.getJob().getJobID());
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

    @GetMapping("/requisitions/{reqID}/applications")
    public ResponseEntity<ResponseMult<ApplicationDTO>> getReqApps(@PathVariable("reqID") int reqID)
    {
        Requisition req = requisitionRepository.findById(reqID);
        Hibernate.initialize(req.getApplications());
        List<ApplicationDTO> lstAppDTO = req.getApplications().stream().map(app -> ApplicationMapper.MAPPER.applicationToApplicationDTO(app)).collect(Collectors.toList());
        ResponseMult<ApplicationDTO> res = new ResponseMult<ApplicationDTO>(HttpStatus.OK, "Success", lstAppDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("requisitions/{reqID}/applications")
    public ResponseEntity<APIResponse> addApplicationReq(@PathVariable("reqID") int reqID, @RequestBody ApplicationAddReqDTO appAddDTO)
    {
        Application app = ApplicationMapper.MAPPER.applicationAddReqDTOToApplication(appAddDTO);
        app.setStatus(statusRepository.findById(1));
        applicationRepository.save(app);
        APIResponse res = new APIResponse(HttpStatus.OK, "An application has been added for candidate with ID " + app.getCandidate().getCandidateID() 
                                        + " for requisition with ID :" + app.getRequisition().getRequisitionID());
        return ResponseEntity.ok(res);
    }

    @PostMapping("jobs/{jobID}/applications")
    public ResponseEntity<APIResponse> addApplicationJob(@PathVariable("jobID") int jobID, @RequestBody ApplicationAddJobDTO appAddJobDTO)
    {
        
        Candidate cand          = candidateRepository.findById(appAddJobDTO.getCandidateID());
        if (cand == null) throw new UserNotFoundException();
        Hibernate.initialize(cand.getJobs());
        Set<Job> jobs = cand.getJobs();
        Job job = jobRepository.findById(appAddJobDTO.getJobID());
        if (job == null) throw new JobNotFoundException();
        //if (jobs.contains(job)) throw new JobExistsForCandidateException();
        jobs.add(job);
        candidateRepository.save(cand);
        List<Requisition> reqs  = requisitionRepository.findOpenRequisitionByJobID(appAddJobDTO.getJobID());
        Application app         = new Application();
        app.setCandidate(cand);
        app.setRequisition(reqs.get(0));
        app.setStatus(statusRepository.findById(1));
        applicationRepository.save(app);
        APIResponse res         = new APIResponse(HttpStatus.OK, "An application has been added for candidate with ID " + app.getCandidate().getCandidateID() 
                                        + " for requisition with ID: " + app.getRequisition().getRequisitionID());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/applications/{appID}")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> getApp(@PathVariable("appID") int appID)
    {
        Application app                     = applicationRepository.findById(appID);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Success", appDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/applications/{appID}/gitLink")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> postGitLink(@PathVariable("appID") int appID, @RequestBody GitLink gitLink)
    {
        Application app                     = applicationRepository.findById(appID);
        // If already candidate already beyond the submit git link step, don't change the status
        if (app.getStatus().getStatusID() <= 1)
            app.setStatus(statusRepository.findById(2));
        app.setGitLink(gitLink.getGitLink());
        applicationRepository.save(app);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Application has been updated", appDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/applications/{appID}/interviewTime")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> postInterviewTime(@PathVariable("appID") int appID, @RequestBody InterviewTime interviewTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeUtility.simpleDateTimeFormat);
        Application app                     = applicationRepository.findById(appID);
        LocalDateTime localDateTime         = null;
        // If already candidate already beyond the submit git link step, don't change the status
        if (app.getStatus().getStatusID() <= 2)
            app.setStatus(statusRepository.findById(3));

        try 
        {
            localDateTime         = LocalDateTime.parse(interviewTime.getInterviewTime(), formatter);
        }
        catch (DateTimeParseException ex)
        {
            throw new UnexpectedDateTimeFormatException();
        }
        app.setInterviewTime(localDateTime);
        applicationRepository.save(app);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Application has been updated", appDTO);
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
    (@RequestParam(name = "orderBy", defaultValue = "statusID") String orderBy, @RequestParam (name = "sort", defaultValue = "ASC") String sort)
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
        +           "(Select c.userID as candidateID, u.email, u.firstName, u.LastName "
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

    
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<LogInResultDTO>> logIn(@RequestBody Login attempt)
    {
        if (attempt.getPassword() == null || attempt.getEmail() == null)
            throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Candidate cand = candidateRepository.findByEmail(attempt.getEmail());
        LogInResultDTO user = null;
        if ((cand != null) && (passwordEncoder.matches(attempt.getPassword(), cand.getProfile().getPassword())))
        {
            user = CandidateMapper.MAPPER.candidateToLogInResultDTO(cand);
            user.setRole("Candidate");
        }
        else
        {
            Admin admin = adminRepository.findByEmail(attempt.getEmail());
            if ((admin != null) && (passwordEncoder.matches(attempt.getPassword(), admin.getProfile().getPassword())))
            {
                user = AdminMapper.MAPPER.adminToLogInResultDTO(admin);
                user.setRole("Admin");
            }
            else
            {
                throw new InvalidLoginException();
            }
        }
        ResponseSingle<LogInResultDTO> res = new ResponseSingle<LogInResultDTO>(HttpStatus.OK, "Success", user);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/notifications")
    public ResponseEntity<APIResponse> addNotification(@RequestBody NotificationAddDTO notificationAddDTO)
    {
        Notification notification   = NotificationMapper.MAPPER.notificationAddDTOToNotification(notificationAddDTO);
        notificationRepository.save(notification);
        APIResponse res  = new APIResponse(HttpStatus.OK, "A notification has been added to userID : " + notificationAddDTO.getUserID());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/notifications/{notificationID}")
    public ResponseEntity<APIResponse> updateNotification(@PathVariable("notificationID") int notificationID)
    {
        Notification notification   = notificationRepository.findById(notificationID);
        notification.setHasRead(true);
        notificationRepository.save(notification);
        APIResponse res  = new APIResponse(HttpStatus.OK, "Notification " + notificationID + " has been marked as read");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/{userID}/notifications")
    public ResponseEntity<ResponseMult<NotificationDTO>> getNotifications(@PathVariable("userID") int userID)
    {
        Profile user = profileRepository.findById(userID);
        if (user == null)
            throw new UserNotFoundException();
        Hibernate.initialize(user.getNotifications());
        List<NotificationDTO> notificationDTOs      = user.getNotifications().stream().map(notification -> NotificationMapper.MAPPER.notificationToNotificationDTO(notification)).collect(Collectors.toList());
        ResponseMult<NotificationDTO> res            = new ResponseMult<NotificationDTO>(HttpStatus.OK, "Success", notificationDTOs);
        return ResponseEntity.ok(res);
    }

}