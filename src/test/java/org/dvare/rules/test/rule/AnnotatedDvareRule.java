/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


package org.dvare.rules.test.rule;


import org.apache.log4j.Logger;
import org.dvare.annotations.*;
import org.dvare.api.Facts;
import org.dvare.api.RuleEngineBuilder;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.ruleengine.DvareRuleEngine;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.rules.test.model.Person;

@Rule(name = "Annotated Dvare Rule")
public class AnnotatedDvareRule {
    private static Logger logger = Logger.getLogger(AnnotatedDvareRule.class);

    public static void main(String args[]) throws IllegalRuleException {

        Person male = new Person();
        male.setAge(25);
        male.setGender("Male");
        male.setTitle("Mr");

        Facts facts = new Facts();
        facts.add("rule", "age between [ 20 , 30 ] And title = 'Mr' And gender = 'Male'");
        facts.add("person", male);
        RuleEngine ruleEngine = new RuleEngineBuilder().facts(facts).build();

        AnnotatedDvareRule dvareRule = new AnnotatedDvareRule();

        ruleEngine.registerRule(dvareRule);
        ruleEngine.fireRules();
        boolean result = ruleEngine.getResult(dvareRule).getResult();
        System.out.println(result);
    }


    @Before
    public void beforeCondition() {
        logger.info("Before Condition ");
    }

    @After
    public void afterCondition() {
        logger.info("After Condition ");

    }

    @Success
    public void success() {
        logger.info("Rule Successfully Run");
    }

    @Fail
    public void fail() {
        logger.error("Rule Failed");
    }

    @Condition
    public boolean condition(@Fact("rule") String rule, @Fact("person")
            Person person, @RuleEngineType DvareRuleEngine textualRuleEngine) throws InterpretException {
        return textualRuleEngine.register(rule, Person.class, person);
    }
}
