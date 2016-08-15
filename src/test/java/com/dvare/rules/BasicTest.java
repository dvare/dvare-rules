package com.dvare.rules;

import com.dvare.config.RuleConfiguration;
import com.dvare.rules.exceptions.IllegalRuleException;
import com.dvare.rules.ruleengine.DVAREEngine;
import com.dvare.rules.ruleengine.RuleEngine;
import com.dvare.rules.test.FileRuleTest;
import com.dvare.rules.test.RuleTest;
import com.dvare.rules.test.TextRuleTest;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class BasicTest extends TestCase {
    @Test
    public void testApp() throws IllegalRuleException {

        RuleConfiguration configuration = new RuleConfiguration();
        DVAREEngine dvareEngine = new DVAREEngine(configuration);
        RuleEngine ruleEngine = new RuleEngine(dvareEngine);

        RuleTest ruleTest = new RuleTest();
        ruleTest.setAge(25);
        ruleEngine.registerRule(ruleTest);

        TextRuleTest textRuleTest = new TextRuleTest();
        textRuleTest.setAge(25);
        textRuleTest.setRule("age > 20 And age < 30");
        ruleEngine.registerRule(textRuleTest);

        FileRuleTest fileRuleTest = new FileRuleTest();
        fileRuleTest.setAge(35);
        URL url = this.getClass().getClassLoader().getResource("rules/age_rule.dvr");
        fileRuleTest.setRule(new File(url.getFile()));
        ruleEngine.registerRule(fileRuleTest);

        ruleEngine.fireRules();

    }
}
