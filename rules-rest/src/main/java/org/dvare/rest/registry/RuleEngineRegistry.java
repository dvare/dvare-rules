package org.dvare.rest.registry;


import org.dvare.config.RuleConfiguration;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.ruleengine.TextualRuleEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleEngineRegistry {

    private Map<String, RestRuleEngine> registry = new HashMap<>();

    public RuleEngineRegistry() {
    }


    public String createNewEngine(String sessionId) {
        RuleConfiguration ruleConfiguration = new RuleConfiguration();
        TextualRuleEngine textualRuleEngine = new TextualRuleEngine(ruleConfiguration);
        RestRuleEngine restRuleEngine = new RestRuleEngine(sessionId, textualRuleEngine);
        registry.put(sessionId, restRuleEngine);
        return sessionId;
    }


    public List<String> getRuleEngines() {
        return new ArrayList<>(registry.keySet());
    }

    public RestRuleEngine getRuleEngine(String sessionId) {
        if (sessionId != null && registry.containsKey(sessionId)) {
            return registry.get(sessionId);
        }
        return null;
    }

    public void unregisterRuleEngine(String sessionId) {
        if (sessionId != null && registry.containsKey(sessionId)) {
            registry.remove(sessionId);
        }
    }


    public void clearRegistry() {
        registry.clear();
    }


}
