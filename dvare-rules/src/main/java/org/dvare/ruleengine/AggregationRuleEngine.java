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
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;

import java.io.File;
import java.io.FileReader;

public class AggregationRuleEngine implements Cloneable {
    private Logger logger = Logger.getLogger(AggregationRuleEngine.class);
    RuleConfiguration configuration;

    private RuleBinding ruleBinding;
    private ContextsBinding contextsBinding;
    private InstancesBinding instancesBinding;

    public AggregationRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected AggregationRuleEngine clone() throws CloneNotSupportedException {
        return (AggregationRuleEngine) super.clone();
    }

    public void register(File rule, ContextsBinding contextsBinding, InstancesBinding instancesBinding) throws IllegalRuleException {


        if (!rule.exists()) {
            throw new IllegalRuleException("Rule File " + rule.getName() + " not exists.");
        }

        try {

            FileReader fileReader = new FileReader(rule);
            StringBuilder ruleBuilder = new StringBuilder();
            int c;
            while ((c = fileReader.read()) != -1) {
                ruleBuilder.append((char) c);
            }

            register(ruleBuilder.toString(), contextsBinding, instancesBinding);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    public void register(String rule, ContextsBinding contextsBinding, InstancesBinding instancesBinding) {
        try {

            this.contextsBinding = contextsBinding;
            this.instancesBinding = instancesBinding;


            register(rule, contextsBinding);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void register(String rule, ContextsBinding contextsBinding) {
        try {
            Expression expression = configuration.getParser().fromString(rule, contextsBinding);
            ruleBinding = new RuleBinding(expression);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    public InstancesBinding evaluate() throws InterpretException {
        configuration.getEvaluator().aggregate(ruleBinding, instancesBinding);
        return instancesBinding;
    }


}
