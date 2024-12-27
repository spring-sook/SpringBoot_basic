package com.kh.springJpa241217.dto;

import com.kh.springJpa241217.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResDto {
    private String email;
    private String name;
    private String imgPath;
    private LocalDateTime regDate;

//    // 게시글 목록 추가
//    private List<BoardResDto> boards;

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .imgPath(member.getImgPath())
                .regDate(member.getRegDate())
                .build();
    }
}
