package backend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Utility.EmailService;
import backend.Utility.TimeUtility;
import backend.dto.ApplicationDTO;
import backend.dto.ApplicationSummaryDTO;
import backend.error.UnexpectedDateTimeFormatException;
import backend.mapper.ApplicationMapper;
import backend.model.Application;
import backend.repository.ApplicationRepository;
import backend.repository.StatusRepository;
import backend.request.GitLink;
import backend.request.InterviewTime;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
@RequestMapping("/applications")
public class ApplicationController
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public ResponseEntity<ResponseMult<ApplicationSummaryDTO>> getApps()
    {
        List<Application> apps              = applicationRepository.findAll();
        List<ApplicationSummaryDTO> appDTOs        = apps.stream().map(app -> ApplicationMapper.MAPPER.applicationToApplicationSummaryDTO(app)).collect(Collectors.toList());
        ResponseMult<ApplicationSummaryDTO> res  = new ResponseMult<ApplicationSummaryDTO>(HttpStatus.OK, "Success", appDTOs);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{appID}")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> getApp(@PathVariable("appID") int appID)
    {
        Application app                     = applicationRepository.findById(appID);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Success", appDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{appID}/gitLink")
    public ResponseEntity<ResponseSingle<ApplicationDTO>> postGitLink(@PathVariable("appID") int appID, @RequestBody GitLink gitLink)
    {
        Application app                     = applicationRepository.findById(appID);
        // If already candidate already beyond the submit git link step, don't change the status
        if (app.getStatus().getStatusID() <= 1)
            app.setStatus(statusRepository.findById(2));
        app.setGitLink(gitLink.getGitLink());
        app.setUpdatedAt(LocalDateTime.now());
        app.setSubmittedAt(LocalDateTime.now());
        applicationRepository.save(app);
        emailService.sendSubmitGitLinkEmail(app.getCandidate().getProfile().getEmail(), app.getCandidate().getProfile().getFirstName(), app);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Application has been updated", appDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{appID}/interviewTime")
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
        app.setUpdatedAt(LocalDateTime.now());
        emailService.sendInterviewTimeEmail(app.getCandidate().getProfile().getEmail(), app.getCandidate().getProfile().getFirstName(), app, localDateTime);
        applicationRepository.save(app);
        ApplicationDTO appDTO               = ApplicationMapper.MAPPER.applicationToApplicationDTO(app);
        ResponseSingle<ApplicationDTO> res  = new ResponseSingle<ApplicationDTO>(HttpStatus.OK, "Application has been updated", appDTO);
        return ResponseEntity.ok(res);
    }
}