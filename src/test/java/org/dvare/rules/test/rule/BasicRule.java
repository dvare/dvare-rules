package org.dvare.rules.test.rule;


import org.apache.log4j.Logger;

public class BasicRule implements org.dvare.api.BasicRule {

    private static Logger logger = Logger.getLogger(BasicRule.class);

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
