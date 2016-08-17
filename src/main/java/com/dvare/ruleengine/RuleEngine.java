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


package com.dvare.ruleengine;

import com.dvare.annotations.*;
import com.dvare.exceptions.rule.ConditionNotFoundException;
import com.dvare.exceptions.rule.ConditionParamNotFoundException;
import com.dvare.exceptions.rule.IllegalRuleException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class RuleEngine {

    private TextualRuleEngine textualRuleEngine;
    private Map<String, Object> rules = new HashMap<>();


    public RuleEngine(TextualRuleEngine textualRuleEngine) {
        this.textualRuleEngine = textualRuleEngine;
    }

    public String registerRule(Object rule) throws IllegalRuleException {

        if (rule == null) {
            throw new IllegalRuleException("Passed Rule is null");
        }


        if (!rule.getClass().isAnnotationPresent(Rule.class)) {
            throw new IllegalRuleException("Passed Rule is not annotated with @Rule");
        }


        boolean conditionFound = false;
        for (Method method : rule.getClass().getMethods()) {

            if (method.isAnnotationPresent(Condition.class)) {
                Condition condition = (Condition) method.getAnnotation(Condition.class);
                ConditionType conditionType = condition.type();

                if (!method.getReturnType().equals(Boolean.class) && !method.getReturnType().equals(boolean.class)) {
                    throw new ConditionNotFoundException("Method with Condition Type Code must return Boolean value ");
                }


                if (conditionType.equals(ConditionType.CODE)) {


                } else if (conditionType.equals(ConditionType.TEXT)) {


                    if (method.getParameters() != null && method.getParameters().length > 0) {

                        Parameter parameter = method.getParameters()[0];
                        if (!parameter.getType().equals(TextualRuleEngine.class)) {
                            throw new ConditionParamNotFoundException("Method Condition Type DVARE not contain TextualRuleEngine param");
                        }

                    } else {
                        throw new ConditionParamNotFoundException("Method Condition Type DVARE not contain TextualRuleEngine param ");
                    }


                }
                conditionFound = true;
            }

        }


        if (!conditionFound) {
            throw new ConditionNotFoundException("Passed Rule not contain any  Method  annotated with @Condition");
        }


        Rule ruleDetails = rule.getClass().getAnnotation(Rule.class);


        String ruleId = ruleDetails.name() + rules.values().size();

        rules.put(ruleId, rule);

        return ruleId;
    }

    public void unregisterRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            rules.remove(ruleId);
        }
    }


    public void clearRules() {
        rules.clear();
    }


    public Collection<Object> getRules() {
        return rules.values();
    }

    public List<RuleResult> fireRules() {

        List<RuleStructure> ruleStructures = filterRules(rules);

        List<RuleResult> ruleResults = ruleStructures.stream().sorted().map(ruleStructure -> {

            RuleResult ruleResult = new RuleResult();
            ruleResult.ruleId = ruleStructure.ruleId;

            List<Boolean> conditionResults = ruleStructure.conditions.stream().sorted().map(conditionStructure -> {


                try {

                    // before condition

                    for (Method before : ruleStructure.beforeMethods) {
                        before.invoke(ruleStructure.rule);
                    }

                    //condition

                    Object condtionResult = null;
                    if (conditionStructure.conditionType.equals(ConditionType.CODE)) {
                        condtionResult = conditionStructure.condition.invoke(ruleStructure.rule);
                    } else if (conditionStructure.conditionType.equals(ConditionType.TEXT)) {
                        condtionResult = conditionStructure.condition.invoke(ruleStructure.rule, textualRuleEngine);
                    }


                    // after condition
                    for (Method before : ruleStructure.afterMethods) {
                        before.invoke(ruleStructure.rule);
                    }

                    // condition action

                    if (condtionResult instanceof Boolean) {
                        Boolean booleanResult = (Boolean) condtionResult;

                        if (booleanResult) {
                            for (Method success : ruleStructure.successMethods) {
                                success.invoke(ruleStructure.rule);
                            }
                        } else {
                            for (Method fail : ruleStructure.failMethods) {
                                fail.invoke(ruleStructure.rule);
                            }
                        }

                        return booleanResult;
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                return null;

            }).collect(Collectors.toList());


            ruleResult.conditionResults = conditionResults;

            return ruleResult;

        }).collect(Collectors.toList());
        ;

        return ruleResults;

    }


    private List<RuleStructure> filterRules(Map<String, Object> rules) {

        List<RuleStructure> ruleStructures = rules.keySet().stream().map(ruleId -> {

            Object rule = rules.get(ruleId);
            if (rule.getClass().isAnnotationPresent(Rule.class)) {
                Rule ruleDetail = rule.getClass().getAnnotation(Rule.class);
                RuleStructure ruleStructure = new RuleStructure();
                ruleStructure.ruleId = ruleId;
                ruleStructure.rule = rule;
                ruleStructure.priority = ruleDetail.priority();
                for (Method method : rule.getClass().getMethods()) {
                    if (method.isAnnotationPresent(Condition.class)) {
                        Condition condition = method.getAnnotation(Condition.class);

                        ConditionStructure conditionStructure = new ConditionStructure();
                        conditionStructure.order = condition.order();
                        conditionStructure.condition = method;
                        conditionStructure.conditionType = condition.type();
                        ruleStructure.conditions.add(conditionStructure);
                    } else if (method.isAnnotationPresent(Before.class)) {
                        ruleStructure.beforeMethods.add(method);
                    } else if (method.isAnnotationPresent(After.class)) {
                        ruleStructure.afterMethods.add(method);
                    } else if (method.isAnnotationPresent(Success.class)) {
                        ruleStructure.successMethods.add(method);
                    } else if (method.isAnnotationPresent(Fail.class)) {
                        ruleStructure.failMethods.add(method);
                    }

                }
                return ruleStructure;

            }
            return null;

        }).collect(Collectors.toList());


        return ruleStructures;
    }

    private class RuleStructure implements Comparable<RuleStructure> {
        String ruleId;
        Object rule;
        Integer priority;
        List<ConditionStructure> conditions = new ArrayList<>();
        List<Method> successMethods = new ArrayList<>();
        List<Method> failMethods = new ArrayList<>();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();


        @Override
        public int compareTo(RuleStructure other) {
            return this.priority.compareTo(other.priority);
        }
    }


    private class ConditionStructure implements Comparable<ConditionStructure> {
        Method condition;
        Integer order;
        ConditionType conditionType;

        @Override
        public int compareTo(ConditionStructure other) {
            return this.order.compareTo(other.order);
        }
    }


    public class RuleResult {
        String ruleId;
        List<Boolean> conditionResults = new ArrayList<>();
    }

}
