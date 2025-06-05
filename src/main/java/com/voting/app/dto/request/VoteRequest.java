
package com.voting.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotNull
    private Long voterId;
    
    @NotNull
    private Long candidateId;
}
