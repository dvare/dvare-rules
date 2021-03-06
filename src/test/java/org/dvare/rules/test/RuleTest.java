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


package org.dvare.rules.test;

import org.dvare.api.RuleEngineBuilder;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.rules.test.model.Person;
import org.dvare.rules.test.rule.BasicRule;
import org.dvare.rules.test.rule.DvareRule;
import org.junit.Assert;
import org.junit.Test;

public class RuleTest {
    @Test
    public void ageTest() throws IllegalRuleException {

        RuleEngine ruleEngine = new RuleEngineBuilder().build();

        BasicRule basicRule = new BasicRule();
        ruleEngine.registerRule(basicRule);


        Person person = new Person();
        person.setAge(25);


        DvareRule dvareRule = new DvareRule();
        dvareRule.setRule("age between [ 20 , 30 ]");
        dvareRule.setPerson(person);
        ruleEngine.registerRule(dvareRule);

        ruleEngine.fireRules(null);

        Assert.assertTrue(ruleEngine.getResult(dvareRule).getResult());

    }
}
