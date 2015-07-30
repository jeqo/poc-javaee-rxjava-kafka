/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeqo.samples.restservice.infra;

import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import com.jeqo.samples.eventsource.infra.kafka.KafkaEventProducer;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author jeqo
 */
@ApplicationScoped
public class ClientAddedEventProducer extends KafkaEventProducer<ClientAddedEvent> {

}
