package backend.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import backend.dto.LogInResultDTO;
import backend.dto.NotificationDTO;
import backend.error.InvalidLoginException;
import backend.error.UserNotFoundException;
import backend.mapper.NotificationMapper;
import backend.mapper.ProfileMapper;
import backend.model.CodingChallenge;
import backend.model.Example;
import backend.model.Job;
import backend.model.Profile;
import backend.repository.CCRepository;
import backend.repository.ExampleRepository;
import backend.repository.JobRepository;
import backend.repository.ProfileRepository;
import backend.request.Login;
import backend.request.PasswordReset;
import backend.response.APIResponse;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
public class MainController
{
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private CCRepository ccRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    // Testing, getting all admins accounts





    
    @PostMapping("/login")
    public ResponseEntity<ResponseSingle<LogInResultDTO>> logIn(@RequestBody Login attempt)
    {
        if (attempt.getPassword() == null || attempt.getEmail() == null)
            throw new InvalidLoginException();

        // Check if this email exists
        Profile profile     = profileRepository.findByEmail(attempt.getEmail());
        if (profile == null)
            throw new InvalidLoginException();
        // Check if this account is locked and uses tempPW to reset pw
        if (profile.getIsLocked())
        {
            if (!passwordEncoder.matches(attempt.getPassword(), profile.getTempPW()))
                throw new InvalidLoginException();
        }
        else
        {
            if (!passwordEncoder.matches(attempt.getPassword(), profile.getPassword()))
                throw new InvalidLoginException();
        }
        Hibernate.initialize(profile.getUserType());
        LogInResultDTO user = ProfileMapper.MAPPER.profileToLogInResultDTO(profile);
        ResponseSingle<LogInResultDTO> res = new ResponseSingle<LogInResultDTO>(HttpStatus.OK, "Success", user);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/password")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody PasswordReset reset)
    {
        Profile profile     = profileRepository.findByEmail(reset.getEmail());
        if (profile == null)
            throw new InvalidLoginException();
        if (reset.getNewPassword() == null)
            throw new InvalidLoginException();
        if (profile.getIsLocked())
        {
            if (!passwordEncoder.matches(reset.getOldPassword(), profile.getTempPW()))
                throw new InvalidLoginException();
            profile.setPassword(passwordEncoder.encode(reset.getNewPassword()));
            profile.setTempPW(null);
            profile.setIsLocked(false);
            profileRepository.save(profile);
        }
        else
        {
            if (!passwordEncoder.matches(reset.getOldPassword(), profile.getPassword()))
                throw new InvalidLoginException();
            profile.setPassword(passwordEncoder.encode(reset.getNewPassword()));
            profile.setTempPW(null);
            profile.setIsLocked(false);
            profileRepository.save(profile);
        }
        APIResponse res  = new APIResponse(HttpStatus.OK, "Password for user with email " + reset.getEmail() + " has been reset / updated");
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



    // For testing
    @GetMapping("/test")
    public List<CodingChallenge> getTest()
    {
        
        Job job = jobRepository.findById(1);
        Hibernate.initialize(job.getCodingChallenges());
        List<CodingChallenge> cc     = job.getCodingChallenges();
        for (CodingChallenge c : cc)
            Hibernate.initialize(c.getExamples());

        Example ex = exampleRepository.findById(1);
        return cc;
    }

    // For testing
    @GetMapping("/test2")
    public CodingChallenge getTest2()
    {
        CodingChallenge cc  = ccRepository.findById(1);
        Hibernate.initialize(cc.getJobs());
        return cc;
    }
}