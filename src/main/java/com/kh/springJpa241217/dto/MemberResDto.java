package com.kh.springJpa241217.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDto {
    private String email;
    private String name;
    private String imgPath;
    private LocalDateTime regDate;
}
