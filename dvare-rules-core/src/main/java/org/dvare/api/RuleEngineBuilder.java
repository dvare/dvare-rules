package org.dvare.api;

import org.dvare.config.RuleConfiguration;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.ruleengine.TextualRuleEngine;

public class RuleEngineBuilder {
    private Integer satisfyCondition = 0;
    private String[] functionPackages;
    private Boolean stopOnFail = false;

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

    public RuleEngine build() {
        RuleConfiguration ruleConfiguration = new RuleConfiguration(functionPackages);
        TextualRuleEngine textualRuleEngine = new TextualRuleEngine(ruleConfiguration);
        RuleEngine ruleEngine = new RuleEngine(textualRuleEngine);
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
}
