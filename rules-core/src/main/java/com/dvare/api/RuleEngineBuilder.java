package com.dvare.api;

import com.dvare.config.RuleConfiguration;
import com.dvare.ruleengine.RuleEngine;
import com.dvare.ruleengine.TextualRuleEngine;

public class RuleEngineBuilder {
    private String[] functionPackages;

    public RuleEngineBuilder() {

    }

    public RuleEngineBuilder functionPackages(String[] functionPackages) {
        this.setFunctionPackages(functionPackages);
        return this;
    }

    public RuleEngine build() {
        RuleConfiguration ruleConfiguration = new RuleConfiguration(functionPackages);
        TextualRuleEngine textualRuleEngine = new TextualRuleEngine(ruleConfiguration);
        RuleEngine ruleEngine = new RuleEngine(textualRuleEngine);
        return ruleEngine;
    }


    private void setFunctionPackages(String[] functionPackages) {
        this.functionPackages = functionPackages;
    }
}
