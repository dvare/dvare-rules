/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


package org.dvare.ruleengine.parser;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.dvare.api.BasicRule;
import org.dvare.api.ListenerStructure;
import org.dvare.api.RuleStructure;
import org.dvare.rule.DvareRule;
import org.dvare.ruleengine.DvareRuleEngine;

import java.lang.reflect.Method;

public class ObjectRuleParser {


    public static RuleStructure parseRule(Object rule, int size) {


        try {
            Object name = MethodUtils.invokeExactMethod(rule, "getName");

            Object priorityObject = MethodUtils.invokeExactMethod(rule, "priority");

            Integer priority = 0;
            if (priorityObject != null) {
                priority = (Integer) priorityObject;
            }


            String ruleId;
            if (name != null) {
                ruleId = (String) name + size;
            } else {
                ruleId = "" + size;
            }


            RuleStructure ruleStructure = new RuleStructure();
            ruleStructure.setId(ruleId);
            ruleStructure.setRule(rule);
            ruleStructure.setPriority(priority);


            Method before = MethodUtils.getAccessibleMethod(rule.getClass(), "before");
            ListenerStructure beforeStructure = new ListenerStructure();
            beforeStructure.setOrder(0);
            beforeStructure.setListener(before);
            ruleStructure.getBeforeListeners().add(beforeStructure);


            if (rule instanceof BasicRule) {
                Method condition = MethodUtils.getAccessibleMethod(rule.getClass(), "condition");
                ListenerStructure MethodStructure = new ListenerStructure();
                MethodStructure.setOrder(0);
                MethodStructure.setListener(condition);
                ruleStructure.getConditions().add(MethodStructure);
            } else if (rule instanceof DvareRule) {
                Method condition = MethodUtils.getAccessibleMethod(rule.getClass(), "condition", DvareRuleEngine.class);
                ListenerStructure MethodStructure = new ListenerStructure();
                MethodStructure.setOrder(0);
                MethodStructure.setListener(condition);
                ruleStructure.getConditions().add(MethodStructure);
            }


            Method after = MethodUtils.getAccessibleMethod(rule.getClass(), "after");
            ListenerStructure afterStructure = new ListenerStructure();
            afterStructure.setOrder(0);
            afterStructure.setListener(after);
            ruleStructure.getAfterListeners().add(afterStructure);


            Method success = MethodUtils.getAccessibleMethod(rule.getClass(), "success");
            ListenerStructure successStructure = new ListenerStructure();
            successStructure.setOrder(0);
            successStructure.setListener(success);
            ruleStructure.getSuccessListeners().add(successStructure);


            Method fail = MethodUtils.getAccessibleMethod(rule.getClass(), "fail");
            ListenerStructure failStructure = new ListenerStructure();
            failStructure.setOrder(0);
            failStructure.setListener(fail);
            ruleStructure.getFailListeners().add(failStructure);

            return ruleStructure;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
