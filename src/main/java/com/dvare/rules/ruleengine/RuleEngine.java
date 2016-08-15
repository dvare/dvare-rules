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


package com.dvare.rules.ruleengine;

import com.dvare.rules.annotations.*;
import com.dvare.rules.exceptions.ConditionNotFoundException;
import com.dvare.rules.exceptions.ConditionParamNotFoundException;
import com.dvare.rules.exceptions.IllegalRuleException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    private DVAREEngine dvareEngine;
    private List<Object> rules = new ArrayList<>();


    public RuleEngine(DVAREEngine dvareEngine) {
        this.dvareEngine = dvareEngine;
    }

    public int registerRule(Object rule) throws IllegalRuleException {

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


                } else if (conditionType.equals(ConditionType.DVARE)) {


                    if (method.getParameters() != null && method.getParameters().length > 0) {

                        Parameter parameter = method.getParameters()[0];
                        if (!parameter.getType().equals(DVAREEngine.class)) {
                            throw new ConditionParamNotFoundException("Method Condition Type DVARE not contain DVAREEngine param");
                        }

                    } else {
                        throw new ConditionParamNotFoundException("Method Condition Type DVARE not contain DVAREEngine param ");
                    }


                }
                conditionFound = true;
            }

        }


        if (!conditionFound) {
            throw new ConditionNotFoundException("Passed Rule not contain any  Method  annotated with @Condition");
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


                    for (Method method : conditionMethods) {

                        try {
                            Object result = null;
                            Condition condition = (Condition) method.getAnnotation(Condition.class);
                            ConditionType conditionType = condition.type();
                            if (conditionType.equals(ConditionType.CODE)) {
                                result = method.invoke(rule, null);
                            } else if (conditionType.equals(ConditionType.DVARE)) {
                                result = method.invoke(rule, dvareEngine);
                            }

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
