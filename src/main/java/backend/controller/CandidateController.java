package backend.controller;

import backend.repository.*;
import backend.dto.CandidateDTO;
import backend.dto.JobDTO;
import backend.dto.RequisitionDTO;
import backend.error.*;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Requisition;
import backend.model.UserType;
import backend.response.*;
import backend.request.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
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
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    

    @RequestMapping("/greeting")
    public String greeting() {
        return "This is HEB team greeting message";
        
    }

    @PersistenceContext
    EntityManager entityManager;

    @GetMapping("Test")
    public Iterable<Candidate> getAll()
    {
        return candidateRepository.findAll();
    }


    // Get a candidate with the specified email
    @GetMapping(path = "/{email}/info")
    public ResponseEntity<ResponseSingle<Candidate>> getUser(@PathVariable("email") String email) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }

    @GetMapping(path = "/{id}/")
    public ResponseEntity<ResponseSingle<Candidate>> getUser(@PathVariable("id") int id) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findById(id);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }
    
    // Signup a user
    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<CandidateDTO>> signUp(@RequestBody CandidateDTO candDTO) {
        // Check if email already in used
        if (candidateRepository.findByEmail(candDTO.getEmail()) != null) throw new EmailInUseException();
        
        UserType candUserType = new UserType();
        candUserType.setUserTypeID(1);

        Candidate cand = modelMapper.map(candDTO, Candidate.class);
        cand.getProfile().setUserType(candUserType);

        // Check required fields
        if (!cand.getProfile().hasAllFields()) throw new MissingInfomationException(); 
        // Add user to the database
        else 
        {   
            // Save to database and exit with status code 200
            candidateRepository.save(cand);
            ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Signup Success", candDTO);
            return ResponseEntity.ok(res);
        }
    }    


    // Process a login attempt, return an exception if login with incorrect credentials
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<Candidate>> login(@RequestBody Login attempt) {
        if (attempt.getPassword() == null || attempt.getEmail() == null) throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Candidate cand = candidateRepository.findByEmail(attempt.getEmail());
        if ( (cand != null) &&
            (passwordEncoder.matches(attempt.getPassword(), cand.getProfile().getPassword())) )
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        }
        else throw new InvalidLoginException();
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable("email") String email)
    {
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new UserNotFoundException();
        candidateRepository.delete(cand);
        APIResponse res = new APIResponse(HttpStatus.OK, "User " + email + " has been deleted");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{email}/requisitions")
    public ResponseEntity<ResponseMult<RequisitionDTO>> getReq(@PathVariable("email") String email)
    {
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            Hibernate.initialize(cand.getRequisitions());
            List<Requisition> reqs = cand.getRequisitions();
            List<RequisitionDTO> reqDTOs = reqs.stream().map(req -> modelMapper.map(req, RequisitionDTO.class)).collect(Collectors.toList());
            ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", reqDTOs);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/{email}/jobs")
    public ResponseEntity<ResponseMult<JobDTO>> getJobs(@PathVariable("email") String email)
    {
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            Hibernate.initialize(cand.getJobs());
            List<Job> jobs = cand.getJobs();
            List<JobDTO> jobDTOs = new ArrayList<JobDTO>();
            for (Job job : jobs)
                jobDTOs.add(modelMapper.map(job, JobDTO.class));
            ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", jobDTOs);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping(
        value = "/{email}/resume",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
        )
    public ResponseEntity<ByteArrayResource> getResume(@PathVariable("email") String email) {
        Candidate cand = candidateRepository.findByEmail(email);
        //Currently has no way to get the file original name, might have to change database to accomodate this feature, assuming all files are pdf
        String resume = "resume.pdf";

        if (cand == null) throw new UserNotFoundException();
        if (cand.getResume() == null) throw new ResumeNotFoundException();

        // Candidate has resume
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + resume + "\"") // header to specify the name
                .body(new ByteArrayResource(cand.getResume())); // return the content of the file
    }

    
    @PostMapping("/{email}/resume")
    public ResponseEntity<ResponseSingle<Candidate>> uploadResume(@RequestParam MultipartFile file, @PathVariable("email") String email) throws IOException
    {

        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            cand.setResume(file.getBytes());
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok().header("File-Uploaded", "resume.docx").body(res);
        }
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> updateCandidate(@RequestBody Candidate cand)
    {
        if (cand == null) throw new UserNotFoundException();
        candidateRepository.save(cand);
        APIResponse res = new APIResponse(HttpStatus.OK, "User " + cand.getProfile().getEmail() + " has been updated");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/testMapper")
    public Candidate checkMapper(@RequestBody CandidateDTO candDTO) 
    {
        System.out.println(candDTO.getEmail());
        System.out.println(candDTO.getStreetAddress());
        Candidate cand = modelMapper.map(candDTO, Candidate.class);
        System.out.println(cand.getProfile().getEmail());
        System.out.println(cand.getStreetAddress());
        return cand;
    }
}