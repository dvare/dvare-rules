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


package org.dvare.ruleengine;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.dvare.annotations.ConditionType;
import org.dvare.rule.BasicRule;
import org.dvare.rule.TextualRule;

import java.lang.reflect.Method;

class RuleParser {

    RuleStructure parseRule(Object rule, int size) {


        try {
            Object name = MethodUtils.invokeExactMethod(rule, "getName");

            Object priorityObject = MethodUtils.invokeExactMethod(rule, "priority");

            Integer priority = 0;
            if (priorityObject != null) {
                priority = (Integer) priorityObject;
            }


            String ruleId = null;
            if (name != null) {
                ruleId = (String) name + size;
            } else {
                ruleId = "" + size;
            }


            RuleStructure ruleStructure = new RuleStructure();
            ruleStructure.ruleId = ruleId;
            ruleStructure.rule = rule;
            ruleStructure.priority = priority;


            Method before = MethodUtils.getAccessibleMethod(rule.getClass(), "before");
            MethodStructure beforeStructure = new MethodStructure();
            beforeStructure.order = 0;
            beforeStructure.method = before;
            ruleStructure.beforeMethods.add(beforeStructure);


            if (rule instanceof BasicRule) {
                Method condition = MethodUtils.getAccessibleMethod(rule.getClass(), "condition");
                ConditionStructure conditionStructure = new ConditionStructure();
                conditionStructure.order = 0;
                conditionStructure.conditionType = ConditionType.CODE;
                conditionStructure.condition = condition;
                ruleStructure.conditions.add(conditionStructure);
            } else if (rule instanceof TextualRule) {
                Method condition = MethodUtils.getAccessibleMethod(rule.getClass(), "condition", new Class[]{TextualRuleEngine.class});
                ConditionStructure conditionStructure = new ConditionStructure();
                conditionStructure.order = 0;
                conditionStructure.conditionType = ConditionType.TEXT;
                conditionStructure.condition = condition;
                ruleStructure.conditions.add(conditionStructure);
            }

            Method after = MethodUtils.getAccessibleMethod(rule.getClass(), "after");
            MethodStructure afterStructure = new MethodStructure();
            afterStructure.order = 0;
            afterStructure.method = after;
            ruleStructure.afterMethods.add(afterStructure);


            Method success = MethodUtils.getAccessibleMethod(rule.getClass(), "success");
            MethodStructure successStructure = new MethodStructure();
            successStructure.order = 0;
            successStructure.method = success;
            ruleStructure.afterMethods.add(successStructure);


            Method fail = MethodUtils.getAccessibleMethod(rule.getClass(), "fail");
            MethodStructure failStructure = new MethodStructure();
            failStructure.order = 0;
            failStructure.method = fail;
            ruleStructure.afterMethods.add(failStructure);

            return ruleStructure;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
