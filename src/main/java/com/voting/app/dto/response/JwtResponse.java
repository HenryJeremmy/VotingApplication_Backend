
package com.voting.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String id;
    private String name;
    private String email;
    private String token;
    private List<String> roles;
}
