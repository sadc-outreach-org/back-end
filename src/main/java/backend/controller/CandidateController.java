package backend.controller;

import backend.repository.*;
import backend.dto.ApplicationNoCandidateDTO;
import backend.dto.CandidateDTO;
import backend.dto.JobDTO;
import backend.error.*;
import backend.model.Candidate;
import backend.model.UserType;
import backend.response.*;
import backend.request.*;
import java.io.IOException;
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
    public Iterable<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    // Get a candidate with the specified email
    @GetMapping(path = "/{email}/info")
    public ResponseEntity<ResponseSingle<Candidate>> getUser(@PathVariable("email") String email) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null)
            throw new UserNotFoundException();
        else {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseSingle<CandidateDTO>> getUser(@PathVariable("id") int id) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findById(id);
        if (cand == null)
            throw new UserNotFoundException();

        CandidateDTO candDTO = modelMapper.map(cand, CandidateDTO.class);
        ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Success", candDTO);
        return ResponseEntity.ok(res);
    }

    // Signup a user
    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<CandidateDTO>> signUp(@RequestBody CandidateDTO candDTO) {
        // Check if email already in used
        if (candidateRepository.findByEmail(candDTO.getEmail()) != null)
            throw new EmailInUseException();

        UserType candUserType = new UserType();
        candUserType.setUserTypeID(1);

        Candidate cand = modelMapper.map(candDTO, Candidate.class);
        cand.getProfile().setUserType(candUserType);

        // Check required fields
        if (!cand.getProfile().hasAllFields())
            throw new MissingInfomationException();
        // Add user to the database
        else {
            // Save to database and exit with status code 200
            candidateRepository.save(cand);
            ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Signup Success",
                    candDTO);
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
            CandidateDTO candDTO = modelMapper.map(cand, CandidateDTO.class);
            ResponseSingle<CandidateDTO> res = new ResponseSingle<CandidateDTO>(HttpStatus.OK, "Success", candDTO);
            return ResponseEntity.ok(res);
        } else
            throw new InvalidLoginException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable("id") int id) {
        Candidate cand = candidateRepository.findById(id);
        if (cand == null)
            throw new UserNotFoundException();
        candidateRepository.delete(cand);
        APIResponse res = new APIResponse(HttpStatus.OK,
                "User with email " + cand.getProfile().getEmail() + " and id " + id + " has been deleted");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<ResponseMult<ApplicationNoCandidateDTO>> getApps(@PathVariable("id") int id) {
        Candidate cand = candidateRepository.findById(id);
        if (cand == null)
            throw new UserNotFoundException();
        else 
        {
            Hibernate.initialize(cand.getApplications());
            List<ApplicationNoCandidateDTO> lstApps = cand.getApplications().stream().map(app -> modelMapper.map(app, ApplicationNoCandidateDTO.class)).collect(Collectors.toList());
            ResponseMult<ApplicationNoCandidateDTO> res = new ResponseMult<ApplicationNoCandidateDTO>(HttpStatus.OK, "Success", lstApps);
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
            List<JobDTO> jobDTOs = cand.getJobs().stream().map(job -> modelMapper.map(job, JobDTO.class)).collect(Collectors.toList());
            ResponseMult<JobDTO> res = new ResponseMult<JobDTO>(HttpStatus.OK, "Success", jobDTOs);
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