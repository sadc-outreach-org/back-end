package backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
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

import backend.dto.AdminDTO;
import backend.dto.RequisitionSummaryDTO;
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
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseSingle<AdminDTO>> getAdminByID(@PathVariable("id") int id) {
        // Get the account with this email address
        Admin admin = adminRepository.findById(id);
        if (admin == null) throw new UserNotFoundException();
        AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
        ResponseSingle<AdminDTO> res = new ResponseSingle<AdminDTO>(HttpStatus.OK, "Success", adminDTO);
        return ResponseEntity.ok(res);
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


    @GetMapping("/{id}/requisitions")
    public ResponseEntity<ResponseMult<RequisitionSummaryDTO>> getReqs(@PathVariable("id") int id)
    {
        Admin admin = adminRepository.findById(id);
        if (admin == null) throw new UserNotFoundException();
        else
        {
            // Force initialization
            Hibernate.initialize(admin.getRequisitions());
            List<RequisitionSummaryDTO> reqDTO = admin.getRequisitions().stream().map(req -> modelMapper.map(req, RequisitionSummaryDTO.class)).collect(Collectors.toList());
            ResponseMult<RequisitionSummaryDTO> res = new ResponseMult<RequisitionSummaryDTO>(HttpStatus.OK, "Success", reqDTO);
            return ResponseEntity.ok(res);
        }
    }


}
