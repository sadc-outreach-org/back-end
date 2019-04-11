package backend.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import backend.dto.AdminDTO;
import backend.dto.RequisitionDTO;
import backend.error.EmailInUseException;
import backend.error.InvalidLoginException;
import backend.error.MissingInfomationException;
import backend.error.UserNotFoundException;
import backend.mapper.AdminMapper;
import backend.mapper.RequisitionMapper;
import backend.model.Admin;
import backend.model.UserType;
import backend.repository.AdminRepository;
import backend.repository.ProfileRepository;
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
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<ResponseSingle<AdminDTO>> signUp(@RequestBody AdminDTO adminDTO) {
        // Check if email already in used
        if (profileRepository.findByEmail(adminDTO.getEmail()) != null)
            throw new EmailInUseException();

        UserType adminUserType = new UserType();
        adminUserType.setUserTypeID(2);

        Admin admin = AdminMapper.MAPPER.adminDTOToAdmin(adminDTO);
        admin.getProfile().setUserType(adminUserType);

        // Check required fields
        if (!admin.getProfile().hasAllFields())
            throw new MissingInfomationException();
        // Add user to the database
        else {
            // Encode password, save to database and exit with status code 200
            admin.getProfile().setPassword(passwordEncoder.encode(admin.getProfile().getPassword()));
            adminRepository.save(admin);
            adminDTO.setAdminID(admin.getAdminID());
            ResponseSingle<AdminDTO> res = new ResponseSingle<AdminDTO>(HttpStatus.OK, "Signup Success",
                    adminDTO);
            return ResponseEntity.ok(res);
        }
    }

    // Process a login attempt, return an exception if login with incorrect credentials
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<AdminDTO>> login(@RequestBody Login attempt) {
        if (attempt.getPassword() == null || attempt.getEmail() == null) throw new InvalidLoginException();
        // Check if email and password correspond to a user on database
        Admin admin = adminRepository.findByEmail(attempt.getEmail());
        AdminDTO adminDTO = AdminMapper.MAPPER.adminToAdminDTO(admin);
        if ( (admin != null) &&
            (passwordEncoder.matches(attempt.getPassword(), admin.getProfile().getPassword())) )
        {
            ResponseSingle<AdminDTO> res = new ResponseSingle<AdminDTO>(HttpStatus.OK, "Success", adminDTO);
            return ResponseEntity.ok(res);
        }
        else throw new InvalidLoginException();
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseSingle<AdminDTO>> getAdminByID(@PathVariable("id") int id) {
        // Get the account with this email address
        Admin admin = adminRepository.findById(id);
        if (admin == null) throw new UserNotFoundException();
        AdminDTO adminDTO = AdminMapper.MAPPER.adminToAdminDTO(admin);
        ResponseSingle<AdminDTO> res = new ResponseSingle<AdminDTO>(HttpStatus.OK, "Success", adminDTO);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{id}/requisitions")
    public ResponseEntity<ResponseMult<RequisitionDTO>> getReqs(@PathVariable("id") int id)
    {
        Admin admin = adminRepository.findById(id);
        if (admin == null) throw new UserNotFoundException();
        else
        {
            // Force initialization
            Hibernate.initialize(admin.getRequisitions());
            List<RequisitionDTO> reqDTO = admin.getRequisitions().stream().map(req -> RequisitionMapper.MAPPER.requisitionToRequisitionDTO(req)).collect(Collectors.toList());
            ResponseMult<RequisitionDTO> res = new ResponseMult<RequisitionDTO>(HttpStatus.OK, "Success", reqDTO);
            return ResponseEntity.ok(res);
        }
    }
}
