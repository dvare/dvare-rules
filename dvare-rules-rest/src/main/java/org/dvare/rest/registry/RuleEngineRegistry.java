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


package org.dvare.rest.registry;


import org.dvare.config.RuleConfiguration;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.ruleengine.DvareRuleEngine;

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
        DvareRuleEngine textualRuleEngine = new DvareRuleEngine(ruleConfiguration);
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
