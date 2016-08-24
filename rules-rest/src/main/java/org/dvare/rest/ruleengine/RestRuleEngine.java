package org.dvare.rest.ruleengine;


import org.dvare.ruleengine.TextualRuleEngine;

import java.util.*;

public class RestRuleEngine {

    String name;
    TextualRuleEngine textualRuleEngine;


    private Map<String, RuleStructure> rules = new HashMap<>();

    public RestRuleEngine(String name, TextualRuleEngine textualRuleEngine) {
        this.name = name;
        this.textualRuleEngine = textualRuleEngine;
    }

    public String registerRule(String ruleName, Integer rulePriority, String rule, Object model) {
        String ruleId = ruleName + rules.size();
        RuleStructure ruleStructure = new RuleStructure();
        ruleStructure.ruleId = ruleId;
        ruleStructure.rule = rule;
        ruleStructure.priority = rulePriority;
        ruleStructure.model = model;
        rules.put(ruleId, ruleStructure);
        return ruleId;
    }

    public void unregisterRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            rules.remove(ruleId);
        }
    }

    public Object getRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            return rules.get(ruleId).rule;
        }
        return null;
    }

    public List<Object> getRules() {
        return new ArrayList<>(rules.values());
    }

    public void clearRules() {
        rules.clear();
    }

    public Map<String, Boolean> fireRules() {

        Map<String, Boolean> results = new HashMap<>();
        List<RuleStructure> ruleSet = new ArrayList<>(rules.values());
        Collections.sort(ruleSet);

        for (RuleStructure rule : ruleSet) {

            Boolean result = textualRuleEngine.evaluate(rule.rule, rule.model.getClass(), rule.model);
            results.put(rule.ruleId, result);
        }

        return results;
    }
}
