package com.kh.springJpa241217.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}
