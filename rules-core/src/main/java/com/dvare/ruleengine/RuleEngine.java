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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The RuleEngine class contains method for rule registering and rule firing.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-08-20
 */
public class RuleEngine {

    private TextualRuleEngine textualRuleEngine;
    private List<RuleResult> ruleResults;
    private Map<String, RuleStructure> rules = new HashMap<>();


    public RuleEngine(TextualRuleEngine textualRuleEngine) {
        this.textualRuleEngine = textualRuleEngine;
    }

    /**
     * The registerRule method is used to register rules in a Rule Engine.
     * This method returns the unique id against every register
     * rule which will use  for unregistering rule and rule result.
     *
     * @param rule This param is any class which is annotated with @Rule
     *             annotation and contains at least one method
     *             with @Condition annotation.
     * @return returns the  unique id of rule in Rule Engine.
     * @throws IllegalRuleException This method throws IllegalRuleException
     *                              when the passed object is null or
     *                              not annotated with @Rule annotation or Rule without Condition Method and Text Condition without TextualRuleEngine Param.
     */
    public String registerRule(Object rule) throws IllegalRuleException {

        if (rule == null) {
            throw new IllegalRuleException("Passed Rule is null");
        }

        if (!rule.getClass().isAnnotationPresent(Rule.class)) {
            throw new IllegalRuleException("Passed Rule is not annotated with @Rule");
        }


        // test Validation in rule
        ruleValidation(rule);


        Rule ruleDetails = rule.getClass().getAnnotation(Rule.class);
        String ruleId = ruleDetails.name() + rules.values().size();

        RuleStructure ruleStructure = parseRule(rule, ruleId);

        rules.put(ruleId, ruleStructure);
        return ruleId;
    }


