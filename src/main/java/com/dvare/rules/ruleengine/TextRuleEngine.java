package com.dvare.rules.ruleengine;


import com.dvare.binding.data.DataRow;
import com.dvare.binding.model.TypeBinding;
import com.dvare.binding.rule.Rule;
import com.dvare.binding.validation.ValidationRuleResult;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextRuleEngine {

    RuleConfiguration configuration;

    public TextRuleEngine(RuleConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean evaluate(File rulefile, TypeBinding typeBinding, DataRow dataRow) throws IOException, ExpressionParseException {


        BufferedReader br = new BufferedReader(new FileReader(rulefile));

        StringBuilder rule = new StringBuilder();
        String row;

        while ((row = br.readLine()) != null) {
            rule.append(row.trim());
            rule.append(" ");
        }

        return evaluate(rule.toString(), typeBinding, dataRow);
    }

    public boolean evaluate(String rule, TypeBinding typeBinding, DataRow dataRow) throws ExpressionParseException {
        Expression expression = configuration.getParser().fromString(rule, typeBinding);
        Rule ruleExpression = new Rule(expression);
        ruleExpression.setRawExpression(rule);
        return evaluate(ruleExpression, dataRow);
    }


    public boolean evaluate(Rule rule, DataRow dataRow) {
        ValidationRuleResult result = configuration.getEvaluator().evaluate(rule, dataRow);
        return !result.isError();
    }


}
