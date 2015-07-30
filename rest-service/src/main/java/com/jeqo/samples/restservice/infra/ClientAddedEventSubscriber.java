/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeqo.samples.restservice.infra;

import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import com.jeqo.samples.eventsource.infra.kafka.KafkaConsumerProvider;
import com.jeqo.samples.eventsource.infra.kafka.KafkaEventServer;
import com.jeqo.samples.restservice.resources.EventsResource;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;

/**
 *
 * @author jeqo
 */
@ApplicationScoped
public class ClientAddedEventSubscriber extends Subscriber<ClientAddedEvent> {

    static final Logger LOGGER = Logger.getLogger(ClientAddedEventSubscriber.class.getName());

    @Resource(name = "DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    @Inject
    private KafkaConsumerProvider consumerProvider;

    private Subscription subscription;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOGGER.log(Level.INFO, "Starting subscription");
        subscription = new KafkaEventServer<>(
                ClientAddedEvent.class,
                consumerProvider,
                executor
        ).consume().subscribe(this);
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        subscription.unsubscribe();
    }

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
        EventsResource.events.add("Client Added: " + t.getName() + " at " + new Date(t.getCreated()));
    }

}
