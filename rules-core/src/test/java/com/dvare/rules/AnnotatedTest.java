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


package com.dvare.rules;

import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.rule.IllegalRuleException;
import com.dvare.ruleengine.RuleEngine;
import com.dvare.ruleengine.TextualRuleEngine;
import com.dvare.rules.test.AnnotatedFileRuleTest;
import com.dvare.rules.test.AnnotatedRuleTest;
import com.dvare.rules.test.AnnotatedTextualRuleTest;
import com.dvare.rules.test.Person;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class AnnotatedTest extends TestCase {
    @Test
    public void testApp() throws IllegalRuleException {

        RuleConfiguration configuration = new RuleConfiguration();
        TextualRuleEngine textualRuleEngine = new TextualRuleEngine(configuration);
        RuleEngine ruleEngine = new RuleEngine(textualRuleEngine);

        AnnotatedRuleTest ruleTest = new AnnotatedRuleTest();
        ruleTest.setAge(25);
        ruleEngine.registerRule(ruleTest);


        Person male = new Person();
        male.setAge(25);
        male.setGender("Male");
        male.setTitle("Mr");

        AnnotatedTextualRuleTest textRuleTest = new AnnotatedTextualRuleTest();
        textRuleTest.setRule("age between [ 20 , 30 ] And title = 'Mr' And gender = 'Male'");
        textRuleTest.setPerson(male);

        ruleEngine.registerRule(textRuleTest);


        Person female = new Person();
        female.setAge(25);
        female.setGender("Female");
        female.setTitle("Ms");


        AnnotatedFileRuleTest fileRuleTest = new AnnotatedFileRuleTest();
        URL url = this.getClass().getClassLoader().getResource("rules/age_rule.dvr");
        fileRuleTest.setRule(new File(url.getFile()));
        fileRuleTest.setPerson(female);
        ruleEngine.registerRule(fileRuleTest);

        ruleEngine.fireRules();


    }
}
