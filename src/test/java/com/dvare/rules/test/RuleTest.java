package com.dvare.rules.test;

import com.dvare.rules.annotations.*;

@Rule(name = "rule", priority = 0)
public class RuleTest {

    private Integer age;


    @Condition(type = ConditionType.CODE)
    public Boolean condition() {

        return age > 20 && age < 30;
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
