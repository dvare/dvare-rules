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
import org.dvare.annotations.RuleEngineType;
import org.dvare.ruleengine.DvareRuleEngine;
import org.dvare.rules.test.model.Person;


public class DvareRule implements org.dvare.rule.DvareRule {
    private static Logger logger = Logger.getLogger(DvareRule.class);

    private String rule;
    private Person person;

    @Override
    public String getName() {
        return "Dvare Rule";
    }


    @Override
    public boolean condition(@RuleEngineType DvareRuleEngine dvareRuleEngine) throws Exception {
        return dvareRuleEngine.register(rule, Person.class, person);

    }

    @Override
    public void success() {
        logger.info("Rule Successfully Run");
    }

    @Override
    public void fail() {
        logger.error("Rule Failed");
    }


    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
