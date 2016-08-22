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


import com.dvare.binding.rule.RuleBinding;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.expression.Expression;
import com.dvare.spring.config.RuleConfiguration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TextualRuleEngine {
    Logger logger = Logger.getLogger(TextualRuleEngine.class);
    RuleConfiguration configuration;

    public TextualRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

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

    public boolean evaluate(String rule, Class type, Object object) {
        try {
            Expression expression = configuration.getParser().fromString(rule, type);
            RuleBinding ruleExpression = new RuleBinding(expression);
            ruleExpression.setRawExpression(rule);
            return evaluate(ruleExpression, object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }


    public boolean evaluate(RuleBinding rule, Object object) throws InterpretException {
        boolean result = configuration.getEvaluator().evaluate(rule, object);
        return result;
    }


}
