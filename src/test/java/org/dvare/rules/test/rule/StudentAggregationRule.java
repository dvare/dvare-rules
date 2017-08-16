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
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.ruleengine.AggregationRuleEngine;
import org.dvare.rules.test.model.Student;
import org.dvare.rules.test.model.StudentAggregation;

import java.io.File;
import java.util.List;

@Rule(name = "Student Aggregation Rule")
public class StudentAggregationRule {
    private static Logger logger = Logger.getLogger(StudentAggregationRule.class);

    @Aggregation
    public Object aggregation(@Fact("rule") File rule, @Fact("students") List<Student> students,
                              @RuleEngineType AggregationRuleEngine aggregationRuleEngine) throws IllegalRuleException {

        ContextsBinding contextsBinding = new ContextsBinding();
        contextsBinding.addContext("result", StudentAggregation.class);
        contextsBinding.addContext("student", Student.class);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("result", new StudentAggregation());
        instancesBinding.addInstance("student", students);

        return aggregationRuleEngine.register(rule, contextsBinding, instancesBinding);
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


}
