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


package org.dvare.rules.test;

import org.dvare.api.RuleEngineBuilder;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.ruleengine.structure.RuleResult;
import org.dvare.rules.test.model.Person;
import org.dvare.rules.test.model.Student;
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


        RuleEngine ruleEngine = new RuleEngineBuilder().build();

        AnnotatedRule ruleTest = new AnnotatedRule();
        ruleTest.setAge(25);
        ruleEngine.registerRule(ruleTest);


        ruleEngine.fireRules();

        boolean result = ruleEngine.getResult(ruleTest).getResult();
        Assert.assertTrue(result);

    }


    @Test
    public void testApp1() throws IllegalRuleException {


        RuleEngine ruleEngine = new RuleEngineBuilder().build();


        Person male = new Person();
        male.setAge(25);
        male.setGender("Male");
        male.setTitle("Mr");

        AnnotatedDvareRule dvareRule = new AnnotatedDvareRule();
        dvareRule.setRule("age between [ 20 , 30 ] And title = 'Mr' And gender = 'Male'");
        dvareRule.setPerson(male);

        ruleEngine.registerRule(dvareRule);

        ruleEngine.fireRules();

        boolean result = ruleEngine.getResult(dvareRule).getResult();
        Assert.assertTrue(result);

    }


    @Test
    public void testApp2() throws IllegalRuleException {


        RuleEngine ruleEngine = new RuleEngineBuilder().build();

        AnnotatedRule ruleTest = new AnnotatedRule();
        ruleTest.setAge(25);
        ruleEngine.registerRule(ruleTest);


        Person female = new Person();
        female.setAge(25);
        female.setGender("Female");
        female.setTitle("Ms");


        AnnotatedRuleFile dvareRule = new AnnotatedRuleFile();
        dvareRule.setRule(new File("src/test/resources/rules/age_rule.dvr"));
        dvareRule.setPerson(female);
        ruleEngine.registerRule(dvareRule);


        String ruleId = ruleEngine.registerRule(dvareRule);

        ruleEngine.fireRules();

        boolean result = ruleEngine.getResult(ruleId).getResult();
        Assert.assertTrue(result);

    }


    @Test
    public void testApp3() throws IllegalRuleException {


        RuleEngine ruleEngine = new RuleEngineBuilder().build();

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


        StudentAggregationRule fileRuleTest = new StudentAggregationRule();

        fileRuleTest.setAggregationRule(new File("src/test/resources/rules/student_aggregation_rule.dvr"));

        fileRuleTest.setStudentList(students);

        String ruleId = ruleEngine.registerRule(fileRuleTest);


        ruleEngine.fireRules();

        RuleResult result = ruleEngine.getResult(ruleId);
        System.out.println(result.getObject("result"));

    }
}



