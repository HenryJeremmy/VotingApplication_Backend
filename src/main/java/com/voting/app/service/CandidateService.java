
package com.voting.app.service;

import com.voting.app.dto.request.CandidateRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.mapper.CandidateMapper;
import com.voting.app.model.Candidate;
import com.voting.app.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public List<CandidateResponse> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates.stream()
                .map(candidate -> {
                    CandidateResponse response = candidateMapper.toResponse(candidate);
                    // No vote counts in a basic list
                    response.setVoteCount(0L);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public CandidateResponse getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

        CandidateResponse response = candidateMapper.toResponse(candidate);
        response.setVoteCount(0L); // No vote count in single candidate view
        return response;
    }

    public CandidateResponse createCandidate(CandidateRequest candidateRequest) {
        Candidate candidate = candidateMapper.toEntity(candidateRequest);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return candidateMapper.toResponse(savedCandidate);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }
}
