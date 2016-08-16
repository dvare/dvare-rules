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


package com.dvare.rules.test;


import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.rules.annotations.*;
import com.dvare.rules.ruleengine.DVAREEngine;
import org.apache.log4j.Logger;

@Rule(name = "textrule", priority = 0)
public class TextRuleTest {
    Logger logger = Logger.getLogger(TextRuleTest.class);

    private String rule;
    private Person person;

    @Condition(type = ConditionType.DVARE)
    public boolean condition(DVAREEngine dvareEngine) {


        boolean result = false;

        try {
            result = dvareEngine.evaluate(rule, Person.class, person);
        } catch (ExpressionParseException e) {
            e.printStackTrace();
        } catch (InterpretException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Before
    public void beforeAction() {
        logger.info("Before Rule Running");
    }

    @Success
    public void success() {
        logger.info("Rule Successfully Run");
    }

    @Fail
    public void fail() {
        logger.error("Rule Failed");
    }

    @After
    public void afterAction() {
        logger.info("After Rule Running");

    }


    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
