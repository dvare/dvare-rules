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


package org.dvare.spring.test;

import org.apache.log4j.Logger;
import org.dvare.annotations.*;
import org.dvare.ruleengine.AggregationRuleEngine;
import org.dvare.spring.test.model.Student;
import org.dvare.spring.test.model.StudentAggregation;

import java.io.File;
import java.util.List;

@Rule(name = "firstRule", priority = 0)
public class AggregationRuleTest {
    Logger logger = Logger.getLogger(AggregationRuleTest.class);
    private File aggregationRule;
    private List<Object> data;
    private StudentAggregation result = new StudentAggregation();


 /*   @Condition(type = ConditionType.TEXT)
    public void condition(TextualRuleEngine textualRuleEngine) {
        //  textualRuleEngine.evaluate(rule, Person.class, person);
    }*/

    @Aggregation
    public void aggregation(AggregationRuleEngine aggregationRuleEngine) {
        aggregationRuleEngine.register(aggregationRule, StudentAggregation.class, Student.class, result, data);
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

    public File getAggregationRule() {
        return aggregationRule;
    }

    public void setAggregationRule(File aggregationRule) {
        this.aggregationRule = aggregationRule;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public StudentAggregation getResult() {
        return result;
    }

    public void setResult(StudentAggregation result) {
        this.result = result;
    }
}
