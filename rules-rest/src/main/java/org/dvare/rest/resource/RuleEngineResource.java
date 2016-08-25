package org.dvare.rest.resource;


import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.service.RuleEngineService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;

@Path("/ruleEngine")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RuleEngineResource {

    @POST
    public Response ruleEngine(@Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("registry") == null) {
            RuleEngineRegistry registry = new RuleEngineRegistry();
            session.setAttribute("registry", registry);
            session.setAttribute("counter", 0);
        }

        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        Integer counter = (Integer) session.getAttribute("counter");
        session.setAttribute("counter", ++counter);
        String ruleEngine = registry.createNewEngine(request.getSession().getId() + counter);


        JSONObject message = new JSONObject();
        message.put("ruleEngine", ruleEngine);

        JSONArray links = ruleEngineLinks(ruleEngine, uriInfo);
        message.put("links", links);

        return Response.status(201).entity(message.toString()).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getRuleEngines(@Context HttpServletRequest request, @Context UriInfo uriInfo) {

        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");


        JSONArray ruleEngines = new JSONArray();
        if (registry != null) {
            List<String> ruleEnginesData = registry.getRuleEngines();


            for (String ruleEngineData : ruleEnginesData) {

                JSONObject ruleEngine = new JSONObject();
                ruleEngine.put("ruleEngine", ruleEngineData);
                ruleEngines.put(ruleEngine);

                JSONArray links = ruleEngineLinks(ruleEngineData, uriInfo);
                ruleEngine.put("links", links);
            }


        }

        System.out.println(ruleEngines.toString());

        return Response.status(200).entity(ruleEngines.toString()).build();
    }


    @DELETE
    @Path("/{ruleEngine}")
    public Response ruleEngine(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        registry.unregisterRuleEngine(ruleEngine);
        return Response.status(200).build();
    }


    @DELETE
    public Response ruleEngine(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        registry.clearRegistry();
        return Response.status(200).build();
    }


    @GET
    @Path("{ruleEngine}/fire")
    public Response fireRules(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        RuleEngineService ruleEngineService = new RuleEngineService(registry);
        Map<String, Boolean> result = ruleEngineService.fireRules(ruleEngine);
        JSONArray results = new JSONArray();
        for (String ruleId : result.keySet()) {
            JSONObject resultObject = new JSONObject();
            resultObject.put("ruleId", ruleId);
            resultObject.put("result", result.get(ruleId));
            results.put(resultObject);
        }
        return Response.status(200).entity(results.toString()).build();
    }


    @Path("{ruleEngine}/rules")
    public RuleResource getRules() {
        return new RuleResource();
    }


    private JSONArray ruleEngineLinks(String ruleEngine, UriInfo uriInfo) {
        JSONArray links = new JSONArray();

        JSONObject self = new JSONObject();
        self.put("ref", "self");
        String selfLink = uriInfo.getAbsolutePathBuilder().path(ruleEngine).build().toString();
        self.put("link", selfLink);
        links.put(self);

        JSONObject fire = new JSONObject();
        fire.put("ref", "fire");
        String fireLink = uriInfo.getAbsolutePathBuilder().path(RuleEngineResource.class, "fireRules").path(RuleResource.class).resolveTemplate("ruleEngine", ruleEngine).build().toString();
        fire.put("link", fireLink);
        links.put(fire);

        JSONObject rules = new JSONObject();
        rules.put("ref", "rules");
        String rulesLink = uriInfo.getAbsolutePathBuilder().path(RuleEngineResource.class, "getRules").path(RuleResource.class).resolveTemplate("ruleEngine", ruleEngine).build().toString();
        rules.put("link", rulesLink);
        links.put(rules);


        return links;
    }

}
