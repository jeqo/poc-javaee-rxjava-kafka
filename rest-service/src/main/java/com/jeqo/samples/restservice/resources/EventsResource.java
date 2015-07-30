/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeqo.samples.restservice.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jeqo
 */
@Path("events")
public class EventsResource {

    static List<String> events = new ArrayList<>();

    public EventsResource() {
        events.add("Event 0: App started");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getEvents() {
        return events;
    }
}
