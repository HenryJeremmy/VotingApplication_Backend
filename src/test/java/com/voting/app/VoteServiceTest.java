
package com.voting.app;

import com.voting.app.dto.request.VoteRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.dto.response.VoteResponse;
import com.voting.app.mapper.CandidateMapper;
import com.voting.app.mapper.VoteMapper;
import com.voting.app.model.Candidate;
import com.voting.app.model.User;
import com.voting.app.model.Vote;
import com.voting.app.repository.CandidateRepository;
import com.voting.app.repository.UserRepository;
import com.voting.app.repository.VoteRepository;
import com.voting.app.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private VoteRepository voteRepository;
    private UserRepository userRepository;
    private CandidateRepository candidateRepository;
    private CandidateMapper candidateMapper;
    private VoteMapper voteMapper;

    private VoteService voteService;

    @BeforeEach
    public void setUp() {
        voteRepository = mock(VoteRepository.class);
        userRepository = mock(UserRepository.class);
        candidateRepository = mock(CandidateRepository.class);
        candidateMapper = mock(CandidateMapper.class);
        voteMapper = mock(VoteMapper.class);

        voteService = new VoteService(voteRepository, userRepository, candidateRepository, candidateMapper, voteMapper);
    }

    @Test
    public void testCastVote_success() {
        Long voterId = 1L;
        Long candidateId = 2L;

        User voter = new User();
        voter.setId(voterId);

        Candidate candidate = new Candidate();
        candidate.setId(candidateId);

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setTimestamp(LocalDateTime.now());

        Vote savedVote = new Vote();
        savedVote.setVoter(voter);
        savedVote.setCandidate(candidate);

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setVoterId(voterId);
        voteRequest.setCandidateId(candidateId);

        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setCandidateId(candidateId.toString());
        voteResponse.setVoterId(voterId.toString());

        when(userRepository.findById(voterId)).thenReturn(Optional.of(voter));
        when(voteRepository.existsByVoter(voter)).thenReturn(false);
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(voteRepository.save(any(Vote.class))).thenReturn(savedVote);
        when(voteMapper.toResponse(savedVote)).thenReturn(voteResponse);

        VoteResponse result = voteService.castVote(voteRequest);

        assertNotNull(result);
        assertEquals(voterId.toString(), result.getVoterId());
        assertEquals(candidateId.toString(), result.getCandidateId());
    }

    @Test
    public void testCastVote_alreadyVoted_throwsException() {
        Long voterId = 1L;
        User voter = new User();
        voter.setId(voterId);

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setVoterId(voterId);
        voteRequest.setCandidateId(2L);

        when(userRepository.findById(voterId)).thenReturn(Optional.of(voter));
        when(voteRepository.existsByVoter(voter)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            voteService.castVote(voteRequest);
        });

        assertEquals("Voter has already cast a vote", exception.getMessage());
    }

    @Test
    public void testGetVoteResults_success() {
        // Mock candidates
        Candidate candidate1 = new Candidate();
        candidate1.setId(1L);
        candidate1.setName("Alice");

        Candidate candidate2 = new Candidate();
        candidate2.setId(2L);
        candidate2.setName("Bob");

        List<Candidate> candidates = List.of(candidate1, candidate2);

        // Mock vote count result: {candidateId, voteCount}
        Object[] result1 = new Object[]{1L, 5L};
        Object[] result2 = new Object[]{2L, 3L};

        List<Object[]> voteCounts = List.of(result1, result2);

        // Mock responses
        CandidateResponse response1 = new CandidateResponse();
        response1.setId("1");
        response1.setName("Alice");
        response1.setVoteCount(5L);

        CandidateResponse response2 = new CandidateResponse();
        response2.setId("2");
        response2.setName("Bob");
        response2.setVoteCount(3L);

        when(candidateRepository.findAll()).thenReturn(candidates);
        when(voteRepository.countVotesByCandidate()).thenReturn(voteCounts);
        when(candidateMapper.toResponse(candidate1)).thenReturn(response1);
        when(candidateMapper.toResponse(candidate2)).thenReturn(response2);

        List<CandidateResponse> result = voteService.getVoteResults();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals(5L, result.get(0).getVoteCount());
        assertEquals("Bob", result.get(1).getName());
        assertEquals(3L, result.get(1).getVoteCount());
    }

}

