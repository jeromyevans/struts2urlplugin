package com.blueskyminds.struts2.urlplugin.matcher;

import com.blueskyminds.struts2.urlplugin.expression.ExpressionProcessorState;
import com.blueskyminds.struts2.urlplugin.expression.ExpressionProcessorListener;
import com.blueskyminds.struts2.urlplugin.expression.ExpressionProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles evaluation of expressions in MatchContext values
 *
 * Date Started: 22/02/2008
 * <p/>
 * History:
 */
public class MatchContextExpressionListener implements ExpressionProcessorListener {

    private static final Log LOG = LogFactory.getLog(MatchContextExpressionListener.class);

    private MatchContext matchContext;

    public MatchContextExpressionListener(MatchContext matchContext) {
        this.matchContext = matchContext;
    }

    /**
     * When the end of a an expression parameter is encountered, lookup the param in the
     * MatchContext and substitute the value into the result
     *
     * This method will recursively evaluate expressions in the result
     *
     * @param result
     * @param param
     * @return
     */
    public ExpressionProcessorState endParamName(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder param) {
        ExpressionProcessorState nextState;// end of parameter name...look it up
        String value = matchContext.get(param.toString());
        if (value != null) {
            if (value.contains("$")) {
                // recursive evaluation
                value = expressionProcessor.evaluate(value);
            }
        }
        result.append(value != null ? value : "");
        nextState = ExpressionProcessorState.IN_TEXT;
        return nextState;
    }

    /** When the end of a group expression parameter is encountered, lookup the group value in
     * the MatchContext and substitute the value into the result
     *
     * @param result
     * @param group
     * @return
     */
    public ExpressionProcessorState endGroup(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder group) {
        ExpressionProcessorState nextState;// end of group number
        try {
            int index = Integer.parseInt(group.toString());
            String value = matchContext.getGroup(index);
            result.append(value != null ? value : "");
        } catch (NumberFormatException e) {
            LOG.error("Pattern contains an invalid group reference (group="+group.toString()+")");
        }

        nextState = ExpressionProcessorState.IN_TEXT;
        return nextState;
    }
}
