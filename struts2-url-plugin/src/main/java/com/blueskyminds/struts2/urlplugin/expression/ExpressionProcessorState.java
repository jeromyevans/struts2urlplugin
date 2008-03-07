package com.blueskyminds.struts2.urlplugin.expression;

/** State of the variable name substitution */
public enum ExpressionProcessorState {
    IN_TEXT,
    IN_VAR_START,
    IN_PARAM_NAME,
    IN_GROUP
}