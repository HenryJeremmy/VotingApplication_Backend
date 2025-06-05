
package com.voting.app.mapper;

import com.voting.app.dto.request.CandidateRequest;
import com.voting.app.dto.response.CandidateResponse;
import com.voting.app.model.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(target = "id", expression = "java(candidate.getId().toString())")
    CandidateResponse toResponse(Candidate candidate);
    
    @Mapping(target = "id", ignore = true)
    Candidate toEntity(CandidateRequest request);
    
    List<CandidateResponse> toResponseList(List<Candidate> candidates);
}
