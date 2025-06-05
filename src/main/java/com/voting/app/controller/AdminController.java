
package com.voting.app.controller;

import com.voting.app.dto.request.CandidateRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.dto.response.MessageResponse;
import com.voting.app.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final CandidateService candidateService;
    
    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateResponse>> getAllCandidates() {
        List<CandidateResponse> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }
    
    @PostMapping("/candidates")
    public ResponseEntity<CandidateResponse> createCandidate(@Valid @RequestBody CandidateRequest candidateRequest) {
        CandidateResponse candidate = candidateService.createCandidate(candidateRequest);
        return ResponseEntity.ok(candidate);
    }
    
    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<MessageResponse> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok(new MessageResponse("Candidate deleted successfully"));
    }
}
