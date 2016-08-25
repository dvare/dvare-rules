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
        RuleEngineService ruleEngineService = new RuleEngineService(registry);
        List<RuleStructure> ruleStructures = ruleEngineService.getRules(ruleEngine);
        JSONArray rules = new JSONArray();
        for (RuleStructure ruleStructure : ruleStructures) {
            rules.put(ruleJson(ruleStructure, uriInfo));
        }
        return Response.status(400).entity(rules.toString()).build();
    }


    @GET
    @Path("/{ruleId}")
    public Response getRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        RuleEngineService ruleEngineService = new RuleEngineService(registry);
        RuleStructure ruleStructure = ruleEngineService.getRule(ruleEngine, ruleId);
        JSONObject rule = ruleJson(ruleStructure, uriInfo);
        return Response.status(400).entity(rule.toString()).build();
    }


    @POST
    public Response registerRule(@PathParam("ruleEngine") final String ruleEngine, final String ruleString, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        RuleBuilder ruleBuilder = new RuleBuilder();
        String ruleId = ruleBuilder.buildRule(ruleEngine, ruleString, registry);
        JSONObject ruleObject = new JSONObject();
        ruleObject.put("ruleId", ruleId);
        return Response.status(201).entity(ruleObject.toString()).build();
    }


    @DELETE
    public Response clearRules(@PathParam("ruleEngine") final String ruleEngine, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        RuleEngineService ruleEngineService = new RuleEngineService(registry);
        ruleEngineService.clearRules(ruleEngine);
        return Response.status(200).build();
    }


    @DELETE
    @Path("/{ruleId}")
    public Response unregisterRule(@PathParam("ruleEngine") final String ruleEngine, @PathParam("ruleId") final String ruleId, @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        RuleEngineRegistry registry = (RuleEngineRegistry) session.getAttribute("registry");
        RuleEngineService ruleEngineService = new RuleEngineService(registry);
        ruleEngineService.unregisterRule(ruleEngine, ruleId);
        return Response.status(200).build();
    }

    private JSONObject ruleJson(RuleStructure ruleStructure, UriInfo uriInfo) {
        JSONObject rule = new JSONObject();
        rule.put("ruleId", ruleStructure.ruleId);
        rule.put("rule", ruleStructure.rule);
        rule.put("name", ruleStructure.name);
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
                jsonObject.put(name, buildModel((DataRow) type));
            } else {
                jsonObject.put(name, ((DataType) type).name());
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


}
