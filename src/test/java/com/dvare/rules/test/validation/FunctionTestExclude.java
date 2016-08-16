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
import com.dvare.config.RuleConfiguration;
import com.dvare.evaluator.RuleEvaluator;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import com.dvare.rules.test.validation.dataobjects.Function;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FunctionTestExclude extends TestCase {
    static Logger logger = LoggerFactory.getLogger(FunctionTestExclude.class);

    @Test
    public void testApp()  throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"com.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addFiveFunction , Variable2, Variable3 )", Function.class);
        Rule rule = new Rule(expression);

        Function function=new Function();
        function.setVariable1(15);
        function.setVariable2(10);
        function.setVariable3("test 2nd argument");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = evaluator.evaluate(rule, function);
        assertTrue(result);
    }


  /*  @Test
    public void testApp2()  throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"com.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 in fun ( testFun2 , Variable2 )", Function.class);
        Rule rule = new Rule(expression);

        Function function=new Function();
        function.setVariable1(5);
        function.setVariable2(9);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = evaluator.evaluate(rule, function);
        assertTrue(result);
    }
*/


}
