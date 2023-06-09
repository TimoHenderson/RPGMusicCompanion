package com.timohenderson.RPGMusicServer.websocket;

import com.timohenderson.RPGMusicServer.events.SendGameStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class SocketHandler extends TextWebSocketHandler {


    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    MessageBroker messageBroker;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        messageBroker.handleMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Connection closed: " + status.getReason());
        //listenerToWebSocket.setSessions(sessions);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        // session.sendMessage(new TextMessage("Hello"));
        addSession(session);
        publisher.publishEvent(new SendGameStateEvent(this, "State Please!"));

    }

    private void addSession(WebSocketSession session) {
        String sessionAddress = session.getRemoteAddress().getHostString();
        for (WebSocketSession s : sessions) {
            if (s.getRemoteAddress().getHostString() == sessionAddress) return;
        }
        sessions.clear();
        sessions.add(session);

        for (WebSocketSession s : sessions) {
            System.out.println(s);
        }

    }

    public void sendTextMessage(String message) throws IOException {
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(message));
        }
    }

    public void sendBinaryMessage(byte[] buffer) throws IOException {
        for (WebSocketSession s : sessions) {
            s.sendMessage(new BinaryMessage(buffer));
        }
    }

}