package backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dto.CCDTO;
import backend.dto.CCSummaryDTO;
import backend.mapper.CodingChallengeMapper;
import backend.model.CodingChallenge;
import backend.repository.CCRepository;
import backend.response.ResponseMult;
import backend.response.ResponseSingle;

@RestController
@RequestMapping("/codingchallenges")
public class CodingChallengeController
{
    @Autowired
    private CCRepository ccRepository;

    @GetMapping("")
    public ResponseEntity<ResponseMult<CCSummaryDTO>> getCCs()
    {
        List<CodingChallenge> ccs       = ccRepository.findAll();
        Hibernate.initialize(ccs);
        List<CCSummaryDTO> ccDTOs              = ccs.stream().map(cc -> CodingChallengeMapper.MAPPER.ccToCCSummaryDTO(cc)).collect(Collectors.toList());
        ResponseMult<CCSummaryDTO> res  = new ResponseMult<CCSummaryDTO>(HttpStatus.OK, "Success", ccDTOs);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{ccID}")
    public ResponseEntity<ResponseSingle<CCDTO>> getCC(@PathVariable("ccID") int ccID)
    {
        CodingChallenge cc      = ccRepository.findById(ccID);
        Hibernate.initialize(cc.getExamples());
        CCDTO ccDTO             = CodingChallengeMapper.MAPPER.ccToCCDTO(cc);
        ResponseSingle<CCDTO> res  = new ResponseSingle<CCDTO>(HttpStatus.OK, "Success", ccDTO);
        return ResponseEntity.ok(res);
    }
}