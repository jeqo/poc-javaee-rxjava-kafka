/* 
 * Copyright (C) 2015 jeqo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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

    AvroEventSerializer<ClientAddedEvent> serializer;
    AvroEventDeserializer<ClientAddedEvent> deserializer;

    public EventSerializerDeserializerTest(String testName) {
        super(testName);
        serializer = new AvroEventSerializer<>();
        deserializer = new AvroEventDeserializer<>(ClientAddedEvent.class);
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
