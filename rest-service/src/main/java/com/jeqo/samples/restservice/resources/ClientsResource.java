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
package com.jeqo.samples.restservice.resources;

import com.jeqo.samples.eventsource.event.ClientAddedEvent;
import com.jeqo.samples.restservice.infra.ClientAddedEventProducer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jeqo
 */
@Stateless
@Path("clients")
public class ClientsResource {

    @Inject
    ClientAddedEventProducer eventProducer;

    static List<String> clients = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getClients() {
        return clients;
    }

    @POST
    public void addClient(String client) {
        clients.add(client);
        eventProducer.publish(
                ClientAddedEvent.newBuilder()
                .setName(client)
                .setCreated(new Date().getTime())
                .build()
        );
    }
}
