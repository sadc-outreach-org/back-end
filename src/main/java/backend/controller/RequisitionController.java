package backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
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
import backend.dto.ApplicationSummaryDTO;
import backend.dto.RequisitionDTO;
import backend.dto.RequisitionSummaryDTO;
import backend.mapper.ApplicationMapper;
import backend.mapper.RequisitionMapper;
import backend.model.Application;
import backend.model.Requisition;
import backend.repository.ApplicationRepository;
import backend.repository.RequisitionRepository;
import backend.repository.StatusRepository;
import backend.request.ApplicationRequisition;
import backend.request.RequisitionAdd;
import backend.response.APIResponse;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
@RequestMapping("/requisitions")
public class RequisitionController
{
    @Autowired
    RequisitionRepository requisitionRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;


    @GetMapping("")
    public ResponseEntity<ResponseMult<RequisitionSummaryDTO>> getReqs()
    {
        List<RequisitionSummaryDTO> lstReqDTO = requisitionRepository.findAll().stream().map(req -> RequisitionMapper.MAPPER.requisitionToRequisitionSummaryDTO(req)).collect(Collectors.toList());
        ResponseMult<RequisitionSummaryDTO> res = new ResponseMult<RequisitionSummaryDTO>(HttpStatus.OK, "Success", lstReqDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> addRequisition(@RequestBody RequisitionAdd requisitionAddDTO)
    {
        Requisition requisition = RequisitionMapper.MAPPER.requisitionAddToRequisition(requisitionAddDTO);
        requisition.setIsOpen(true);
        requisitionRepository.save(requisition);
        APIResponse res = new APIResponse(HttpStatus.OK, "A requisition has been added for job with ID : " + requisition.getJob().getJobID());
        return ResponseEntity.ok(res);

    }

    
    @GetMapping("/{reqID}")
    public ResponseEntity<ResponseSingle<RequisitionDTO>> getReq(@PathVariable("reqID") int reqID)
    {
        Requisition req = requisitionRepository.findById(reqID);
        RequisitionDTO reqDTO = RequisitionMapper.MAPPER.requisitionToRequisitionDTO(req);
        ResponseSingle<RequisitionDTO> res = new ResponseSingle<RequisitionDTO>(HttpStatus.OK, "Success", reqDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{reqID}/applications")
    public ResponseEntity<ResponseMult<ApplicationSummaryDTO>> getReqApps(@PathVariable("reqID") int reqID)
    {
        Requisition req = requisitionRepository.findById(reqID);
        Hibernate.initialize(req.getApplications());
        List<ApplicationSummaryDTO> lstAppDTO = req.getApplications().stream().map(app -> ApplicationMapper.MAPPER.applicationToApplicationSummaryDTO(app)).collect(Collectors.toList());
        ResponseMult<ApplicationSummaryDTO> res = new ResponseMult<ApplicationSummaryDTO>(HttpStatus.OK, "Success", lstAppDTO);
        return ResponseEntity.ok(res);
    }

    
    @PostMapping("/{reqID}/applications")
    public ResponseEntity<APIResponse> addApplicationReq(@PathVariable("reqID") int reqID, @RequestBody ApplicationRequisition appReq)
    {
        Application app = ApplicationMapper.MAPPER.applicationRequisitionToApplication(appReq);
        app.setStatus(statusRepository.findById(1));
        app.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(app);
        Hibernate.initialize(app);
        emailService.sendApplicationEmail(app.getCandidate().getProfile().getEmail(), app.getCandidate().getProfile().getFirstName(), app);
        APIResponse res = new APIResponse(HttpStatus.OK, "An application has been added for candidate with ID " + app.getCandidate().getCandidateID() 
                                        + " for requisition with ID :" + app.getRequisition().getRequisitionID());
        return ResponseEntity.ok(res);
    }
}