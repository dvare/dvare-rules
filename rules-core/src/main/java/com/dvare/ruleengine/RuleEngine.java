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

import com.dvare.annotations.ConditionType;
import com.dvare.annotations.Rule;
import com.dvare.exceptions.rule.IllegalRuleException;
import com.dvare.rule.BasicRule;
import com.dvare.rule.TextualRule;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The RuleEngine class contains method for rule registering and rule firing.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-08-20
 */
public class RuleEngine {
    Logger logger = Logger.getLogger(RuleEngine.class);

    private Integer satisfyCondition = 0;
    private Boolean stopOnFail = false;
    private TextualRuleEngine textualRuleEngine;
    private List<RuleResult> ruleResults;
    private Map<String, RuleStructure> rules = new HashMap<>();


    public RuleEngine(TextualRuleEngine textualRuleEngine) {
        this.textualRuleEngine = textualRuleEngine;
    }


    /**
     * The registerRule method is used to register rules in a RuleBinding Engine.
     * This method returns the unique id against every register
     * rule which will use  for unregistering rule and rule result.
     *
     * @param rule This param is any class which is annotated with @RuleBinding
     *             annotation and contains at least one method
     *             with @Condition annotation.
     * @return returns the  unique id of rule in RuleBinding Engine.
     * @throws IllegalRuleException This method throws IllegalRuleException
     *                              when the passed object is null or
     *                              not annotated with @RuleBinding annotation or RuleBinding without Condition Method and Text Condition without TextualRuleEngine Param.
     */
    public String registerRule(Object rule) throws IllegalRuleException {

        if (rule == null) {
            throw new IllegalRuleException("Passed Rule is null");
        }


        RuleStructure ruleStructure = null;

        if (rule.getClass().isAnnotationPresent(Rule.class)) {
            AnnotatedRuleParser annotatedRuleParser = new AnnotatedRuleParser();
            annotatedRuleParser.validateRule(rule);
            ruleStructure = annotatedRuleParser.parseRule(rule, rules.values().size());

        } else if (rule instanceof BasicRule || rule instanceof TextualRule) {

            RuleParser ruleParser = new RuleParser();
            ruleStructure = ruleParser.parseRule(rule, rules.values().size());

        } else {
            throw new IllegalRuleException("Passed Rule Must be Annotated with @Rule or implement BasicRule or TextualRule");
        }

        rules.put(ruleStructure.ruleId, ruleStructure);
        return ruleStructure.ruleId;
    }


    /**
     * The unregisterRule method is used to unregister rule from RuleBinding Engine.
     *
     * @param ruleId The ruleId is unique id returns while rule registering
     */
    public void unregisterRule(String ruleId) {
        if (rules.containsKey(ruleId)) {
            rules.remove(ruleId);
        }
    }


    /**
     * The getRule method returns rule from RuleBinding Engine.
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
     * The getRules method returns all rules from RuleBinding Engine.
     *
     * @return rule from rule engine
     */
    public List<Object> getRules() {
        return rules.values().stream().map(ruleStructure -> {
            return ruleStructure.rule;
        }).collect(Collectors.toList());

    }


    /**
     * The clearRules method remove all rules from RuleBinding Engine.
     */
    public void clearRules() {
        rules.clear();
    }

    /**
     * The fireRules trigger all registered rules.
     *
     * @return return Map of ruleId and RuleBinding Result
     */
    public Map<String, Boolean> fireRules() {
        ruleResults = new ArrayList<>();

        List<RuleStructure> ruleSet = new ArrayList<>(rules.values());
        Collections.sort(ruleSet);

        for (RuleStructure rule : ruleSet) {

            try {
                //trigger Condition
                Boolean result = triggerConditions(rule);
                //trigger Listener
                triggerListener(rule, result);
                RuleResult ruleResult = new RuleResult();
                ruleResult.setRuleId(rule.ruleId);
                ruleResult.setRule(rule.rule);
                ruleResult.setResult(result);

                ruleResults.add(ruleResult);

                if (stopOnFail && !result) {
                    break;
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }

        Map<String, Boolean> result = new HashMap<>();
        for (RuleResult ruleResult : ruleResults) {
            result.put(ruleResult.getRuleId(), ruleResult.getResult());
        }

        return result;

    }

    /**
     * The getResults returns RuleBinding Engine Detail Result.
     *
     * @return return Map of ruleId and RuleBinding Result
     * @see RuleResult
     */
    public void getResults() {

    }

    private void triggerBefore(final RuleStructure rule) throws IllegalAccessException, InvocationTargetException {
        List<MethodStructure> beforeMethods = rule.beforeMethods;
        Collections.sort(beforeMethods);
        for (MethodStructure methodStructure : beforeMethods) {
            methodStructure.method.invoke(rule.rule);
        }
    }

    private void triggerAfter(final RuleStructure rule) throws IllegalAccessException, InvocationTargetException {
        List<MethodStructure> afterMethods = rule.afterMethods;
        Collections.sort(afterMethods);
        for (MethodStructure methodStructure : afterMethods) {
            methodStructure.method.invoke(rule.rule);
        }
    }

    private Boolean triggerConditions(RuleStructure rule) throws IllegalAccessException, InvocationTargetException {

        List<ConditionStructure> conditions = rule.conditions;
        Collections.sort(conditions);

        List<Boolean> results = new ArrayList<>();


        for (ConditionStructure conditionStructure : conditions) {
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
                results.add(booleanResult);
            }
        }


        if (satisfyCondition.equals(0)) {

            for (Boolean result : results) {
                if (result) return true;
            }

        } else if (satisfyCondition.equals(1)) {
            for (Boolean result : results) {
                if (!result) return false;
            }
        }


        return true;

    }

    private void triggerListener(RuleStructure rule, Boolean result) throws IllegalAccessException, InvocationTargetException {

        if (result) {
            List<MethodStructure> successMethods = rule.successMethods;
            Collections.sort(successMethods);
            for (MethodStructure methodStructure : successMethods) {
                methodStructure.method.invoke(rule.rule);
            }

        } else {

            List<MethodStructure> failMethods = rule.failMethods;
            Collections.sort(failMethods);
            for (MethodStructure methodStructure : failMethods) {
                methodStructure.method.invoke(rule.rule);
            }
        }

    }


    public void setSatisfyCondition(Integer satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }


    public void setStopOnFail(Boolean stopOnFail) {
        this.stopOnFail = stopOnFail;
    }
}
