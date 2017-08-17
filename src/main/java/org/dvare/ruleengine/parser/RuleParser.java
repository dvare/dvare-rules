package org.dvare.ruleengine.parser;

import org.dvare.annotations.Rule;
import org.dvare.api.BasicRule;
import org.dvare.api.RuleStructure;
import org.dvare.exceptions.IllegalRuleException;
import org.dvare.rule.DvareRule;

public class RuleParser {

    public static RuleStructure parseRule(Object rule, int size) throws IllegalRuleException {

        if (rule.getClass().isAnnotationPresent(Rule.class)) {

            AnnotatedRuleParser.validateRule(rule);

            return AnnotatedRuleParser.parseRule(rule, size);

        } else if (rule instanceof BasicRule || rule instanceof DvareRule) {

            return ObjectRuleParser.parseRule(rule, size);

        } else {
            throw new IllegalRuleException("Passed Rule Must be Annotated with @Rule or implement BasicRule or TextualRule");
        }


    }

}
