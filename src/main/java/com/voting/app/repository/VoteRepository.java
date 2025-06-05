
package com.voting.app.repository;

import com.voting.app.model.User;
import com.voting.app.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByVoter(User voter);
    boolean existsByVoter(User voter);
    
    @Query("SELECT v.candidate.id, COUNT(v) as count FROM Vote v GROUP BY v.candidate.id")
    List<Object[]> countVotesByCandidate();
}
