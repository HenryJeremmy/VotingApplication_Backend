
package com.voting.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {
    private String id;
    private String voterId;
    private String candidateId;
    private LocalDateTime timestamp;
}
