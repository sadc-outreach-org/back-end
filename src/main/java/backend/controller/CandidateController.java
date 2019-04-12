package backend.controller;

import backend.repository.*;
import backend.Utility.EmailServiceService;
import backend.dto.ApplicationDTO;
import backend.dto.CandidateDTO;
import backend.dto.JobDTO;
import backend.error.*;
import backend.mapper.ApplicationMapper;
import backend.mapper.CandidateMapper;
import backend.mapper.JobMapper;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Profile;
import backend.model.UserType;
import backend.response.*;
import backend.request.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.text.RandomStringGenerator;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/users")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private EmailServiceService emailServiceService;

    @Autowired
    private RandomStringGenerator randomStringGenerator;

    @RequestMapping("/greeting")
    public String greeting() {
        return "This is HEB team greeting message";

    }

    @GetMapping("Test")
    public Iterable<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseSingle<CandidateDTO>> getUser(@PathVariable("id") int id) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findById(id);
        if (cand == null)
            throw new UserNotFoundException();

        CandidateDTO candDTO = CandidateMapper.MAPPER.candidateToCandidateDTO(cand);
        ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Success", candDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseSingle<CandidateDTO>> updateCandidate(@PathVariable("id") int id, @RequestBody CandidateDTO candDTO)
    {
        Candidate cand  = candidateRepository.findById(id);
        if (cand == null) 
            throw new UserNotFoundException();
        cand            = CandidateMapper.MAPPER.updateCandidateFromCandidateDTO(candDTO, cand);
        candidateRepository.save(cand);
        CandidateDTO returnDTO  = CandidateMapper.MAPPER.candidateToCandidateDTO(cand);
        ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "User with id " + id + " has been updated", returnDTO);
        return ResponseEntity.ok(res);
    }

    // Signup a user
    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<CandidateDTO>> signUp(@RequestBody CandidateDTO candDTO) {
        // Check if email already in used
        if (profileRepository.findByEmail(candDTO.getEmail()) != null)
            throw new EmailInUseException();

        UserType candUserType = userTypeRepository.findById(1);

        Candidate cand = CandidateMapper.MAPPER.candidateDTOToCandidate(candDTO);
        cand.getProfile().setUserType(candUserType);

        // Check required fields
        if (!cand.getProfile().hasAllFields())
            throw new MissingInfomationException();
        // Add user to the database
        else {
            // Set and encode temp password, save to database and exit with status code 200
            String tempPW   = randomStringGenerator.generate(10);
            cand.getProfile().setTempPW(passwordEncoder.encode(tempPW));
            cand.getProfile().setIsLocked(true);
            candidateRepository.save(cand);
            candDTO.setCandidateID(cand.getCandidateID());
            ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Signup Success",
                    candDTO);
            try {
                emailServiceService.sendSignUpEmail(candDTO.getEmail(), candDTO.getFirstName(), tempPW);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(res);
        }
    }

    // Process a login attempt, return an exception if login with incorrect
    // credentials
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<CandidateDTO>> login(@RequestBody Login attempt) {
        if (attempt.getPassword() == null || attempt.getEmail() == null)
            throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Candidate cand = candidateRepository.findByEmail(attempt.getEmail());
        if ((cand != null) && (passwordEncoder.matches(attempt.getPassword(), cand.getProfile().getPassword()))) {
            CandidateDTO candDTO = CandidateMapper.MAPPER.candidateToCandidateDTO(cand);
            ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Success", candDTO);
            return ResponseEntity.ok(res);
        } else
            throw new InvalidLoginException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable("id") int id) {
        Profile profile = profileRepository.findById(id);
        if (profile == null)
            throw new UserNotFoundException();
        profileRepository.delete(profile);
        APIResponse res = new APIResponse(HttpStatus.OK,
                "User with email " + profile.getEmail() + " and id " + id + " has been deleted");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<ResponseMult<ApplicationDTO>> getApps(@PathVariable("id") int id) {
        Candidate cand = candidateRepository.findById(id);
        if (cand == null)
            throw new UserNotFoundException();
        else 
        {
            Hibernate.initialize(cand.getApplications());
            //List<ApplicationNoCandidateDTO> lstApps = cand.getApplications().stream().map(app -> modelMapper.map(app, ApplicationNoCandidateDTO.class)).collect(Collectors.toList());
            List<ApplicationDTO> lstApps = cand.getApplications().stream().map(app -> ApplicationMapper.MAPPER.applicationToApplicationDTO(app)).collect(Collectors.toList());
            ResponseMult<ApplicationDTO> res = new ResponseMult<ApplicationDTO>(HttpStatus.OK, "Success", lstApps);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<ResponseMult<JobDTO>> getJobs(@PathVariable("id") int id)
    {
        Candidate cand = candidateRepository.findById(id);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            Hibernate.initialize(cand.getJobs());
            List<JobDTO> jobDTOs = cand.getJobs().stream().map(job -> JobMapper.MAPPER.jobToJobDTO(job)).collect(Collectors.toList());
            ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", jobDTOs);
            return ResponseEntity.ok(res);
        }
    }

    @PostMapping("/{id}/jobs")
    public ResponseEntity<APIResponse> addJobs(@RequestBody CandToJob candToJob)
    {
        Candidate cand = candidateRepository.findById(candToJob.getCandidateID());
        if (cand == null) throw new UserNotFoundException();
        else
        {
            Hibernate.initialize(cand.getJobs());
            Set<Job> jobs = cand.getJobs();

            // Check if job exists and that user has not applied for this job already
            Job job = jobRepository.findById(candToJob.getJobID());
            if (job == null) throw new JobNotFoundException();
            if (jobs.contains(job)) throw new JobExistsForCandidateException();

            jobs.add(job);
            candidateRepository.save(cand);
            APIResponse res = new APIResponse(HttpStatus.OK, "Job for " + job.getTitle() + " has been added to candidate with ID : " + cand.getCandidateID());
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping(
        value = "/{id}/resume",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
        )
    public ResponseEntity<ByteArrayResource> getResume(@PathVariable("id") int id) {
        Candidate cand = candidateRepository.findById(id);
        //Currently has no way to get the file original name, might have to change database to accomodate this feature, assuming all files are pdf
        String resume = "resume.pdf";

        if (cand == null) throw new UserNotFoundException();
        if (cand.getResume() == null) throw new ResumeNotFoundException();

        // Candidate has resume
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + resume + "\"") // header to specify the name
                .body(new ByteArrayResource(cand.getResume())); // return the content of the file
    }

    
    @PostMapping("/{id}/resume")
    public ResponseEntity<ResponseSingle<Candidate>> uploadResume(@RequestParam MultipartFile file, @PathVariable("id") int id) throws IOException
    {

        Candidate cand = candidateRepository.findById(id);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            cand.setResume(file.getBytes());
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok().header("File-Uploaded", "resume.docx").body(res);
        }
    }



}