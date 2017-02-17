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


package org.dvare.ruleengine;


import org.apache.log4j.Logger;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TextualRuleEngine implements Cloneable {
    Logger logger = Logger.getLogger(TextualRuleEngine.class);
    RuleConfiguration configuration;


    private RuleBinding ruleBinding;
    private InstancesBinding instancesBinding = new InstancesBinding();
    private ContextsBinding contextsBinding = new ContextsBinding();

    public TextualRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected TextualRuleEngine clone() throws CloneNotSupportedException {
        return (TextualRuleEngine) super.clone();
    }

    @Deprecated
    public boolean evaluate(File rulefile, Class type, Object object) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(rulefile));

            StringBuilder rule = new StringBuilder();
            String row;

            while ((row = br.readLine()) != null) {
                rule.append(row.trim());
                rule.append(" ");
            }

            return evaluate(rule.toString(), type, object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Deprecated
    public boolean evaluate(String rule, TypeBinding typeBinding, Object object) {
        try {
            Expression expression = configuration.getParser().fromString(rule, typeBinding);
            RuleBinding ruleExpression = new RuleBinding(expression);
            ruleExpression.setRawExpression(rule);
            return evaluate(ruleExpression, object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Deprecated
    public boolean evaluate(String rule, Class type, Object object) {
        TypeBinding typeBinding = ExpressionParser.translate(type);
        return evaluate(rule, typeBinding, object);
    }


    @Deprecated
    public boolean evaluate(RuleBinding rule, Object object) throws InterpretException {
        Object result = configuration.getEvaluator().evaluate(rule, object);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }


    public void register(File rulefile, Class type, Object object) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(rulefile));

            StringBuilder rule = new StringBuilder();
            String row;

            while ((row = br.readLine()) != null) {
                rule.append(row.trim());
                rule.append(" ");
            }

            register(rule.toString(), type, object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    public void register(String rule, Class type, Object data) {


        TypeBinding typeBinding = ExpressionParser.translate(type);
        contextsBinding.addContext("self", typeBinding);

        instancesBinding.addInstance("self", data);


        register(rule, contextsBinding);

    }

    private void register(String rule, ContextsBinding typeBinding) {
        try {


            Expression expression = configuration.getParser().fromString(rule, typeBinding);
            ruleBinding = new RuleBinding(expression);
            ruleBinding.setRawExpression(rule);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    public boolean evaluate() throws InterpretException {
        Object result = configuration.getEvaluator().evaluate(ruleBinding, instancesBinding);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }

}
