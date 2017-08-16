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

import org.dvare.api.Facts;
import org.dvare.api.RuleEngineBuilder;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.ruleengine.structure.RuleResult;
import org.dvare.rules.test.model.Person;
import org.dvare.rules.test.model.Student;
import org.dvare.rules.test.model.StudentAggregation;
import org.dvare.rules.test.rule.AnnotatedDvareRule;
import org.dvare.rules.test.rule.AnnotatedRule;
import org.dvare.rules.test.rule.AnnotatedRuleFile;
import org.dvare.rules.test.rule.StudentAggregationRule;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedTest {
    @Test
    public void testApp() throws IllegalRuleException {

        Facts facts = new Facts();
        facts.add("age", 25);
        RuleEngine ruleEngine = new RuleEngineBuilder().facts(facts).build();

        AnnotatedRule ruleTest = new AnnotatedRule();

        ruleEngine.registerRule(ruleTest);


        ruleEngine.fireRules();

        boolean result = ruleEngine.getResult(ruleTest).getResult();
        Assert.assertTrue(result);

    }


    @Test
    public void testApp1() throws IllegalRuleException {


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
        Assert.assertTrue(result);

    }


    @Test
    public void testApp2() throws IllegalRuleException {

        Person female = new Person();
        female.setAge(25);
        female.setGender("Female");
        female.setTitle("Ms");

        Facts facts = new Facts();
        facts.add("rule", new File("src/test/resources/rules/age_rule.dvr"));
        facts.add("person", female);
        RuleEngine ruleEngine = new RuleEngineBuilder().facts(facts).build();

        AnnotatedRuleFile dvareRule = new AnnotatedRuleFile();

        ruleEngine.registerRule(dvareRule);
        String ruleId = ruleEngine.registerRule(dvareRule);
        ruleEngine.fireRules();

        boolean result = ruleEngine.getResult(ruleId).getResult();
        Assert.assertTrue(result);

    }


    @Test
    public void testApp3() throws IllegalRuleException {



        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setName("student1");
        student1.setAge(20);
        student1.setSgpa(2.4f);
        students.add(student1);

        Student student2 = new Student();
        student2.setName("student2");
        student2.setAge(25);
        student2.setSgpa(2.8f);
        students.add(student2);

        Student student3 = new Student();
        student3.setName("student3");
        student3.setAge(30);
        student3.setSgpa(3.4f);
        students.add(student3);


        Facts facts = new Facts();
        facts.add("rule", new File("src/test/resources/rules/student_aggregation_rule.dvr"));
        facts.add("students", students);

        RuleEngine ruleEngine = new RuleEngineBuilder().facts(facts).build();

        String ruleId = ruleEngine.registerRule(new StudentAggregationRule());


        ruleEngine.fireRules();

        RuleResult result = ruleEngine.getResult(ruleId);

        Assert.assertNotNull(result.getObject("result"));

        StudentAggregation aggregation = (StudentAggregation) result.getObject("result");
        Assert.assertEquals(aggregation.getCount(), 8);
        Assert.assertTrue(aggregation.getSgpa() == 2.8f);
        Assert.assertEquals(aggregation.getAge(), 25);

    }
}



