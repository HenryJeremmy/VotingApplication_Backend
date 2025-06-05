
package com.voting.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String party;
    
    @NotBlank
    private String position;
    
    @NotBlank
    private String imageUrl;
}
