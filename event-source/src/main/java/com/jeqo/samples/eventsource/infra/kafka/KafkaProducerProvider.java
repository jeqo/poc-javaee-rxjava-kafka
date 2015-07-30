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

import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 *
 * @author jeqo
 */
@ApplicationScoped
public class KafkaProducerProvider {

    KafkaProducer<String, String> producer;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        producer = new KafkaProducer<>(props);
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        producer.close();
    }

    public KafkaProducer producer() {
        return producer;
    }
}
