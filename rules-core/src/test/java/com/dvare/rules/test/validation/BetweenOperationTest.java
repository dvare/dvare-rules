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


package com.dvare.rules.test.validation;

import com.dvare.binding.rule.RuleBinding;
import com.dvare.evaluator.RuleEvaluator;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;
import com.dvare.rules.test.validation.dataobjects.BetweenOperation;
import com.dvare.spring.config.RuleConfiguration;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BetweenOperationTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 between [2,4]" +
                " And Variable2 between [3.1,3.3]" +
                " And Variable3 between [12-05-2016,15-06-2016] ";

        Expression expression = factory.getParser().fromString(exp, BetweenOperation.class);
        RuleBinding rule = new RuleBinding(expression);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        BetweenOperation betweenOperation = new BetweenOperation();
        betweenOperation.setVariable1(2);
        betweenOperation.setVariable2(3.2f);
        betweenOperation.setVariable3(dateFormat.parse("28-05-2016"));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = evaluator.evaluate(rule, betweenOperation);
        assertTrue(result);
    }

}
