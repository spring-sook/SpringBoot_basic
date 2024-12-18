package com.kh.springJpa241217.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO : 다른 레이어간의 데이터를 교환할 때 사용
// 주로 Frontend와 Backend 사이에 데이터를 주고받는 용도
// 회원가입용
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberReqDto {
    private String email;
    private String pwd;
    private String name;
    private String imgPath;
}
