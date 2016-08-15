package com.dvare.rules.test;

import com.dvare.binding.data.DataRow;
import com.dvare.binding.model.TypeBinding;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.rules.annotations.*;
import com.dvare.rules.ruleengine.DVAREEngine;

@Rule(name = "textrule", priority = 0)
public class TextRuleTest {

    private String rule;
    private Integer age;

    @Condition(type = ConditionType.DVARE)
    public boolean condition(DVAREEngine dvareEngine) {


        TypeBinding typeBinding = new TypeBinding();
        typeBinding.addTypes("age", "IntegerType");

        DataRow dataRow = new DataRow();
        dataRow.addData("age", age);

        boolean result = false;

        try {
            result = dvareEngine.evaluate(rule, typeBinding, dataRow);
        } catch (ExpressionParseException e) {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
