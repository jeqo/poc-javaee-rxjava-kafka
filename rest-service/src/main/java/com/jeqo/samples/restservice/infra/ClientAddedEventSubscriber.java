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
