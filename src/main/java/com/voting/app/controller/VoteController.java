
package com.voting.app.controller;

import com.voting.app.dto.request.VoteRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.dto.response.VoteResponse;
import com.voting.app.dto.response.VoteStatusResponse;
import com.voting.app.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;
    
    @PostMapping
    public ResponseEntity<VoteResponse> castVote(@Valid @RequestBody VoteRequest voteRequest) {
        VoteResponse vote = voteService.castVote(voteRequest);
        return ResponseEntity.ok(vote);
    }
    
    @GetMapping("/results")
    public ResponseEntity<List<CandidateResponse>> getVoteResults() {
        List<CandidateResponse> results = voteService.getVoteResults();
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/voter/{voterId}")
    public ResponseEntity<VoteStatusResponse> checkVoterStatus(@PathVariable Long voterId) {
        VoteStatusResponse status = voteService.checkVoterStatus(voterId);
        return ResponseEntity.ok(status);
    }
}
