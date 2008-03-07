package com.blueskyminds.struts2.urlplugin.expression;

import java.util.Map;

/**
 * Processes expressions within a simple context
 */
public class SimpleExpression {
       
    /** Process an expression containing references to keys in a map */
    public static String evaluate(String expression, final Map<String, String> context) {
        ExpressionProcessor expressionProcessor = new ExpressionProcessor(new ExpressionProcessorListener() {

            public ExpressionProcessorState endParamName(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder param) {
                String value = context.get(param.toString());
                if (value != null) {
                    result.append(value);
                }
                return ExpressionProcessorState.IN_TEXT;
            }

            public ExpressionProcessorState endGroup(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder group) {
                return ExpressionProcessorState.IN_TEXT;
            }
        });
        return expressionProcessor.evaluate(expression);
    }


}
