package org.dvare.ruleengine;

import org.dvare.annotations.Fact;
import org.dvare.annotations.RuleEngineType;
import org.dvare.api.DefaultRuleEngine;
import org.dvare.api.Facts;
import org.dvare.api.ListenerStructure;
import org.dvare.api.RuleStructure;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListenerRuleEngine implements DefaultRuleEngine {

    protected Integer satisfyCondition = 0;
    protected Boolean stopOnFail = false;
    protected DvareRuleEngine textualRuleEngine;
    protected AggregationRuleEngine aggregationRuleEngine;


    /**
     * trigger condition methods
     *
     * @param afterMethods before Methods
     * @param rule         rule Instance
     */
    @Override
    public boolean triggerConditions(List<ListenerStructure> beforeMethods, List<ListenerStructure> conditions,
                                     List<ListenerStructure> afterMethods, Object rule, Facts facts)
            throws IllegalAccessException, InvocationTargetException {


        Collections.sort(conditions);

        List<Boolean> results = new ArrayList<>();


        for (ListenerStructure conditionStructure : conditions) {
            // before condition
            triggerBeforeListeners(beforeMethods, rule);

            //condition
            Object conditionResult;

            Method condition = conditionStructure.getListener();

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
            conditionResult = conditionStructure.getListener().invoke(rule, args);


            // after condition
            triggerAfterListeners(afterMethods, rule);


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
    @Override
    public void triggerBeforeListeners(List<ListenerStructure> beforeMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException {
        Collections.sort(beforeMethods);
        for (ListenerStructure methodStructure : beforeMethods) {
            methodStructure.getListener().invoke(rule);
        }
    }

    /**
     * trigger after method condition
     *
     * @param afterMethods before Methods
     * @param rule         rule Instance
     */
    @Override
    public void triggerAfterListeners(List<ListenerStructure> afterMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException {
        Collections.sort(afterMethods);
        for (ListenerStructure methodStructure : afterMethods) {
            methodStructure.getListener().invoke(rule);
        }
    }

    @Override

    public InstancesBinding triggerAggregation(ListenerStructure methodStructure, Object rule, Facts facts)
            throws IllegalAccessException, InvocationTargetException, InterpretException {
        if (methodStructure != null) {

            List<Object> params = new ArrayList<>();
            for (Parameter parameter : methodStructure.getListener().getParameters()) {
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

            Object instance = methodStructure.getListener().invoke(rule, args);

            if (instance instanceof InstancesBinding) {
                return (InstancesBinding) instance;
            }

        }
        return null;
    }


    @Override
    public void triggerListeners(RuleStructure rule, Boolean result)
            throws IllegalAccessException, InvocationTargetException {

        if (result) {
            List<ListenerStructure> successMethods = rule.getSuccessListeners();
            Collections.sort(successMethods);
            for (ListenerStructure methodStructure : successMethods) {
                methodStructure.getListener().invoke(rule.getRule());
            }

        } else {

            List<ListenerStructure> failMethods = rule.getFailListeners();
            Collections.sort(failMethods);
            for (ListenerStructure methodStructure : failMethods) {
                methodStructure.getListener().invoke(rule.getRule());
            }
        }

    }

}
