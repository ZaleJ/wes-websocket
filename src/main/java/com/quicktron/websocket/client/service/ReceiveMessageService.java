package com.quicktron.websocket.client.service;


import com.kc.evo.wes.event.basic.model.ReceiveMessage;

public interface ReceiveMessageService {
    void receiveMessageFromRcs(ReceiveMessage message);
}
