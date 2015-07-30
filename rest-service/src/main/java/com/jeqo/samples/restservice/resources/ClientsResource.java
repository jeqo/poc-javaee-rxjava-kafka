/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeqo.samples.restservice.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jeqo
 */
@Path("clients")
public class ClientsResource {

    static List<String> clients = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getClients() {
        return clients;
    }

    @POST
    public void addClient(String client) {
        clients.add(client);
    }
}
