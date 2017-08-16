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


package org.dvare.ruleengine.structure;

import java.util.ArrayList;
import java.util.List;

public class RuleStructure implements Comparable<RuleStructure> {
    private String ruleId;
    private Object rule;
    private Integer priority;
    private List<MethodStructure> conditions = new ArrayList<>();
    private MethodStructure aggregation;
    private List<MethodStructure> successMethods = new ArrayList<>();
    private List<MethodStructure> failMethods = new ArrayList<>();
    private List<MethodStructure> beforeMethods = new ArrayList<>();
    private List<MethodStructure> afterMethods = new ArrayList<>();


    @Override
    public int compareTo(RuleStructure other) {
        return this.priority.compareTo(other.priority);
    }


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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<MethodStructure> getConditions() {
        return conditions;
    }

    public void setConditions(List<MethodStructure> conditions) {
        this.conditions = conditions;
    }

    public MethodStructure getAggregation() {
        return aggregation;
    }

    public void setAggregation(MethodStructure aggregation) {
        this.aggregation = aggregation;
    }

    public List<MethodStructure> getSuccessMethods() {
        return successMethods;
    }

    public void setSuccessMethods(List<MethodStructure> successMethods) {
        this.successMethods = successMethods;
    }

    public List<MethodStructure> getFailMethods() {
        return failMethods;
    }

    public void setFailMethods(List<MethodStructure> failMethods) {
        this.failMethods = failMethods;
    }

    public List<MethodStructure> getBeforeMethods() {
        return beforeMethods;
    }

    public void setBeforeMethods(List<MethodStructure> beforeMethods) {
        this.beforeMethods = beforeMethods;
    }

    public List<MethodStructure> getAfterMethods() {
        return afterMethods;
    }

    public void setAfterMethods(List<MethodStructure> afterMethods) {
        this.afterMethods = afterMethods;
    }
}
