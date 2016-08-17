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


package com.dvare.ruleengine;


import com.dvare.binding.rule.Rule;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
// condition order
// before and after for condition
// rule status by number
// rule priority
// getall rule instance by name
public class TextualRuleEngine {

    RuleConfiguration configuration;

    public TextualRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean evaluate(File rulefile, Class type, Object object) throws IOException, ExpressionParseException, InterpretException {


        BufferedReader br = new BufferedReader(new FileReader(rulefile));

        StringBuilder rule = new StringBuilder();
        String row;

        while ((row = br.readLine()) != null) {
            rule.append(row.trim());
            rule.append(" ");
        }

        return evaluate(rule.toString(), type, object);
    }

    public boolean evaluate(String rule, Class type, Object object) throws ExpressionParseException, InterpretException {
        Expression expression = configuration.getParser().fromString(rule, type);
        Rule ruleExpression = new Rule(expression);
        ruleExpression.setRawExpression(rule);
        return evaluate(ruleExpression, object);
    }


    public boolean evaluate(Rule rule, Object object) throws InterpretException {
        boolean result = configuration.getEvaluator().evaluate(rule, object);
        return result;
    }


}
