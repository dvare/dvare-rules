package com.dvare.rules.ruleengine;

import com.dvare.rules.annotations.*;
import com.dvare.rules.exceptions.IllegalRuleException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    private List<Object> rules = new ArrayList<>();

    public int registerRule(Object rule) {

        if (rule == null) {
            return -1;
        }


        if (!rule.getClass().isAnnotationPresent(Rule.class)) {
            return -1;
        }

        rules.add(rule);

        return rules.size() - 1;
    }

    public void unregisterRule(Integer ruleIndex) throws IllegalRuleException {
        rules.remove(rules.get(ruleIndex));
    }


    public void fireRules() {

        for (Object rule : rules) {


            if (rule.getClass().isAnnotationPresent(Rule.class)) {

                Annotation annotation = rule.getClass().getAnnotation(Rule.class);
                if (annotation != null) {
                    Rule ruleDetail = (Rule) annotation;

                    List<Method> conditionMethods = new ArrayList<>();
                    List<Method> successMethods = new ArrayList<>();
                    List<Method> failMethods = new ArrayList<>();
                    List<Method> beforeMethods = new ArrayList<>();
                    List<Method> afterMethods = new ArrayList<>();

                    for (Method method : rule.getClass().getMethods()) {

                        if (method.isAnnotationPresent(Condition.class)) {
                            conditionMethods.add(method);
                        } else if (method.isAnnotationPresent(Success.class)) {
                            successMethods.add(method);
                        } else if (method.isAnnotationPresent(Fail.class)) {
                            failMethods.add(method);
                        } else if (method.isAnnotationPresent(Before.class)) {
                            beforeMethods.add(method);
                        } else if (method.isAnnotationPresent(After.class)) {
                            afterMethods.add(method);
                        }

                    }


                    for (Method condition : conditionMethods) {

                        try {
                            Object result = condition.invoke(rule, null);
                            if (result != null && ((Boolean) result)) {

                                for (Method before : beforeMethods) {
                                    before.invoke(rule, null);
                                }
                                for (Method success : successMethods) {
                                    success.invoke(rule, null);
                                }
                                for (Method after : afterMethods) {
                                    after.invoke(rule, null);
                                }

                            } else {
                                for (Method before : beforeMethods) {
                                    before.invoke(rule, null);
                                }
                                for (Method fail : failMethods) {
                                    fail.invoke(rule, null);
                                }
                                for (Method after : afterMethods) {
                                    after.invoke(rule, null);
                                }
                            }

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }


                    }


                }
            }


        }
    }

}
