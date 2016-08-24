package org.dvare.rest.service;


import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.json.JSONObject;

public class RuleEngineService {


    public String fireRules(final String ruleEngine) {
        RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            restRuleEngine.fireRules();
        }
        return null;
    }

    public void unregisterRule(final String ruleEngine, final String ruleId) {
        RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            restRuleEngine.unregisterRule(ruleId);
        }

    }

    public void clearRules(String ruleEngine) {
        RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            restRuleEngine.clearRules();
        }
    }


    private RestRuleEngine getRuleEngine(final String ruleString) {
        if (ruleString != null && !ruleString.isEmpty()) {
            JSONObject jsonObject = new JSONObject(ruleString);
            String ruleEngine = jsonObject.getString("ruleEngine");
            RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
            RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
            return restRuleEngine;
        }
        return null;
    }


}
