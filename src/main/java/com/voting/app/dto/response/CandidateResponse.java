
package com.voting.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {
    private String id;
    private String name;
    private String party;
    private String position;
    private String imageUrl;
    private Long voteCount;
}
