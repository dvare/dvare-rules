package org.dvare.rest.resource;


import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.service.RuleEngineService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ruleEngine")
public class RuleEngine {
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/")
    public Response ruleEngine(@Context HttpServletRequest request) {
        RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
        String ruleEngine = ruleEngineRegistry.createNewEngine(request.getSession().getId());
        String message = "{\"ruleEngine\" : \"" + ruleEngine + "\" }";
        return Response.status(200).entity(message).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/{ruleEngine}")
    public Response ruleEngine(@PathParam("ruleEngine") final String ruleEngine) {
        RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
        ruleEngineRegistry.unregisterRuleEngine(ruleEngine);
        return Response.status(200).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("{ruleEngine}/fire")
    public Response fireRules(String ruleEngine) {
        RuleEngineService ruleEngineService = new RuleEngineService();
        ruleEngineService.fireRules(ruleEngine);
        return Response.status(200).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("{ruleEngine}/rules")
    public Rules getRules(String ruleEngine) {
        return new Rules();
    }


}
