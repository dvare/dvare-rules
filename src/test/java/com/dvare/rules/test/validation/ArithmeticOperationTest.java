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

import com.dvare.binding.rule.Rule;
import com.dvare.binding.rule.RuleRegistry;
import com.dvare.config.RuleConfiguration;
import com.dvare.evaluator.RuleEvaluator;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;
import com.dvare.rules.test.validation.dataobjects.ArithmeticOperation;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ArithmeticOperationTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException , InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 = (7 + 3)" +
                " And Variable1 <> ( 30 - 10)" +
                " And Variable2 = (4 * 5)" +
                " And Variable1 = ( Variable2 / 2 )" +
                " And Variable1 = ( Variable1 min Variable2 )" +
                " And Variable2 = ( Variable1 max Variable2 )";

        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        Rule rule = new Rule(expression);


        ArithmeticOperation arithmeticOperation=new ArithmeticOperation();
        arithmeticOperation.setVariable1(10);
        arithmeticOperation.setVariable2(20);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = evaluator.evaluate(rule, arithmeticOperation);
        assertTrue(result);
    }

    public void testApp2() throws ExpressionParseException , InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable3 = (5.0 + 5.0)" +
                " And Variable3 = ( 20.0 - 10.0)" +
                " And Variable4 = (4.0 * 5.0)" +
                " And Variable3 = (Variable4 / 2.0 )" +
                " And Variable3 = ( Variable3 min Variable4 )" +
                " And Variable4 = ( Variable3 max Variable4 )";

        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        Rule rule = new Rule(expression);

        ArithmeticOperation arithmeticOperation=new ArithmeticOperation();
        arithmeticOperation.setVariable3(10.0f);
        arithmeticOperation.setVariable4(20.0f);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = evaluator.evaluate(rule, arithmeticOperation);
        assertTrue(result);
    }


}
