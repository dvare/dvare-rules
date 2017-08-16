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
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DvareRuleEngine {

    private static Logger logger = Logger.getLogger(DvareRuleEngine.class);
    private RuleConfiguration configuration;
    private RuleBinding ruleBinding;
    private InstancesBinding instancesBinding = new InstancesBinding();
    private ContextsBinding contextsBinding = new ContextsBinding();

    public DvareRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean register(File rule, Class type, Object object) throws IllegalRuleException, InterpretException, IOException {

        if (!rule.exists()) {
            throw new IllegalRuleException("Rule File " + rule.getName() + " not exists.");
        }


        BufferedReader br = new BufferedReader(new FileReader(rule));

        StringBuilder ruleBuilder = new StringBuilder();
        String row;

        while ((row = br.readLine()) != null) {
            ruleBuilder.append(row.trim());
            ruleBuilder.append(" ");
        }

        return register(ruleBuilder.toString(), type, object);

    }


    public boolean register(String rule, Class type, Object data) throws InterpretException {
        contextsBinding.addContext("self", type);
        instancesBinding.addInstance("self", data);
        return registerRule(rule, contextsBinding);
    }

    private boolean registerRule(String rule, ContextsBinding typeBinding) throws InterpretException {
        try {


            Expression expression = configuration.getParser().fromString(rule, typeBinding);
            ruleBinding = new RuleBinding(expression);
            ruleBinding.setRawExpression(rule);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return evaluate();
    }


    public boolean evaluate() throws InterpretException {
        Object result = configuration.getEvaluator().evaluate(ruleBinding, instancesBinding);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }

}
