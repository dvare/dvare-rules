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
import com.dvare.rules.test.validation.dataobjects.Parenthesis;
import com.dvare.spring.config.RuleConfiguration;
import junit.framework.TestCase;
import org.junit.Test;

public class ParenthesisTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = 'A'" +
                " AND ( ( Variable2 = 'O'" +
                " AND Variable3 = 'R' )" +
                " OR ( Variable4 = 'G'" +
                " AND Variable5 = 'M' ) )";
        Expression expression = factory.getParser().fromString(expr, Parenthesis.class);

        RuleBinding rule = new RuleBinding(expression);

        Parenthesis parenthesis = new Parenthesis();
        parenthesis.setVariable1("A");
        parenthesis.setVariable2("O");
        parenthesis.setVariable3("R");
        parenthesis.setVariable4("K");
        parenthesis.setVariable5("M");


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = evaluator.evaluate(rule, parenthesis);
        assertTrue(result);
    }

}
