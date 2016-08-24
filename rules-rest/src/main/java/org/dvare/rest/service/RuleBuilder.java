package org.dvare.rest.service;


import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;
import org.dvare.rest.registry.RuleEngineRegistry;
import org.dvare.rest.ruleengine.RestRuleEngine;
import org.dvare.util.DataTypeMapping;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RuleBuilder {

    Logger logger = Logger.getLogger(RuleBuilder.class);

    public static Class<?> createClass(String modelName, JSONObject modelTypes) {

        final Map<String, Class<?>> properties =
                new HashMap<>();


        Iterator<?> keys = modelTypes.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object data = modelTypes.get(key);

            if (data instanceof String) {
                Class propType = DataTypeMapping.getDataTypeMapping((String) data);
                properties.put(key, propType);
            } else if (data instanceof JSONObject) {
                Class propType = createClass("nested" + modelName, (JSONObject) data);
                properties.put(key, propType);
            }
        }

        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setSuperclass(Object.class);
        beanGenerator.setNamingPolicy(new NamingPolicy() {
            @Override
            public String getClassName(final String prefix,
                                       final String source, final Object key, final Predicate names) {
                return modelName;
            }
        });

        BeanGenerator.addProperties(beanGenerator, properties);

        return (Class<?>) beanGenerator.createClass();
    }

    public static Object setValueObject(Object object, JSONObject modelTypes, JSONObject model) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Iterator<?> keys = model.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();


            if (model.get(key) instanceof String) {
                String setter = "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                Object type = modelTypes.get(key);
                Class propType = DataTypeMapping.getDataTypeMapping((String) type);
                MethodUtils.invokeMethod(object, setter, new Object[]{model.get(key)}, new Class[]{propType});

            } else if (model.get(key) instanceof JSONObject) {
                String getter = "get" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                String setter = "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                Object propObject = MethodUtils.invokeMethod(object, getter);
                Object value = setValueObject(propObject, (JSONObject) modelTypes.get(key), (JSONObject) model.get(key));

                MethodUtils.invokeMethod(object, setter, new Object[]{value}, new Class[]{value.getClass()});


            }
        }
        return object;
    }

    public static void main(String args[]) throws Exception {
        String jsonString = "{ \"age\" : \"IntegerType\"}";
        JSONObject modelTypes = new JSONObject(jsonString);

        String jsonString1 = "{ \"age\" : 25}";
        JSONObject model = new JSONObject(jsonString1);


        Class type = createClass("ModelClass", modelTypes);
        for (Field field : type.getDeclaredFields()) {
            System.out.println(field.getName());
        }
        Object instance = type.newInstance();
        instance = setValueObject(instance, modelTypes, model);


        System.out.println(instance);
        System.out.println(instance.getClass());


    }

    public String buildRule(String ruleEngine, String ruleString) {

        if (ruleString != null && !ruleString.isEmpty()) {
            JSONObject jsonObject = new JSONObject(ruleString);

            String ruleName = jsonObject.getString("ruleName");
            Integer rulePriority = jsonObject.getInt("rulePriority");
            String rule = jsonObject.getString("rule");

            String modelName = jsonObject.getString("modelName");
            JSONObject modelTypes = jsonObject.getJSONObject("modelTypes");
            JSONObject values = jsonObject.getJSONObject("model");


            Object model = buildModel("", modelTypes, values);


            RuleEngineRegistry ruleEngineRegistry = RuleEngineRegistry.INSTANCE;
            RestRuleEngine restRuleEngine = ruleEngineRegistry.getRuleEngine(ruleEngine);


            String ruleId = restRuleEngine.registerRule(ruleName, rulePriority, rule, model);


            return ruleId;
        }
        return null;
    }

    private Object buildModel(String modelName, JSONObject modelTypes, JSONObject model) {
        try {
            Class type = createClass(modelName, modelTypes);
            Object instance = type.newInstance();
            instance = setValueObject(instance, modelTypes, model);
            return instance;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }


        return null;
    }

}
