
package com.voting.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteStatusResponse {
    private boolean hasVoted;
    private String candidateId;
}
