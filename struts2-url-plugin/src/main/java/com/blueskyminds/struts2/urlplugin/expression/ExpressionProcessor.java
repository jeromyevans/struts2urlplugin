package com.blueskyminds.struts2.urlplugin.expression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 *
 * Processes strings that contain expressions of the form ${name} and $1, notifying the
 * listener as as each variable is encountered.
 */
public class ExpressionProcessor {

    private static final Log LOG = LogFactory.getLog(ExpressionProcessor.class);

    private ExpressionProcessorListener listener;
    private boolean ignorePrefix;

    public ExpressionProcessor(ExpressionProcessorListener listener) {
        this.listener = listener;
        this.ignorePrefix = false;
    }

    public ExpressionProcessor(ExpressionProcessorListener listener, boolean ignorePrefix) {
        this(listener);
        this.ignorePrefix = ignorePrefix;
    }

    /**
     * Substitute values in the context into variables in the input expression.
     * This will substitute values of the form ${name} and $n where
     *   name is the name of a parameter in the MatchContext; or
     *   n is a group number in the MatchContext
     *
     * Implementation is a simple state machine.
     * */
    public String evaluate(String expression) {
        StringBuilder result = new StringBuilder();
        StringBuilder group = null;
        StringBuilder param = null;
        if (expression != null) {
            char[] chars = expression.toCharArray();
            ExpressionProcessorState state = ExpressionProcessorState.IN_TEXT;
            ExpressionProcessorState nextState = state;
            boolean charUsed;

            for (char ch : chars) {
                charUsed = false;
                switch (state) {
                    case IN_TEXT:
                        if (!ignorePrefix) {
                            if (ch == '$') {
                                nextState = ExpressionProcessorState.IN_VAR_START;
                                charUsed = true;
                            }
                        } else {
                            if (ch == '{') {
                                // start a new param name
                                param = new StringBuilder();
                                nextState = ExpressionProcessorState.IN_PARAM_NAME;
                                charUsed = true;
                            }
                        }
                        break;
                    case IN_PARAM_NAME:
                        if (ch == '}') {
                            nextState = listener.endParamName(this, result, param);
                            charUsed = true;
                        } else {
                            param.append(ch);
                            charUsed = true;
                        }
                        break;
                    case IN_VAR_START:
                        if (Character.isDigit(ch)) {
                            // start of a group number reference
                            group = new StringBuilder();
                            group.append(ch);
                            charUsed = true;
                            nextState = ExpressionProcessorState.IN_GROUP;
                        } else {
                            if (ch == '{') {
                                // start a new param name
                                param = new StringBuilder();
                                nextState = ExpressionProcessorState.IN_PARAM_NAME;
                                charUsed = true;
                            } else {
                                // invalid - rollback state, keep the $
                                result.append("$");
                                if (ch == '$') { // a double $ - this may be the prefix of a new var
                                    nextState = ExpressionProcessorState.IN_VAR_START;
                                    charUsed = true;
                                } else {
                                    charUsed = false;
                                    nextState = ExpressionProcessorState.IN_TEXT;
                                }
                            }
                        }
                        break;
                    case IN_GROUP:
                        if (Character.isDigit(ch)) {
                            group.append(ch);
                            charUsed = true;
                        } else {
                            nextState = listener.endGroup(this, result, group);
                        }
                        break;
                }

                if (!charUsed) {
                    result.append(ch);
                }
                state = nextState;
            }

            // tidy up at end of string
            switch (state) {
                case IN_GROUP:
                    listener.endGroup(this, result, group);
                    break;
                case IN_PARAM_NAME:
                    listener.endParamName(this, result, param);
                    break;
                case IN_VAR_START:
                    // restore the trailing $
                    result.append("$");
                    break;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(expression+" -> "+result.toString());
            }
            return result.toString();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("expression is null");
            }
            return null;
        }
    }
       
}
