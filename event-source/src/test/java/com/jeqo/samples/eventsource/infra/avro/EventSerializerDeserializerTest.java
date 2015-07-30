/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeqo.samples.eventsource.infra.avro;

import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author jeqo
 */
public class EventSerializerDeserializerTest extends TestCase {

    EventSerializer<ClientAddedEvent> serializer;
    EventDeserializer<ClientAddedEvent> deserializer;

    public EventSerializerDeserializerTest(String testName) {
        super(testName);
        serializer = new EventSerializer<>();
        deserializer = new EventDeserializer<>(ClientAddedEvent.class);
    }

    public void test() {
        ClientAddedEvent event = ClientAddedEvent.newBuilder()
                .setName("jeqo")
                .setCreated(new Date().getTime())
                .build();
        byte[] eventSerialized = serializer.serialize(event);
        ClientAddedEvent eventDeserialized = deserializer.deserialize(eventSerialized);
        assertEquals(event, eventDeserialized);
    }

}
