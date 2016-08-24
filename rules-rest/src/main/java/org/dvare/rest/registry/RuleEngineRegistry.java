package org.dvare.rest.registry;


import org.dvare.config.RuleConfiguration;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.ruleengine.TextualRuleEngine;

import java.util.HashMap;
import java.util.Map;

public class RuleEngineRegistry {

    private static RuleEngineRegistry ruleEngineRegistry;
    public static RuleEngineRegistry INSTANCE = instance();
    private Map<String, RestRuleEngine> registry = new HashMap<>();

    private RuleEngineRegistry() {
    }

    private static RuleEngineRegistry instance() {
        if (ruleEngineRegistry == null) {
            ruleEngineRegistry = new RuleEngineRegistry();
        }
        return ruleEngineRegistry;
    }

    public String createNewEngine(String sessionId) {
        RuleConfiguration ruleConfiguration = new RuleConfiguration();
        TextualRuleEngine textualRuleEngine = new TextualRuleEngine(ruleConfiguration);
        RestRuleEngine restRuleEngine = new RestRuleEngine(sessionId, textualRuleEngine);
        registry.put(sessionId, restRuleEngine);
        return sessionId;
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


}
