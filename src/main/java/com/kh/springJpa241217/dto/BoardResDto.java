package com.kh.springJpa241217.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class BoardResDto {
    private Long boardId;
    private String title;
    private String content;
    private String imgPath;
    private LocalDateTime regDate;
    private String email;
}
