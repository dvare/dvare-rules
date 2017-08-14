/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.rest.resource;


import org.dvare.exceptions.interpreter.InterpretException;
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

        if (registry != null) {
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
            return Response.status(200).entity(ruleEngines.toString()).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();

    }


    @DELETE
    @Path("/{ruleEngine}")
    public Response removeAllRuleEngine(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            registry.unregisterRuleEngine(ruleEngine);
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }


    @DELETE
    public Response removeRuleEngine(@Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            registry.clearRegistry();
            return Response.status(200).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }


    @GET
    @Path("{ruleEngine}/fire")
    public Response fireRules(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request, @Context UriInfo uriInfo) throws InterpretException {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleEngineService ruleEngineService = new RuleEngineService(registry);
            List<RuleResult> result = ruleEngineService.fireRules(ruleEngine);
            JSONArray results = new JSONArray();
            for (RuleResult ruleResult : result) {
                JSONObject resultObject = new JSONObject();
                resultObject.put("ruleId", ruleResult.getRuleId());
                resultObject.put("ruleName", ruleResult.getRuleName());
                resultObject.put("rule", ruleResult.getRule());
                resultObject.put("result", ruleResult.isResult());
                results.put(resultObject);
            }
            return Response.status(200).entity(results.toString()).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
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


    private JSONObject build404(UriInfo uriInfo) {
        JSONObject resultObject = new JSONObject();
        resultObject.put("message", "Rule Engine not initialized");
        JSONArray links = new JSONArray();

        JSONObject self = new JSONObject();
        self.put("ref", "self");
        String selfLink = uriInfo.getAbsolutePathBuilder().build().toString();
        self.put("link", selfLink);
        links.put(self);

        resultObject.put("links", links);

        return resultObject;
    }

}
