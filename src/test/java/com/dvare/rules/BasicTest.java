package com.dvare.rules;

import com.dvare.rules.exceptions.IllegalRuleException;
import com.dvare.rules.ruleengine.RuleEngine;
import com.dvare.rules.test.FileRuleTest;
import com.dvare.rules.test.RuleTest;
import com.dvare.rules.test.TextRuleTest;
import junit.framework.TestCase;
import org.junit.Test;

public class BasicTest extends TestCase {
    @Test
    public void testApp() throws IllegalRuleException {

        RuleEngine ruleEngine = new RuleEngine();

        RuleTest ruleTest = new RuleTest();
        ruleTest.setAge(25);
        ruleEngine.registerRule(ruleTest);

        TextRuleTest textRuleTest = new TextRuleTest();
        textRuleTest.setAge(25);
        ruleEngine.registerRule(textRuleTest);

        FileRuleTest fileRuleTest = new FileRuleTest();
        fileRuleTest.setAge(35);
        ruleEngine.registerRule(fileRuleTest);

        ruleEngine.fireRules();

    }
}
