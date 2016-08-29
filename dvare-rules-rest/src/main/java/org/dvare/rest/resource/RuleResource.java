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


import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;
import org.dvare.expression.datatype.DataType;
import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.ruleengine.RuleStructure;
import org.dvare.rest.service.RuleBuilder;
import org.dvare.rest.service.RuleEngineService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Resource
public class RuleResource {

    @GET
    public Response getRules(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleEngineService ruleEngineService = new RuleEngineService(registry);
            List<RuleStructure> ruleStructures = ruleEngineService.getRules(ruleEngine);
            JSONArray rules = new JSONArray();
            for (RuleStructure ruleStructure : ruleStructures) {
                rules.put(ruleJson(ruleStructure, uriInfo));
            }
            return Response.status(200).entity(rules.toString()).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }


    @GET
    @Path("/{ruleId}")
    public Response getRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleEngineService ruleEngineService = new RuleEngineService(registry);
            RuleStructure ruleStructure = ruleEngineService.getRule(ruleEngine, ruleId);
            JSONObject rule = ruleJson(ruleStructure, uriInfo);
            return Response.status(200).entity(rule.toString()).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerRule(@PathParam("ruleEngine") final String ruleEngine, String ruleString, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleBuilder ruleBuilder = new RuleBuilder();
            String ruleId = ruleBuilder.buildRule(ruleEngine, ruleString, registry);
            JSONObject ruleObject = new JSONObject();
            ruleObject.put("ruleId", ruleId);
            return Response.status(201).entity(ruleObject.toString()).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }


    @DELETE
    public Response clearRules(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleEngineService ruleEngineService = new RuleEngineService(registry);
            ruleEngineService.clearRules(ruleEngine);
            return Response.status(200).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }


    @DELETE
    @Path("/{ruleId}")
    public Response unregisterRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        if (registry != null) {
            RuleEngineService ruleEngineService = new RuleEngineService(registry);
            ruleEngineService.unregisterRule(ruleEngine, ruleId);
            return Response.status(200).build();
        }
        return Response.status(404).entity(build404(uriInfo).toString()).build();
    }

    private JSONObject ruleJson(RuleStructure ruleStructure, UriInfo uriInfo) {
        JSONObject rule = new JSONObject();
        rule.put("ruleId", ruleStructure.ruleId);
        rule.put("rule", ruleStructure.rule);
        rule.put("ruleName", ruleStructure.name);
        rule.put("ruleId", ruleStructure.priority);
        rule.put("modelTypes", buildModelType(ruleStructure.typeBinding));
        rule.put("model", buildModel(ruleStructure.dataRow));
        rule.put("links", ruleLinks(ruleStructure.ruleId, uriInfo));
        return rule;
    }


    private JSONObject buildModelType(TypeBinding typeBinding) {
        JSONObject jsonObject = new JSONObject();
        for (String name : typeBinding.getTypes().keySet()) {
            Object type = typeBinding.getDataType(name);
            if (type instanceof DataType) {
                jsonObject.put(name, ((DataType) type).name());
            } else if (type instanceof TypeBinding) {
                jsonObject.put(name, buildModelType((TypeBinding) type));
            }
        }
        return jsonObject;
    }

    private JSONObject buildModel(DataRow dataRow) {
        JSONObject jsonObject = new JSONObject();
        for (String name : dataRow.getData().keySet()) {
            Object type = dataRow.getValue(name);
            if (type instanceof DataRow) {
                jsonObject.put(name, (DataRow) type);
            } else {
                jsonObject.put(name, type);
            }
        }
        return jsonObject;
    }

    private JSONArray ruleLinks(String ruleId, UriInfo uriInfo) {
        JSONArray links = new JSONArray();

        JSONObject self = new JSONObject();
        self.put("ref", "self");
        String selfLink = uriInfo.getAbsolutePathBuilder().path(ruleId).build().toString();
        self.put("link", selfLink);
        links.put(self);

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
