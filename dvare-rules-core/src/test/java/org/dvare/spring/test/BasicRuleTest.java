package org.dvare.spring.test;


import org.apache.log4j.Logger;
import org.dvare.rule.BasicRule;

public class BasicRuleTest implements BasicRule {

    Logger logger = Logger.getLogger(BasicRuleTest.class);

    @Override
    public String getName() {
        return "basicRule";
    }

    @Override
    public boolean condition() {
        return true;
    }

    @Override
    public void success() {
        logger.info("RuleSuccessfully Run");
    }

    @Override
    public void fail() {
        logger.error("Rule Failed");
    }


}
