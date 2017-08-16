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


package org.dvare.ruleengine;

import org.apache.log4j.Logger;
import org.dvare.annotations.Fact;
import org.dvare.annotations.Rule;
import org.dvare.annotations.RuleEngineType;
import org.dvare.api.BasicRule;
import org.dvare.api.Facts;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.rule.DvareRule;
import org.dvare.ruleengine.parser.AnnotatedRuleParser;
import org.dvare.ruleengine.parser.RuleParser;
import org.dvare.ruleengine.structure.MethodStructure;
import org.dvare.ruleengine.structure.RuleResult;
import org.dvare.ruleengine.structure.RuleStructure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * The RuleEngine class contains method for rule registering and rule firing.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-08-20
 */
public class RuleEngine {
    private static Logger logger = Logger.getLogger(RuleEngine.class);

    private Integer satisfyCondition = 0;
    private Boolean stopOnFail = false;
    private DvareRuleEngine textualRuleEngine;
    private AggregationRuleEngine aggregationRuleEngine;
    private Facts facts;
    private List<RuleResult> ruleResults;
    private Map<String, RuleStructure> rules = new HashMap<>();

    public RuleEngine(Facts facts, DvareRuleEngine textualRuleEngine, AggregationRuleEngine aggregationRuleEngine) {
        if (facts != null) {
            this.facts = facts;
        } else {
            this.facts = new Facts();
        }
        this.textualRuleEngine = textualRuleEngine;
        this.aggregationRuleEngine = aggregationRuleEngine;
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


        RuleStructure ruleStructure;

        if (rule.getClass().isAnnotationPresent(Rule.class)) {
            AnnotatedRuleParser annotatedRuleParser = new AnnotatedRuleParser();
            annotatedRuleParser.validateRule(rule);
            ruleStructure = annotatedRuleParser.parseRule(rule, rules.values().size());

        } else if (rule instanceof BasicRule || rule instanceof DvareRule) {

            ruleStructure = new RuleParser().parseRule(rule, rules.values().size());

        } else {
            throw new IllegalRuleException("Passed Rule Must be Annotated with @Rule or implement BasicRule or TextualRule");
        }

        rules.put(ruleStructure.getRuleId(), ruleStructure);

        return ruleStructure.getRuleId();
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
            return rules.get(ruleId).getRule();
        }
        return null;
    }


    /**
     * The getRules method returns all rules from RuleBinding Engine.
     *
     * @return List rule from rule engine
     */
    public List<Object> getRules() {
        return new ArrayList<>(rules.values());
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
     * @return Map  of ruleId and RuleBinding Result
     */
    public Map<String, Boolean> fireRules() {
        ruleResults = new ArrayList<>();

        List<RuleStructure> ruleSet = new ArrayList<>(rules.values());
        Collections.sort(ruleSet);

        for (RuleStructure ruleStructure : ruleSet) {

            RuleResult ruleResult = triggerRule(ruleStructure);
            ruleResults.add(ruleResult);
            if (stopOnFail && !ruleResult.getResult()) {
                break;
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
     * @return List of RuleResult and RuleBinding Result
     * @see RuleResult
     */
    public List<RuleResult> getResults() {
        return ruleResults;
    }

    public RuleResult getResult(String ruleId) {
        for (RuleResult result : ruleResults) {
            if (result.getRuleId().equals(ruleId)) {
                return result;
            }
        }
        return null;
    }

    /**
     * The getResults returns Rule Binding Engine Detail Result.
     *
     * @return List of RuleResult and RuleBinding Result
     * @see RuleResult
     */
    public RuleResult getResult(Object rule) {
        for (RuleResult result : ruleResults) {
            if (result.getRule().equals(rule)) {
                return result;
            }
        }
        return null;
    }


    /**
     * trigger rule methods
     *
     * @param ruleStructure rule Structure
     * @return RuleResult Rule Binding Engine Detail Result
     */
    private RuleResult triggerRule(RuleStructure ruleStructure) {

        RuleResult ruleResult = new RuleResult();
        ruleResult.setRuleId(ruleStructure.getRuleId());
        ruleResult.setRule(ruleStructure.getRule());

        try {
            //trigger Condition
            boolean result = triggerConditions(ruleStructure.getBeforeMethods(),
                    ruleStructure.getConditions(), ruleStructure.getAfterMethods(), ruleStructure.getRule());


            ruleResult.setResult(result);

            //trigger success and fail Listener
            triggerListener(ruleStructure, result);

            //trigger aggregation
            if (ruleStructure.getAggregation() != null) {
                InstancesBinding instancesBinding = triggerAggregation(ruleStructure.getAggregation(), ruleStructure.getRule());
                ruleResult.setAggregationResult(instancesBinding);
            }


        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                logger.error(e.getCause().getMessage(), e.getCause());
            } else {
                logger.error(e.getMessage(), e);
            }
            ruleResult.setResult(false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ruleResult.setResult(false);
        }

        return ruleResult;

    }


    /**
     * trigger condition methods
     *
     * @param afterMethods before Methods
     * @param rule         rule Instance
     */
    private boolean triggerConditions(List<MethodStructure> beforeMethods, List<MethodStructure> conditions,
                                      List<MethodStructure> afterMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException {


        Collections.sort(conditions);

        List<Boolean> results = new ArrayList<>();


        for (MethodStructure conditionStructure : conditions) {
            // before condition
            triggerBefore(beforeMethods, rule);

            //condition
            Object conditionResult;

            Method condition = conditionStructure.getMethod();

            List<Object> params = new ArrayList<>();
            for (Parameter parameter : condition.getParameters()) {

                Fact fact = parameter.getAnnotation(Fact.class);
                if (fact != null) {
                    String factName = fact.value();
                    Object param = facts.get(factName);
                    params.add(param);

                } else {

                    RuleEngineType engineType = parameter.getAnnotation(RuleEngineType.class);
                    if (engineType != null) {
                        params.add(textualRuleEngine);

                    }
                }
            }

            Object args[] = params.toArray();
            conditionResult = conditionStructure.getMethod().invoke(rule, args);


            // after condition
            triggerAfter(afterMethods, rule);


            // return result
            if (conditionResult instanceof Boolean) {
                Boolean booleanResult = (Boolean) conditionResult;
                results.add(booleanResult);
            }
        }


        if (satisfyCondition.equals(0)) { //any match

            for (Boolean result : results) {
                if (result) return true;
            }

            return false;
        } else if (satisfyCondition.equals(1)) {
            for (Boolean result : results) {
                if (!result) return false;
            }
            return true;
        }


        return true;

    }


    /**
     * trigger before method condition
     *
     * @param beforeMethods before Methods
     * @param rule          rule Instance
     */
    private void triggerBefore(List<MethodStructure> beforeMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException {
        Collections.sort(beforeMethods);
        for (MethodStructure methodStructure : beforeMethods) {
            methodStructure.getMethod().invoke(rule);
        }
    }

    /**
     * trigger after method condition
     *
     * @param afterMethods before Methods
     * @param rule         rule Instance
     */
    private void triggerAfter(List<MethodStructure> afterMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException {
        Collections.sort(afterMethods);
        for (MethodStructure methodStructure : afterMethods) {
            methodStructure.getMethod().invoke(rule);
        }
    }


    private InstancesBinding triggerAggregation(MethodStructure methodStructure, Object rule)
            throws IllegalAccessException, InvocationTargetException, CloneNotSupportedException, InterpretException {
        if (methodStructure != null) {

            List<Object> params = new ArrayList<>();
            for (Parameter parameter : methodStructure.getMethod().getParameters()) {
                Fact fact = parameter.getAnnotation(Fact.class);
                if (fact != null) {
                    String factName = fact.value();
                    Object param = facts.get(factName);
                    params.add(param);
                } else {
                    RuleEngineType engineType = parameter.getAnnotation(RuleEngineType.class);
                    if (engineType != null) {
                        params.add(aggregationRuleEngine);
                    }
                }
            }

            Object args[] = params.toArray();

            Object instance = methodStructure.getMethod().invoke(rule, args);

            if (instance instanceof InstancesBinding) {
                return (InstancesBinding) instance;
            }

        }
        return null;
    }


    private void triggerListener(RuleStructure rule, Boolean result)
            throws IllegalAccessException, InvocationTargetException {

        if (result) {
            List<MethodStructure> successMethods = rule.getSuccessMethods();
            Collections.sort(successMethods);
            for (MethodStructure methodStructure : successMethods) {
                methodStructure.getMethod().invoke(rule.getRule());
            }

        } else {

            List<MethodStructure> failMethods = rule.getFailMethods();
            Collections.sort(failMethods);
            for (MethodStructure methodStructure : failMethods) {
                methodStructure.getMethod().invoke(rule.getRule());
            }
        }

    }

    /* setters */

    public void setSatisfyCondition(Integer satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }


    public void setStopOnFail(Boolean stopOnFail) {
        this.stopOnFail = stopOnFail;
    }
}
