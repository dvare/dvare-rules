/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

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


package org.dvare.rest.service;


import org.apache.log4j.Logger;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.json.JSONObject;

import java.util.Iterator;


public class RuleBuilder {

    Logger logger = Logger.getLogger(RuleBuilder.class);


    public String buildRule(String ruleEngine, String ruleString, RuleEngineRegistry registry) {

        if (ruleString != null && !ruleString.isEmpty()) {
            JSONObject jsonObject = new JSONObject(ruleString);

            String ruleName = jsonObject.getString("ruleName");
            Integer rulePriority = jsonObject.getInt("priority");
            String rule = jsonObject.getString("rule");

            if (jsonObject.has("model")) {
                JSONObject values = jsonObject.getJSONObject("model");
                TypeBinding typeBinding = null;
                if (jsonObject.has("modelTypes")) {
                    JSONObject modelTypes = jsonObject.getJSONObject("modelTypes");
                    typeBinding = buildModel(modelTypes);
                } else {
                    typeBinding = buildModelFromValues(values);
                }
                DataRow model = buildValues(values, typeBinding);
                RestRuleEngine restRuleEngine = registry.getRuleEngine(ruleEngine);
                String ruleId = restRuleEngine.registerRule(ruleName, rulePriority, rule, typeBinding, model);
                return ruleId;
            }
        }
        return null;
    }


    private TypeBinding buildModel(JSONObject modelTypes) {
        TypeBinding typeBinding = new TypeBinding();
        Iterator<?> keys = modelTypes.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object data = modelTypes.get(key);
            if (data instanceof String) {
                if (data instanceof String) {
                    DataType dataType = DataType.valueOf((String) data);
                    typeBinding.addTypes(key, dataType);
                }
            } else if (data instanceof JSONObject) {
                TypeBinding nested = buildModel((JSONObject) data);
                typeBinding.addTypes(key, nested);
            }
        }
        return typeBinding;
    }

    private TypeBinding buildModelFromValues(JSONObject modelTypes) {
        TypeBinding typeBinding = new TypeBinding();
        Iterator<?> keys = modelTypes.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object data = modelTypes.get(key);
            if (data instanceof String) {
                DataType dataType = LiteralType.computeDataType(data.toString());
                if (dataType == null) {
                    dataType = DataType.StringType;
                }
                typeBinding.addTypes(key, dataType);
            } else if (data instanceof JSONObject) {
                TypeBinding nested = buildModel((JSONObject) data);
                typeBinding.addTypes(key, nested);
            }
        }
        return typeBinding;
    }


    private DataRow buildValues(JSONObject model, TypeBinding typeBinding) {
        DataRow dataRow = new DataRow();
        Iterator<?> keys = model.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();

            if (model.get(key) instanceof JSONObject) {
                if (typeBinding.getDataType(key) instanceof TypeBinding) {
                    typeBinding = (TypeBinding) typeBinding.getDataType(key);
                    DataRow nested = buildValues((JSONObject) model.get(key), typeBinding);
                    dataRow.addData(key, nested);
                }
            } else {
                Object value = model.get(key);
                Object type = typeBinding.getDataType(key);
                if (type instanceof DataType) {
                    DataType dataType = (DataType) type;
                    try {
                        LiteralExpression literalExpression = LiteralType.getLiteralExpression(value.toString(), dataType);
                        dataRow.addData(key, literalExpression.getValue());
                    } catch (IllegalValueException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        return dataRow;
    }

}

/*

{
	"ruleName": "hammad",
	"priority": 1,
	"rule": "age between [ 18, 25]",
	"modelTypes": {
		"age": "IntegerType"
	},
	"model": {
		"age": 20
	}
}
 */