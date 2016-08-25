package org.dvare.rest.service;


import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.rest.ruleengine.RuleStructure;

import java.util.List;
import java.util.Map;

public class RuleEngineService {

    RuleEngineRegistry ruleEngineRegistry = null;

    public RuleEngineService(RuleEngineRegistry ruleEngineRegistry) {
        this.ruleEngineRegistry = ruleEngineRegistry;
    }


    public Map<String, Boolean> fireRules(final String ruleEngine) {

        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            return restRuleEngine.fireRules();
        }
        return null;
    }

    public void unregisterRule(final String ruleEngine, final String ruleId) {

        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            restRuleEngine.unregisterRule(ruleId);
        }

    }

    public void clearRules(String ruleEngine) {

        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        if (restRuleEngine != null) {
            restRuleEngine.clearRules();
        }
    }


    public List<RuleStructure> getRules(String ruleEngine) {
        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        List<RuleStructure> rules = restRuleEngine.getRules();
        return rules;
    }


    public RuleStructure getRule(String ruleEngine, String ruleId) {
        RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);
        RuleStructure rule = restRuleEngine.getRule(ruleId);
        return rule;
    }


}
