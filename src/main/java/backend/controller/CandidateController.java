package backend.controller;

import backend.repository.*;
import backend.error.*;
import backend.model.Candidate;
import backend.response.*;
import backend.request.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;


@RestController
@RequestMapping("/user")
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;

    @RequestMapping("/greeting")
    public String greeting() {
        return "This is HEB team greeting message";
    }

    // Get a list of all users along with their information
    @GetMapping("/users")
    public ResponseEntity<ResponseMult<Candidate>> getUsers() {
        Iterable<Candidate> all = candidateRepository.findAll();
        List<Candidate> cands = new ArrayList<Candidate>();
        all.forEach(cands::add);
        ResponseMult<Candidate> res = new ResponseMult<Candidate>(HttpStatus.OK, "Success", cands);
        return ResponseEntity.ok(res);
    }

    // Get information from a specific user, using their email address as the value
    @GetMapping(path = "/{email}/info")
    public ResponseEntity<ResponseSingle<Candidate>> getUser(@PathVariable("email") String email) {
        // Get the account with this email address
        Candidate cand = candidateRepository.findOneByemail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }

    // Check if a login is success or failure
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<Candidate>> login(@RequestBody Login attempt) {
        if (attempt.getPassword() == null || attempt.getEmail() == null) throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Candidate cand = candidateRepository.findOneByemail(attempt.getEmail());
        if ((cand != null) && (cand.getPassword().equals(attempt.getPassword()))) 
        {
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        }
        else throw new InvalidLoginException();
    }

    // Signup a user
    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<Candidate>> signUp(@RequestBody Candidate cand) {
        // Check if email already in used
        if (candidateRepository.findOneByemail(cand.getEmail()) != null) throw new EmailInUseException();
        // Validation check for all the required fields
        else if (cand.checkEmpty()) throw new MissingInfomationException(); 
        // Add user to the database
        else 
        {
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Signup Success", cand);
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping(
        value = "/{email}/resume",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
        )
    public ResponseEntity<ByteArrayResource> getResume(@PathVariable("email") String email) {
        Candidate cand = candidateRepository.findOneByemail(email);
        //Currently has no way to get the file original name, might have to change database to accomodate this feature, and not all files are .docx neither
        String resume = "resume.docx";

        if (cand == null) throw new CandidateNotFoundException();
        if (cand.getResume() == null) throw new ResumeNotFoundException();

        // Candidate has resume
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + resume + "\"") // header to specify the name
                .body(new ByteArrayResource(cand.getResume())); // return the content of the file
    }

    @PostMapping("/{email}/resume")
    public ResponseEntity<ResponseSingle<Candidate>> uploadResume(@RequestParam MultipartFile file, @PathVariable("email") String email) throws IOException
    {

        Candidate cand = candidateRepository.findOneByemail(email);
        if (cand == null) throw new CandidateNotFoundException();
        else
        {
            cand.setResume(file.getBytes());
            candidateRepository.save(cand);
            ResponseSingle<Candidate> res = new ResponseSingle<Candidate>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok().header("File-Uploaded", "resume.docx").body(res);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable("email") String email)
    {
        long deleted = candidateRepository.deleteByEmail(email);
        if (deleted == 0) throw new CandidateNotFoundException();
        else
        {
            APIResponse res = new APIResponse(HttpStatus.OK, "User " + email + " has been deleted");
            return ResponseEntity.ok(res);
        }
    }








    @GetMapping("/resume/test")
    public ResponseEntity<ByteArrayResource> getResumeFromCandidate() throws IOException
    {
        //Will change this later
        //Will have to query for the path of their resume from the database
        String resume = "template.docx";
        Path path = Paths.get("C:\\Users\\thoai bui\\Documents\\Resume\\template.pdf");
        //ByteArrayResource resume = ;
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + resume + "\"") //header to specify the name
                                    .contentType(MediaType.parseMediaType("application/octet-stream"))//Specify that it is a pdf file
                                    .body(new ByteArrayResource(Files.readAllBytes(path))); //return the content of the file 
    }
}