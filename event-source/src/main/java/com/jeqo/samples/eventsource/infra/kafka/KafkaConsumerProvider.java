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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 *
 * @author jeqo
 */
public class KafkaConsumerProvider {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerProvider.class.getName());

    private ConsumerConnector consumer;
    private KafkaConsumerConfig config;

    public void init() {
        try {
            config = new ObjectMapper(new YAMLFactory())
                    .readValue(
                            this.getClass()
                            .getClassLoader()
                            .getResourceAsStream("kafka-consumer.yml"),
                            KafkaConsumerConfig.class
                    );

            Properties props = new Properties();
            props.put("zookeeper.connect", "localhost:2181");
            props.put("group.id", config.getGroupId());
            props.put("zookeeper.session.timeout.ms", "413");
            props.put("zookeeper.sync.time.ms", "203");
            props.put("auto.commit.interval.ms", "1000");

            ConsumerConfig cf = new ConsumerConfig(props);
            consumer = Consumer.createJavaConsumerConnector(cf);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error KafkaConsumerProvider init", ex);
        }
    }

    public void destroy() {
        consumer.shutdown();
    }

    public ConsumerConnector consumer() {
        return consumer;
    }
}
