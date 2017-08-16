package org.dvare.api;

import org.dvare.config.RuleConfiguration;
import org.dvare.ruleengine.AggregationRuleEngine;
import org.dvare.ruleengine.DvareRuleEngine;
import org.dvare.ruleengine.RuleEngine;

public class RuleEngineBuilder {
    private Integer satisfyCondition = 0;
    private String[] functionPackages;
    private Boolean stopOnFail = false;
    private Facts facts;

    public RuleEngineBuilder() {

    }

    public RuleEngineBuilder functionPackages(String[] functionPackages) {
        this.setFunctionPackages(functionPackages);
        return this;
    }

    public RuleEngineBuilder satisfyCondition(Integer satisfyCondition) {
        this.setSatisfyCondition(satisfyCondition);
        return this;
    }

    public RuleEngineBuilder stopOnFail(Boolean stopOnFail) {
        this.setStopOnFail(stopOnFail);
        return this;
    }

    public RuleEngineBuilder facts(Facts facts) {
        this.setFacts(facts);
        return this;
    }

    public RuleEngine build() {
        RuleConfiguration ruleConfiguration = new RuleConfiguration(functionPackages);
        DvareRuleEngine textualRuleEngine = new DvareRuleEngine(ruleConfiguration);
        AggregationRuleEngine aggregationRuleEngine = new AggregationRuleEngine(ruleConfiguration);
        RuleEngine ruleEngine = new RuleEngine(facts, textualRuleEngine, aggregationRuleEngine);
        ruleEngine.setSatisfyCondition(satisfyCondition);
        ruleEngine.setStopOnFail(stopOnFail);
        return ruleEngine;
    }



    private void setFunctionPackages(String[] functionPackages) {
        this.functionPackages = functionPackages;
    }

    private void setSatisfyCondition(Integer satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }

    private void setStopOnFail(Boolean stopOnFail) {
        this.stopOnFail = stopOnFail;
    }

    private void setFacts(Facts facts) {
        this.facts = facts;
    }
}
