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


package org.dvare.rest.service;


import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.resource.RuleResult;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.rest.ruleengine.RuleStructure;

import java.util.List;

public class RuleEngineService {

    private RuleEngineRegistry ruleEngineRegistry = null;

    public RuleEngineService(RuleEngineRegistry ruleEngineRegistry) {
        this.ruleEngineRegistry = ruleEngineRegistry;
    }


    public List<RuleResult> fireRules(final String ruleEngine) throws InterpretException {

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
