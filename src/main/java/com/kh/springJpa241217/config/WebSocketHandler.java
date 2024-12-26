package com.kh.springJpa241217.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.springJpa241217.dto.ChatMessageDto;
import com.kh.springJpa241217.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor // Lombok 어노테이션으로 생성자 만들기
@Slf4j // Lombok 어노테이션으로 log 메시지 사용
@Component // 스프링 컨테이너에 빈 등록
public class WebSocketHandler extends TextWebSocketHandler { // 텍스트 메시지를 주고받는 기능을 구현할 때 사용
    private final ObjectMapper objectMapper; // Java 객체와 JSON 간의 직렬화 및 역직렬화를 수행하는 데 사용
    private final ChatService chatService;

    // 세션과 채팅방을 매핑하는 데 사용 (사용자가 어떤 채팅방에 속해 있는지 등록)
    private final Map<WebSocketSession, String>
            sessionRoomIdMap = new ConcurrentHashMap<>(); // ConcurrentHashMap : 여러 스레드가 동시에 접근하거나 수정해도 동기화 문제가 발생하지 않도록 설계된 자료구조

    @Override // handleTextMessage 메서드는 TextWebSocketHandler 클래스에서 제공하는 메서드로, WebSocket을 통해 수신된 텍스트 메시지를 처리하는 데 사용
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception { // protected : 같은 패키지 내에서나 하위 클래스에서만 호출 가능 -> WebSocket 메시지를 처리하는 로직이 외부로 노출되지 않도록 보호
        String payload = message.getPayload(); // 클라이언트가 전송한 메시지
        log.info("{}", payload);
        // JSON 문자열을 ChatMessageDto로 변환 작업
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
        String roomId = chatMessage.getRoomId();
        if (chatMessage.getType() == ChatMessageDto.MessageType.ENTER) {
            sessionRoomIdMap.put(session, chatMessage.getRoomId());
            chatService.addSessionAndHandleEnter(roomId, session, chatMessage);
        } else if (chatMessage.getType() == ChatMessageDto.MessageType.CLOSE) {
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        } else {
            chatService.sendMessageToAll(roomId, chatMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception{
        log.info("연결 해제 이후 동작 : {}", session);
        String roomId = sessionRoomIdMap.remove(session);
        if(roomId != null) {
            ChatMessageDto chatMessage = new ChatMessageDto();
            chatMessage.setType(ChatMessageDto.MessageType.CLOSE);
            chatService.removeSessionAndHandleExit(roomId, session, chatMessage);
        }
    }
}
