package com.timohenderson.RPGMusicServer.websocket;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.enums.ActionType;
import com.timohenderson.RPGMusicServer.events.Event;
import com.timohenderson.RPGMusicServer.events.GameParamsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class MessageBroker {
    @Autowired
    ApplicationEventPublisher publisher;

    public void handleMessage(WebSocketSession session, TextMessage message) {
        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        if (value.get("action") == null) {
            System.out.println("No action");
            System.out.println(value.toString());
        } else {
            publisher.publishEvent(buildEvent(session, value));
        }
    }

    public Event buildEvent(WebSocketSession session, Map value) {
        ActionType actionType = ActionType.valueOf((String) value.get("action"));
        switch (actionType) {
            case PARAMS -> {
                return new GameParamsEvent(session, value);
            }
        }
        return null;

    }
}
