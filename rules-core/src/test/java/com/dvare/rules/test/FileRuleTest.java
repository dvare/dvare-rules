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


import com.dvare.annotations.*;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.ruleengine.TextualRuleEngine;

import java.io.File;
import java.io.IOException;

@Rule(name = "fileRule", priority = 0)
public class FileRuleTest {

    private File rule;
    private Person person;

    @Condition(type = ConditionType.TEXT)
    public boolean condition(TextualRuleEngine textualRuleEngine) {


        boolean result = false;


        try {
            result = textualRuleEngine.evaluate(rule, Person.class, person);
        } catch (ExpressionParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterpretException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Before
    public void beforeAction() {
        System.out.println("Before Rule");
    }

    @Success
    public void success() {
        System.out.println("Rule Successfully Run");
    }

    @Fail
    public void fail() {
        System.out.println("Rule Failed");
    }

    @After
    public void afterAction() {
        System.out.println("After Rule");
    }


    public File getRule() {
        return rule;
    }

    public void setRule(File rule) {
        this.rule = rule;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