    private void ruleValidation(Object rule) throws ConditionNotFoundException, ConditionParamNotFoundException {

        boolean conditionFound = false;
        for (Method method : rule.getClass().getMethods()) {

            if (method.isAnnotationPresent(Condition.class)) {
                Condition condition = (Condition) method.getAnnotation(Condition.class);
                ConditionType conditionType = condition.type();

                if (!method.getReturnType().equals(Boolean.class) && !method.getReturnType().equals(boolean.class)) {
                    throw new ConditionNotFoundException("Method with Condition Type Code must return Boolean value ");
                }

                if (conditionType.equals(ConditionType.TEXT)) {
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

    }

    private RuleStructure parseRule(Object rule, String ruleId) {

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
                    Before before = method.getAnnotation(Before.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.order = before.order();
                    methodStructure.method = method;
                    ruleStructure.beforeMethods.add(methodStructure);
                } else if (method.isAnnotationPresent(After.class)) {
                    After after = method.getAnnotation(After.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.order = after.order();
                    methodStructure.method = method;
                    ruleStructure.afterMethods.add(methodStructure);
                } else if (method.isAnnotationPresent(Success.class)) {
                    Success success = method.getAnnotation(Success.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.order = success.order();
                    methodStructure.method = method;
                    ruleStructure.successMethods.add(methodStructure);
                } else if (method.isAnnotationPresent(Fail.class)) {
                    Fail fail = method.getAnnotation(Fail.class);
                    MethodStructure methodStructure = new MethodStructure();
                    methodStructure.order = fail.order();
                    methodStructure.method = method;
                    ruleStructure.failMethods.add(methodStructure);
                }

            }
            return ruleStructure;

        }
        return null;


    }

    /**
     * The unregisterRule method is used to unregister rule from Rule Engine.
     *
     * @param ruleId The ruleId is unique id returns while rule registering
     */
    public void unregisterRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            rules.remove(ruleId);
        }
    }


    /**
     * The getRule method returns rule from Rule Engine.
     *
     * @param ruleId The ruleId is unique id returns while rule registering
     * @return rule from rule engine
     */
    public Object getRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            return rules.get(ruleId).rule;
        }
        return null;
    }


    /**
     * The getRules method returns all rules from Rule Engine.
     *
     * @return rule from rule engine
     */
    public List<Object> getRules() {
        return rules.values().stream().map(ruleStructure -> {
            return ruleStructure.rule;
        }).collect(Collectors.toList());

    }


    /**
     * The clearRules method remove all rules from Rule Engine.
     */
    public void clearRules() {
        rules.clear();
    }

    /**
     * The fireRules trigger all registered rules.
     *
     * @return return Map of ruleId and Rule Result
     */
    public Map<String, Boolean> fireRules() {

        ruleResults = rules.values().stream().sorted().map(rule -> {

            //trigger Condition
            Boolean result = triggerConditions(rule);
            //trigger Listener
            triggerListener(rule, result);
            RuleResult ruleResult = new RuleResult();
            ruleResult.ruleId = rule.ruleId;
            ruleResult.rule = rule.rule;
            ruleResult.result = result;

            return ruleResult;

        }).collect(Collectors.toList());


        Map<String, Boolean> result = new HashMap<>();


        ruleResults.stream().forEach(ruleResult -> {
            result.put(ruleResult.getRuleId(), ruleResult.getResult());
        });

        return result;

    }

    /**
     * The getResults returns Rule Engine Detail Result.
     *
     * @return return Map of ruleId and Rule Result
     * @see RuleResult
     */
    public void getResults() {

    }

    private void triggerBefore(final RuleStructure rule) {
        rule.beforeMethods.stream().sorted().forEach(methodStructure -> {
            try {
                methodStructure.method.invoke(rule.rule);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private void triggerAfter(final RuleStructure rule) {
        rule.afterMethods.stream().sorted().forEach(methodStructure -> {
            try {
                methodStructure.method.invoke(rule.rule);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private Boolean triggerConditions(RuleStructure rule) {
        List<Boolean> results = rule.conditions.stream().sorted().map(conditionStructure -> {

            try {
                // before condition
                triggerBefore(rule);

                //condition

                Object conditionResult = null;
                if (conditionStructure.conditionType.equals(ConditionType.CODE)) {
                    conditionResult = conditionStructure.condition.invoke(rule.rule);
                } else if (conditionStructure.conditionType.equals(ConditionType.TEXT)) {
                    conditionResult = conditionStructure.condition.invoke(rule.rule, textualRuleEngine);
                }

                // after condition
                triggerAfter(rule);

                // return result

                if (conditionResult instanceof Boolean) {
                    Boolean booleanResult = (Boolean) conditionResult;
                    return booleanResult;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;

        }).collect(Collectors.toList());

        for (Boolean result : results) {
            if (!result) return false;
        }

        return true;

    }

    private void triggerListener(RuleStructure rule, Boolean result) {

        if (result) {
            rule.successMethods.stream().sorted().forEach(methodStructure -> {
                try {
                    methodStructure.method.invoke(rule.rule);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } else {
            rule.failMethods.stream().sorted().forEach(methodStructure -> {
                try {
                    methodStructure.method.invoke(rule.rule);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    private class RuleStructure implements Comparable<RuleStructure> {
        String ruleId;
        Object rule;
        Integer priority;
        List<ConditionStructure> conditions = new ArrayList<>();
        List<MethodStructure> successMethods = new ArrayList<>();
        List<MethodStructure> failMethods = new ArrayList<>();
        List<MethodStructure> beforeMethods = new ArrayList<>();
        List<MethodStructure> afterMethods = new ArrayList<>();


        @Override
        public int compareTo(RuleStructure other) {
            return this.priority.compareTo(other.priority);
        }
    }

    private class MethodStructure implements Comparable<ConditionStructure> {
        Method method;
        Integer order;

        @Override
        public int compareTo(ConditionStructure other) {
            return this.order.compareTo(other.order);
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
        Object rule;
        Boolean result;

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public Object getRule() {
            return rule;
        }

        public void setRule(Object rule) {
            this.rule = rule;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }
    }

}
