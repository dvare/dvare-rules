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


package org.dvare.rest.ruleengine;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.rest.resource.RuleResult;
import org.dvare.ruleengine.DvareRuleEngine;

import java.util.*;

public class RestRuleEngine {

    private String name;
    private DvareRuleEngine dvareRuleEngine;


    private Map<String, RuleStructure> rules = new HashMap<>();

    public RestRuleEngine(String name, DvareRuleEngine dvareRuleEngine) {
        this.name = name;
        this.dvareRuleEngine = dvareRuleEngine;
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

    public List<RuleResult> fireRules() throws InterpretException {

        List<RuleResult> results = new ArrayList<>();
        List<RuleStructure> ruleSet = new ArrayList<>(rules.values());
        Collections.sort(ruleSet);

        for (RuleStructure rule : ruleSet) {

            Boolean result = dvareRuleEngine.evaluate();
            RuleResult ruleResult = new RuleResult();
            ruleResult.setRule(rule.rule);
            ruleResult.setRuleName(rule.name);
            ruleResult.setRuleId(rule.ruleId);
            ruleResult.setResult(result);
            results.add(ruleResult);
        }

        return results;
    }
}
