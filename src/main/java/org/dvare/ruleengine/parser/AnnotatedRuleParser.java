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

import org.dvare.annotations.*;
import org.dvare.exceptions.ConditionNotFoundException;
import org.dvare.exceptions.ConditionParamNotFoundException;
import org.dvare.ruleengine.structure.MethodStructure;
import org.dvare.ruleengine.structure.RuleStructure;

import java.lang.reflect.Method;

public class AnnotatedRuleParser {

    public RuleStructure parseRule(Object rule, int size) {

        Rule ruleDetails = rule.getClass().getAnnotation(Rule.class);
        String ruleId = ruleDetails.name() + size;

        if (rule.getClass().isAnnotationPresent(Rule.class)) {
            Rule ruleDetail = rule.getClass().getAnnotation(Rule.class);
            RuleStructure ruleStructure = new RuleStructure();
            ruleStructure.setRuleId(ruleId);
            ruleStructure.setRule(rule);

            ruleStructure.setPriority(ruleDetail.priority());
            for (Method method : rule.getClass().getMethods()) {
                if (method.isAnnotationPresent(Condition.class)) {
                    Condition condition = method.getAnnotation(Condition.class);
                    MethodStructure conditionStructure = new MethodStructure();
                    conditionStructure.setOrder(condition.order());
                    conditionStructure.setMethod(method);
                    ruleStructure.getConditions().add(conditionStructure);
                } else if (method.isAnnotationPresent(Aggregation.class)) {
                    Aggregation aggregation = method.getAnnotation(Aggregation.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.setMethod(method);
                    ruleStructure.setAggregation(methodStructure);
                } else if (method.isAnnotationPresent(Before.class)) {
                    Before before = method.getAnnotation(Before.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.setOrder(before.order());
                    methodStructure.setMethod(method);
                    ruleStructure.getBeforeMethods().add(methodStructure);
                } else if (method.isAnnotationPresent(After.class)) {
                    After after = method.getAnnotation(After.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.setOrder(after.order());
                    methodStructure.setMethod(method);
                    ruleStructure.getAfterMethods().add(methodStructure);
                } else if (method.isAnnotationPresent(Success.class)) {
                    Success success = method.getAnnotation(Success.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.setOrder(success.order());
                    methodStructure.setMethod(method);
                    ruleStructure.getSuccessMethods().add(methodStructure);
                } else if (method.isAnnotationPresent(Fail.class)) {
                    Fail fail = method.getAnnotation(Fail.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.setOrder(fail.order());
                    methodStructure.setMethod(method);
                    ruleStructure.getFailMethods().add(methodStructure);
                }

            }
            return ruleStructure;

        }
        return null;


    }


    public void validateRule(Object rule) throws ConditionNotFoundException, ConditionParamNotFoundException {

        boolean conditionFound = false;
        for (Method method : rule.getClass().getMethods()) {

            if (method.isAnnotationPresent(Condition.class)) {
                Condition condition = (Condition) method.getAnnotation(Condition.class);

                if (!method.getReturnType().equals(Boolean.class) && !method.getReturnType().equals(boolean.class)) {
                    throw new ConditionNotFoundException("Condition method must return Boolean value ");
                }

                conditionFound = true;
            } else if (method.isAnnotationPresent(Aggregation.class)) {
                conditionFound = true;
            }


        }


        if (!conditionFound) {
            throw new ConditionNotFoundException(" Rule not contain any Method annotated with @Condition or @Aggregation");
        }

    }

}
