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
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggregationRuleEngine implements Cloneable {
    Logger logger = Logger.getLogger(AggregationRuleEngine.class);
    RuleConfiguration configuration;

    private List<RuleBinding> ruleBindings = new ArrayList<>();
    private ContextsBinding contextsBinding = new ContextsBinding();
    private InstancesBinding instancesBinding = new InstancesBinding();

    public AggregationRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected AggregationRuleEngine clone() throws CloneNotSupportedException {
        return (AggregationRuleEngine) super.clone();
    }

    public void register(File rulefile, ContextsBinding contextsBinding, InstancesBinding instancesBinding) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(rulefile));


            StringBuilder rule = new StringBuilder();
            String row;
            while ((row = br.readLine()) != null) {
                rule.append(row.trim());
                if (!row.trim().contains(";")) {
                    rule.append(" ");
                }
            }


            String tokens[] = rule.toString().split(";");
            List<String> rules = Arrays.asList(tokens);

            register(rules, contextsBinding, instancesBinding);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    public void register(List<String> rules, ContextsBinding contextsBinding, InstancesBinding instancesBinding) {
        try {

            this.contextsBinding = contextsBinding;
            this.instancesBinding = instancesBinding;


            register(rules, contextsBinding);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void register(List<String> rules, ContextsBinding contextsBinding) {
        try {

            ruleBindings = new ArrayList<>();
            for (String rule : rules) {
                Expression expression = configuration.getParser().fromString(rule, contextsBinding);
                RuleBinding ruleBinding = new RuleBinding(expression);
                ruleBinding.setRawExpression(rule);
                ruleBindings.add(ruleBinding);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    public InstancesBinding evaluate() throws InterpretException {
        InstancesBinding result = configuration.getEvaluator().aggregate(ruleBindings, instancesBinding);
        return result;
    }


}
