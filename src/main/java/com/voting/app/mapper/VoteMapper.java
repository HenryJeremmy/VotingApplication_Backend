
package com.voting.app.mapper;

import com.voting.app.dto.response.VoteResponse;
import com.voting.app.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    @Mapping(target = "id", expression = "java(vote.getId().toString())")
    @Mapping(target = "voterId", expression = "java(vote.getVoter().getId().toString())")
    @Mapping(target = "candidateId", expression = "java(vote.getCandidate().getId().toString())")
    VoteResponse toResponse(Vote vote);
}
