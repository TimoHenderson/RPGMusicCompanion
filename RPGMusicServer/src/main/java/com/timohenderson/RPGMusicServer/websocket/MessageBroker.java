package com.timohenderson.RPGMusicServer.websocket;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.enums.EventType;
import com.timohenderson.RPGMusicServer.enums.NavigationType;
import com.timohenderson.RPGMusicServer.enums.ParamType;
import com.timohenderson.RPGMusicServer.enums.TransportActionType;
import com.timohenderson.RPGMusicServer.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class MessageBroker {
    @Autowired
    ApplicationEventPublisher publisher;

    public void handleMessage(WebSocketSession session, TextMessage message) throws IOException {
        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        if (value.get("event") == null) {
            System.out.println("No action");
            session.sendMessage(new TextMessage("Invalid command: " + message.getPayload().toString()));
        } else {
            Event event = buildEvent(session, value);
            if (event != null) {
                publisher.publishEvent(event);
            }
        }
    }

    public Event buildEvent(WebSocketSession session, Map value) {
        EventType eventType = EventType.valueOf((String) value.get("event"));
        switch (eventType) {
            case PARAMS:
                HashMap<ParamType, Double> params = new HashMap<>();
                params.put(ParamType.DARKNESS, (Double) value.get("darkness"));
                params.put(ParamType.INTENSITY, (Double) value.get("intensity"));
                return new GameParamsEvent(session, params);
            case TRANSPORT:
                TransportActionType type = TransportActionType.valueOf((String) value.get("action"));
                return new TransportEvent(session, type);
            case NAVIGATION:
                NavigationType navType = NavigationType.valueOf((String) value.get("action"));
                return new NavigationEvent(session, navType);
            case SELECT_TUNE:
                return new LoadTuneEvent(session, (String) value.get("tune"));
            default:
                System.out.println("No action");
                System.out.println(value.toString());
                return null;
        }

    }


}
