
package com.voting.app.service;

import com.voting.app.dto.request.VoteRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.dto.response.VoteResponse;
import com.voting.app.dto.response.VoteStatusResponse;
import com.voting.app.mapper.CandidateMapper;
import com.voting.app.mapper.VoteMapper;
import com.voting.app.model.Candidate;
import com.voting.app.model.User;
import com.voting.app.model.Vote;
import com.voting.app.repository.CandidateRepository;
import com.voting.app.repository.UserRepository;
import com.voting.app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    private final VoteMapper voteMapper;

    @Transactional
    public VoteResponse castVote(@NotNull VoteRequest voteRequest) {
        User voter = userRepository.findById(voteRequest.getVoterId())
                .orElseThrow(() -> new RuntimeException("Voter not found with id: " + voteRequest.getVoterId()));

        if (voteRepository.existsByVoter(voter)) {
            throw new RuntimeException("Voter has already cast a vote");
        }

        Candidate candidate = candidateRepository.findById(voteRequest.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + voteRequest.getCandidateId()));

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setTimestamp(LocalDateTime.now());

        Vote savedVote = voteRepository.save(vote);
        return voteMapper.toResponse(savedVote);
    }

    public List<CandidateResponse> getVoteResults() {
        List<Candidate> candidates = candidateRepository.findAll();
        Map<Long, Long> voteCounts = new HashMap<>();

        // Count votes for each candidate
        voteRepository.countVotesByCandidate().forEach(result -> {
            Long candidateId = ((Number) result[0]).longValue();
            Long count = ((Number) result[1]).longValue();
            voteCounts.put(candidateId, count);
        });

        return candidates.stream()
                .map(candidate -> {
                    CandidateResponse response = candidateMapper.toResponse(candidate);
                    response.setVoteCount(voteCounts.getOrDefault(candidate.getId(), 0L));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public VoteStatusResponse checkVoterStatus(Long voterId) {
        User voter = userRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found with id: " + voterId));

        Optional<Vote> vote = voteRepository.findByVoter(voter);

        if (vote.isPresent()) {
            return new VoteStatusResponse(true, vote.get().getCandidate().getId().toString());
        } else {
            return new VoteStatusResponse(false, null);
        }
    }
}
