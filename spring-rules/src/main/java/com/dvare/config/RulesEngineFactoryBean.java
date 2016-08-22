package com.dvare.config;


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
