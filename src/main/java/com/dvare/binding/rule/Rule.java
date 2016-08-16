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


package com.dvare.binding.rule;


import com.dvare.action.ActionDispatcher;
import com.dvare.action.NullActionDispatcher;
import com.dvare.expression.Expression;

public class Rule implements Comparable<com.dvare.binding.rule.Rule> {
    private String id;
    private int priority = 0;
    private String rawExpression;
    private String rawAggreagte;
    private Expression expression;
    private Expression aggreagte;
    private ActionDispatcher dispatcher = new NullActionDispatcher();

    public Rule(Expression expression) {
        this.expression = expression;
    }

    public Rule(Expression expression, Expression aggreagte) {
        this.expression = expression;
        this.aggreagte = aggreagte;
    }


    public Rule(Expression expression, Expression aggreagte, ActionDispatcher dispatcher) {
        this.expression = expression;
        this.aggreagte = aggreagte;
        this.dispatcher = dispatcher;

    }

    public Rule(Expression expression, ActionDispatcher dispatcher) {
        this.expression = expression;
        this.dispatcher = dispatcher;

    }

    @Override
    public int compareTo(com.dvare.binding.rule.Rule other) {
        return Integer.compare(this.priority, other.priority);
    }

        /* Getter and Setter */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public ActionDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(ActionDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Expression getAggreagte() {
        return aggreagte;
    }

    public void setAggreagte(Expression aggreagte) {
        this.aggreagte = aggreagte;
    }

    public String getRawExpression() {
        return rawExpression;
    }

    public void setRawExpression(String rawExpression) {
        this.rawExpression = rawExpression;
    }

    public String getRawAggreagte() {
        return rawAggreagte;
    }

    public void setRawAggreagte(String rawAggreagte) {
        this.rawAggreagte = rawAggreagte;
    }


}
