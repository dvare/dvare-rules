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


package com.dvare.spring.config;


import com.dvare.api.RuleEngineBuilder;
import com.dvare.exceptions.rule.IllegalRuleException;
import com.dvare.ruleengine.RuleEngine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.List;

public class RulesEngineFactoryBean implements FactoryBean<RuleEngine> {
    Logger logger = Logger.getLogger(RulesEngineFactoryBean.class);
    private String[] functionPackages;
    private List<Object> rules = new ArrayList<>();

    @Override
    public RuleEngine getObject() throws Exception {
        RuleEngineBuilder ruleEngineBuilder = new RuleEngineBuilder();
        ruleEngineBuilder.functionPackages(functionPackages);
        RuleEngine ruleEngine = ruleEngineBuilder.build();
        registerRules(ruleEngine);
        return ruleEngine;
    }

    private void registerRules(RuleEngine ruleEngine) {
        if (rules != null && !rules.isEmpty()) {
            for (Object rule : rules) {
                try {
                    ruleEngine.registerRule(rule);
                } catch (IllegalRuleException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public Class<RuleEngine> getObjectType() {
        return RuleEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String[] getFunctionPackages() {
        return functionPackages;
    }

    public void setFunctionPackages(String[] functionPackages) {
        this.functionPackages = functionPackages;
    }

    public List<Object> getRules() {
        return rules;
    }

    public void setRules(List<Object> rules) {
        this.rules = rules;
    }
}
