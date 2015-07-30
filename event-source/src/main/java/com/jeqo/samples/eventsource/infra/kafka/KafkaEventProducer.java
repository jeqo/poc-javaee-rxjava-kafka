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
package com.jeqo.samples.eventsource.infra.kafka;

import com.jeqo.samples.eventsource.EventProducer;
import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import com.jeqo.samples.eventsource.infra.avro.AvroEventSerializer;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author jeqo
 * @param <T>
 */
public class KafkaEventProducer<T extends SpecificRecordBase>
        implements EventProducer<T> {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventProducer.class.getClass().getName());

    private final KafkaProducerProvider producerProvider;

    private final AvroEventSerializer<T> serializer;

    public KafkaEventProducer(KafkaProducerProvider producerProvider) {
        this.serializer = new AvroEventSerializer<>();
        this.producerProvider = producerProvider;
    }

    @Override
    public void publish(T message) {
        ProducerRecord<String, byte[]> data = new ProducerRecord<>(
                message.getClass().getSimpleName(),
                serializer.serialize(message)
        );

        Future<RecordMetadata> rs = producerProvider.producer()
                .send(data, (RecordMetadata recordMetadata, Exception e) -> {
                    LOGGER.log(Level.INFO, "Received ack for partition={0} offset = {1}", new Object[]{recordMetadata.partition(), recordMetadata.offset()});
                });

        try {
            RecordMetadata rm = rs.get();

            LOGGER.log(Level.INFO, "Kafka Record Metadata: partition = {0} offset ={1}", new Object[]{rm.partition(), rm.offset()});

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        KafkaProducerProvider producerProvider = new KafkaProducerProvider();

        producerProvider.init();

        KafkaEventProducer<ClientAddedEvent> eventProducer = new KafkaEventProducer<>(producerProvider);

        eventProducer.publish(ClientAddedEvent.newBuilder()
                .setName("jeqo")
                .setCreated(new Date().getTime())
                .build());

        producerProvider.destroy();
    }
}
