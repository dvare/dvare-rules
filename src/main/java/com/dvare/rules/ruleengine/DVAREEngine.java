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


import com.dvare.binding.data.DataRow;
import com.dvare.binding.model.TypeBinding;
import com.dvare.binding.rule.Rule;
import com.dvare.binding.validation.ValidationRuleResult;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DVAREEngine {

    RuleConfiguration configuration;

    public DVAREEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean evaluate(File rulefile, TypeBinding typeBinding, DataRow dataRow) throws IOException, ExpressionParseException {


        BufferedReader br = new BufferedReader(new FileReader(rulefile));

        StringBuilder rule = new StringBuilder();
        String row;

        while ((row = br.readLine()) != null) {
            rule.append(row.trim());
            rule.append(" ");
        }

        return evaluate(rule.toString(), typeBinding, dataRow);
    }

    public boolean evaluate(String rule, TypeBinding typeBinding, DataRow dataRow) throws ExpressionParseException {
        Expression expression = configuration.getParser().fromString(rule, typeBinding);
        Rule ruleExpression = new Rule(expression);
        ruleExpression.setRawExpression(rule);
        return evaluate(ruleExpression, dataRow);
    }


    public boolean evaluate(Rule rule, DataRow dataRow) {
        ValidationRuleResult result = configuration.getEvaluator().evaluate(rule, dataRow);
        return !result.isError();
    }


}
