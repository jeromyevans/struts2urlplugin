package com.blueskyminds.struts2.urlplugin.expression;

/**
 * The ExpressionProcessorListener is notified when a parameter/group is encountered in
 * an expression
 *
 * Date Started: 22/02/2008
 * <p/>
 * History:
 */
public interface ExpressionProcessorListener {

    /**
     * This method is called at the end of a parameter name in an expression
     *
     * eg. ${name}
     *
     * @param expressionProcessor
     * @param result
     * @param param
     * @return
     */
    ExpressionProcessorState endParamName(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder param);

    /**
     * This method is called at the end of a group number in an expression
     *
     * eg. $1
     *
     * @param expressionProcessor
     * @param result
     * @param group
     * @return
     */
    ExpressionProcessorState endGroup(ExpressionProcessor expressionProcessor, StringBuilder result, StringBuilder group);
}
