package backend.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.error.InvalidLoginException;
import backend.error.UserNotFoundException;
import backend.model.Admin;
import backend.model.Requisition;
import backend.repository.AdminRepository;
import backend.request.Login;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
@RequestMapping("/admins")
public class AdminController 
{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @GetMapping(path = "/{id}/info")
    public ResponseEntity<ResponseSingle<Admin>> getAdminByID(@PathVariable("id") int id) {
        // Get the account with this email address
        Admin cand = adminRepository.findById(id);
        if (cand == null) throw new UserNotFoundException();
        else
        {
            ResponseSingle<Admin> res = new ResponseSingle<Admin>(HttpStatus.OK, "Success", cand);
            return ResponseEntity.ok(res);
        } 
    }

    @GetMapping(path = "/{email}/info")
    public ResponseEntity<ResponseSingle<Admin>> getAdminByEmail(@PathVariable("email") String email) {
        // Get the account with this email address
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) throw new UserNotFoundException();
        else
        {
            ResponseSingle<Admin> res = new ResponseSingle<Admin>(HttpStatus.OK, "Success", admin);
            return ResponseEntity.ok(res);
        } 
    }

    // Process a login attempt, return an exception if login with incorrect credentials
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<Admin>> login(@RequestBody Login attempt) {
        if (attempt.getPassword() == null || attempt.getEmail() == null) throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Admin admin = adminRepository.findByEmail(attempt.getEmail());
        if ( (admin != null) &&
            (passwordEncoder.matches(attempt.getPassword(), admin.getProfile().getPassword())) )
        {
            ResponseSingle<Admin> res = new ResponseSingle<Admin>(HttpStatus.OK, "Success", admin);
            return ResponseEntity.ok(res);
        }
        else throw new InvalidLoginException();
    }


    @GetMapping("/{email}/requisitions")
    public ResponseEntity<ResponseMult<Requisition>> getReqs(@PathVariable("email") String email)
    {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) throw new UserNotFoundException();
        else
        {
            // Force initialization
            Hibernate.initialize(admin.getRequisitions());
            ResponseMult<Requisition> res = new ResponseMult<Requisition>(HttpStatus.OK, "Success", admin.getRequisitions());
            return ResponseEntity.ok(res);
        }
    }


}
