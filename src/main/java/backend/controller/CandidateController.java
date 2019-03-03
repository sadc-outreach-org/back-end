package backend.controller;

import backend.repository.*;
import backend.error.*;
import backend.model.Admin;
import backend.model.Candidate;
import backend.model.Job;
import backend.model.Profile;
import backend.model.Requisition;
import backend.model.UserType;
import backend.response.*;
import backend.request.*;
import java.io.IOException;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/user")
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/greeting")
    public String greeting() {
        return "This is HEB team greeting message";
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseMult<Profile>> getUsers() {
        Iterable<Profile> all = profileRepository.findAll();
        List<Profile> cands = new ArrayList<Profile>();
        all.forEach(cands::add);
        ResponseMult<Profile> res = new ResponseMult<Profile>(HttpStatus.OK, "Success", cands);
        return ResponseEntity.ok(res);
    }



    // Get a candidate with the specified email
    @GetMapping(path = "/{email}/info")
    public ResponseEntity<ResponseSingle<Candidate>> getUser(@PathVariable("email") String email) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }
    

    @GetMapping(path = "/byIDAdmin/{id}/info")
    public ResponseEntity<ResponseSingle<Admin>> getAdmin(@PathVariable("id") int id) {
        // Get the account with this email address
        Admin cand = adminRepository.findById(id);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            ResponseSingle<Admin> res = new ResponseSingle<Admin>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }

    @GetMapping("/usertypes")
    public List<UserType> getUserTypes() {
        List<UserType> types = new ArrayList<UserType>();
        Iterable<UserType> userTypes = userTypeRepository.findAll();
        userTypes.forEach(types::add);
        return types;
    }

    // Signup a user
    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<Candidate>> signUp(@RequestBody Signup signup) {
        // Check if email already in used
        if (candidateRepository.findByEmail(signup.getEmail()) != null) throw new EmailInUseException();
        
        //Set up new candidate user
        Candidate cand = new Candidate();
        cand.setCity(signup.getCity());
        cand.setGitLink(signup.getGitLink());
        cand.setState(signup.getState());
        cand.setStreetAddress(signup.getStreetAddress());
        cand.setZipcode(signup.getZipCode());

        // Set usertype to candidate
        UserType userType = new UserType();
        userType.setId(1);

        // Set the profile for candidate
        Profile profile = new Profile();
        profile.setEmail(signup.getEmail());
        profile.setFirstName(signup.getFirstName());
        profile.setLastName(signup.getLastName());
        profile.setPassword(passwordEncoder.encode(signup.getPassword()));
        profile.setPhoneNum(signup.getPhoneNum());
        profile.setUserType(userType);

        cand.setProfile(profile);

        // Check required fields
        if (!cand.getProfile().hasAllFields()) throw new MissingInfomationException(); 
        // Add user to the database
        else 
        {   
            // Save to database and exit with status code 200
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Signup Success", cand);
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
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            candidateRepository.delete(cand);
            APIResponse res = new APIResponse(HttpStatus.OK, "User " + email + " has been deleted");
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/{email}/requisitions")
    public ResponseEntity<ResponseMult<Requisition>> getReq(@PathVariable("email") String email)
    {
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            ResponseMult<Requisition> res = new ResponseMult<Requisition>(HttpStatus.OK, "Success", cand.getRequisitions());
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/{email}/jobs")
    public ResponseEntity<ResponseMult<Job>> getPos(@PathVariable("email") String email)
    {
        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            ResponseMult<Job> res = new ResponseMult<Job>(HttpStatus.OK, "Success", cand.getJobs());
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

        if (cand == null) throw new CandidateNotFoundException();
        if (cand.getResume() == null) throw new ResumeNotFoundException();

        // Candidate has resume
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + resume + "\"") // header to specify the name
                .body(new ByteArrayResource(cand.getResume())); // return the content of the file
    }

    
    @PostMapping("/{email}/resume")
    public ResponseEntity<ResponseSingle<Candidate>> uploadResume(@RequestParam MultipartFile file, @PathVariable("email") String email) throws IOException
    {

        Candidate cand = candidateRepository.findByEmail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            cand.setResume(file.getBytes());
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok().header("File-Uploaded", "resume.docx").body(res);
        }
    }
}