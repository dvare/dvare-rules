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


package org.dvare.spring;

import junit.framework.TestCase;
import org.dvare.api.RuleEngineBuilder;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.RuleEngine;
import org.dvare.spring.test.AggregationRuleTest;
import org.dvare.spring.test.AnnotatedFileRuleTest;
import org.dvare.spring.test.AnnotatedRuleTest;
import org.dvare.spring.test.AnnotatedTextualRuleTest;
import org.dvare.spring.test.model.Person;
import org.dvare.spring.test.model.Student;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedTest extends TestCase {
    @Test
    public void testApp() throws IllegalRuleException {


        RuleEngine ruleEngine = new RuleEngineBuilder().build();

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


    @Test
    public void testApp2() throws IllegalRuleException {


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


        AggregationRuleTest fileRuleTest = new AggregationRuleTest();
        URL url = this.getClass().getClassLoader().getResource("rules/student_aggregation_rule.dvr");
        fileRuleTest.setAggregationRule(new File(url.getFile()));
        fileRuleTest.setStudentList(students);

        String ruleId = ruleEngine.registerRule(fileRuleTest);


        ruleEngine.fireRules();

        Object result = ruleEngine.getResult(ruleId);
        System.out.println(result);

    }
}
