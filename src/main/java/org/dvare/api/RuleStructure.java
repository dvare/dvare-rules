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


package org.dvare.api;

import java.util.ArrayList;
import java.util.List;

public class RuleStructure implements Comparable<RuleStructure> {
    private String id;
    private Object rule;
    private int priority;
    private List<ListenerStructure> beforeListeners = new ArrayList<>();
    private List<ListenerStructure> afterListeners = new ArrayList<>();
    private List<ListenerStructure> conditions = new ArrayList<>();
    private ListenerStructure aggregation;
    private List<ListenerStructure> successListeners = new ArrayList<>();
    private List<ListenerStructure> failListeners = new ArrayList<>();



    @Override
    public int compareTo(RuleStructure other) {
        return Integer.compare(this.priority, other.priority);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getRule() {
        return rule;
    }

    public void setRule(Object rule) {
        this.rule = rule;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<ListenerStructure> getBeforeListeners() {
        return beforeListeners;
    }

    public void setBeforeListeners(List<ListenerStructure> beforeListeners) {
        this.beforeListeners = beforeListeners;
    }

    public List<ListenerStructure> getAfterListeners() {
        return afterListeners;
    }

    public void setAfterListeners(List<ListenerStructure> afterListeners) {
        this.afterListeners = afterListeners;
    }

    public List<ListenerStructure> getConditions() {
        return conditions;
    }

    public void setConditions(List<ListenerStructure> conditions) {
        this.conditions = conditions;
    }

    public ListenerStructure getAggregation() {
        return aggregation;
    }

    public void setAggregation(ListenerStructure aggregation) {
        this.aggregation = aggregation;
    }

    public List<ListenerStructure> getSuccessListeners() {
        return successListeners;
    }

    public void setSuccessListeners(List<ListenerStructure> successListeners) {
        this.successListeners = successListeners;
    }

    public List<ListenerStructure> getFailListeners() {
        return failListeners;
    }

    public void setFailListeners(List<ListenerStructure> failListeners) {
        this.failListeners = failListeners;
    }
}
