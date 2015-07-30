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

import com.jeqo.samples.eventsource.EventServer;
import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import com.jeqo.samples.eventsource.infra.avro.AvroEventDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.avro.specific.SpecificRecordBase;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * @author jeqo
 * @param <T>
 */
public class KafkaEventServer<T extends SpecificRecordBase> implements EventServer<T> {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventServer.class.getClass().getName());

    private final KafkaConsumerProvider consumerProvider;
    private final AvroEventDeserializer<T> deserializer;
    private final Class<T> type;

    public KafkaEventServer(
            Class<T> type,
            KafkaConsumerProvider consumerProvider
    ) {
        this.consumerProvider = consumerProvider;
        this.type = type;
        this.deserializer = new AvroEventDeserializer<>(type);
    }

    @Override
    public Observable<T> consume() {
        return Observable.create(subscriber -> {
            try {
                LOGGER.log(Level.INFO, "Preparing Server for Event {0}", type.getName());
                Map<String, Integer> topicCountMap = new HashMap<>();
                topicCountMap.put(type.getSimpleName(), 1);

                Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap
                        = consumerProvider.consumer()
                        .createMessageStreams(topicCountMap);

                List<KafkaStream<byte[], byte[]>> streams = consumerMap
                        .get(type.getSimpleName());

                KafkaStream<byte[], byte[]> stream = streams.get(0);

                ConsumerIterator<byte[], byte[]> it = stream.iterator();

                while (it.hasNext()) {
                    subscriber.onNext(
                            deserializer.deserialize(it.next().message())
                    );
                }
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        });
    }

    public static void main(String[] args) {
        KafkaConsumerProvider consumerProvider = new KafkaConsumerProvider();
        consumerProvider.init();
        KafkaEventServer<ClientAddedEvent> eventServer = new KafkaEventServer<>(
                ClientAddedEvent.class, consumerProvider
        );
        eventServer.consume().subscribe(new Subscriber<ClientAddedEvent>() {

            @Override
            public void onCompleted() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onError(Throwable e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onNext(ClientAddedEvent t) {
                LOGGER.log(Level.INFO, "Event received {0}", t.toString());
            }
        });
    }

}
