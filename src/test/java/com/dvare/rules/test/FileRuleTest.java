package com.dvare.rules.test;

import com.dvare.binding.data.DataRow;
import com.dvare.binding.model.TypeBinding;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.rules.annotations.*;
import com.dvare.rules.ruleengine.TextRuleEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Rule(name = "fileRule", priority = 0)
public class FileRuleTest {

    private Integer age;

    @Condition
    public boolean condition() {
        RuleConfiguration configuration = new RuleConfiguration();
        TextRuleEngine textRuleEngine = new TextRuleEngine(configuration);

        TypeBinding typeBinding = new TypeBinding();
        typeBinding.addTypes("age", "IntegerType");

        DataRow dataRow = new DataRow();
        dataRow.addData("age", age);

        boolean result = false;

        URL url = this.getClass().getClassLoader().getResource("rules/age_rule.dvare");
        File rule = new File(url.getFile());

        try {
            result = textRuleEngine.evaluate(rule, typeBinding, dataRow);
        } catch (ExpressionParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
}
