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
import org.dvare.api.Facts;
import org.dvare.api.RuleResult;
import org.dvare.api.RuleStructure;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.parser.RuleParser;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The RuleEngine class contains method for rule registering and rule firing.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-08-20
 */
public class RuleEngine extends ListenerRuleEngine {
    private static Logger logger = Logger.getLogger(RuleEngine.class);


    private List<RuleResult> ruleResults;
    private Map<String, RuleStructure> rules = new HashMap<>();

    public RuleEngine(DvareRuleEngine textualRuleEngine, AggregationRuleEngine aggregationRuleEngine) {

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


        RuleStructure ruleStructure = RuleParser.parseRule(rule, rules.size());

        rules.put(ruleStructure.getId(), ruleStructure);

        return ruleStructure.getId();
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
     */
    public void fireRules(Facts facts) {
        ruleResults = new ArrayList<>();

        List<RuleStructure> ruleSet = new ArrayList<>(rules.values());
        Collections.sort(ruleSet);

        for (RuleStructure ruleStructure : ruleSet) {

            RuleResult ruleResult = triggerRule(ruleStructure, facts);
            ruleResults.add(ruleResult);
            if (stopOnFail && !ruleResult.getResult()) {
                break;
            }

        }


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
    private RuleResult triggerRule(RuleStructure ruleStructure, Facts facts) {

        RuleResult ruleResult = new RuleResult();
        ruleResult.setRuleId(ruleStructure.getId());
        ruleResult.setRule(ruleStructure.getRule());

        try {
            //trigger Condition
            boolean result = triggerConditions(ruleStructure.getBeforeListeners(),
                    ruleStructure.getConditions(), ruleStructure.getAfterListeners(), ruleStructure.getRule(), facts);


            ruleResult.setResult(result);

            //trigger success and fail Listener
            triggerListeners(ruleStructure, result);

            //trigger aggregation
            if (ruleStructure.getAggregation() != null) {
                InstancesBinding instancesBinding = triggerAggregation(ruleStructure.getAggregation(), ruleStructure.getRule(), facts);
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




    /* setters */

    public void setSatisfyCondition(Integer satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }


    public void setStopOnFail(Boolean stopOnFail) {
        this.stopOnFail = stopOnFail;
    }
}
