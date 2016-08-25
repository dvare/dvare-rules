package org.dvare.rest.ruleengine;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;
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

    public String registerRule(String ruleName, Integer rulePriority, String rule, TypeBinding typeBinding, DataRow dataRow) {
        String ruleId = name + (rules.size() + 1);
        RuleStructure ruleStructure = new RuleStructure();
        ruleStructure.ruleId = ruleId;
        ruleStructure.name = ruleName;
        ruleStructure.rule = rule;
        ruleStructure.priority = rulePriority;
        ruleStructure.typeBinding = typeBinding;
        ruleStructure.dataRow = dataRow;
        rules.put(ruleId, ruleStructure);
        return ruleId;
    }

    public void unregisterRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            rules.remove(ruleId);
        }
    }

    public RuleStructure getRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            return rules.get(ruleId);
        }
        return null;
    }

    public List<RuleStructure> getRules() {
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

            Boolean result = textualRuleEngine.evaluate(rule.rule, rule.typeBinding, rule.dataRow);
            results.put(rule.ruleId, result);
        }

        return results;
    }
}
