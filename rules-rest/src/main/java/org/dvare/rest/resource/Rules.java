package org.dvare.rest.resource;


import org.dvare.rest.service.RuleBuilder;
import org.dvare.rest.service.RuleEngineService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class Rules {


    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/")
    public Response registerRule(@PathParam("ruleEngine") final String ruleEngine, final String ruleString) {
        RuleBuilder ruleBuilder = new RuleBuilder();
        String ruleId = ruleBuilder.buildRule(ruleEngine, ruleString);
        if (ruleId != null && !ruleId.isEmpty()) {

            String message = "{\"ruleId\" : \"" + ruleId + "\" }";
            return Response.status(200).entity(message).build();
        }
        String message = "{\"message\" :\"please send valid rule\" }";
        return Response.status(400).entity(message).build();

    }


    @DELETE
    @Path("/")
    public Response clearRules(@PathParam("ruleEngine") final String ruleEngine) {
        RuleEngineService ruleEngineService = new RuleEngineService();
        ruleEngineService.clearRules(ruleEngine);
        return Response.status(200).build();
    }


/*

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @GET
    public Response getRules(@PathParam("ruleEngine") final String ruleEngine) {
        return null;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{ruleId}")
    public Response getRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId) {

        return null;
    }
*/


    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{ruleId}")
    public Response unregisterRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId) {
        RuleEngineService ruleEngineService = new RuleEngineService();
        ruleEngineService.unregisterRule(ruleEngine, ruleId);
        return Response.status(200).build();
    }


}
