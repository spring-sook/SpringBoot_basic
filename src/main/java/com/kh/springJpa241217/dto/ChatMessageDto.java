package com.kh.springJpa241217.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    public enum MessageType {
        ENTER, TALK, CLOSE
    }

    private MessageType type; // 방 진입, 메시지
    private String roomId; // 채탱방 번호
    private String sender; // 보내는 사람
    private String message; // 메시지 내용
}
