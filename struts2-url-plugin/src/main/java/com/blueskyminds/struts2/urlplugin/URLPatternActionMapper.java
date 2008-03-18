package com.blueskyminds.struts2.urlplugin;

import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.ActionContext;
import com.blueskyminds.struts2.urlplugin.configuration.*;
import com.blueskyminds.struts2.urlplugin.matcher.uri.URIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.blueskyminds.struts2.urlplugin.matcher.action.ActionMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.MatcherProvider;
import com.blueskyminds.struts2.urlplugin.utils.ComponentURI;
import com.blueskyminds.struts2.urlplugin.filter.RequestFilter;
import com.blueskyminds.struts2.urlplugin.expression.SimpleExpression;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * A Struts2 ActionMapper that uses patterns to match the URL to action
 */
public class URLPatternActionMapper implements ActionMapper {

    private static final Log LOG = LogFactory.getLog(URLPatternActionMapper.class);

    private ActionMapConfiguration mappingConfiguration;
    private ActionMatcher actionMatcher;

    private String mappingConfigurationName;
    private String actionMatcherName;
    private MatcherProvider<URIMatcher> uriMatcherProvider;
    private Container container;

    private RequestFilter requestFilter;

    private static Method extensionMethod = null;

    /** Temporary reflective API compatibility with Struts 2.0 */
    static {
        try {
            extensionMethod = ActionMapping.class.getMethod("getExtension");
        } catch (NoSuchMethodException e) {
            // swallow - not supported by this API
        }
    }

    /** Craete a new URLPatternActionMapper.  The ActionMapConfiguration, URIMatcher and ActionMatcher need to be injected */
    public URLPatternActionMapper() {
    }


    public URLPatternActionMapper(ActionMapConfiguration mappingConfiguration, ActionMatcher actionMatcher, MatcherProvider<URIMatcher> uriMatcherProvider) {
        this.mappingConfiguration = mappingConfiguration;
        this.actionMatcher = actionMatcher;
        this.uriMatcherProvider = uriMatcherProvider;
    }

    /**
     * Derives an ActionMapping for the current request using the ActionMapConfiguration and matchers
     *
     * @param httpServletRequest     The servlet request
     * @param configurationManager   The current configuration manager
     * @return The appropriate action mapping or null if no match
     */
    public ActionMapping getMapping(HttpServletRequest httpServletRequest, ConfigurationManager configurationManager) {

        setupConfiguration();

        if ((requestFilter == null) || (requestFilter.accept(httpServletRequest))) {
            String method = httpServletRequest.getMethod();
            String contextPath = httpServletRequest.getContextPath();
            String path;
            if (contextPath.length() > 0) {
                // strip the context path
                String fullPath = httpServletRequest.getRequestURI();
                path = fullPath.substring(fullPath.indexOf(contextPath)+contextPath.length());
            } else {
                path = httpServletRequest.getRequestURI();
            }
            String query = httpServletRequest.getQueryString();
            Configuration configuration = configurationManager.getConfiguration();

            ComponentURI uri = new ComponentURI(method, path, query);

            return getMapping(uri, configuration);
        } else {
            return null;
        }
    }

    /** todo: this method is stupid. */
    public ActionMapping getMappingFromActionName(String actionName) {
        ActionMapping actionMapping = new ActionMapping();
        actionMapping.setName(actionName);
        return actionMapping;
    }

    /**
     * Requests the container to provide instances with the injected name
     * Name injection rather than instance injection is used so the beans can be easily overridden
     */
    private void setupConfiguration() {
        if (mappingConfiguration == null) {
            mappingConfiguration = container.getInstance(ActionMapConfiguration.class, mappingConfigurationName);
        }

        if (actionMatcher == null) {
            actionMatcher = container.getInstance(ActionMatcher.class, actionMatcherName);
        }

        if (uriMatcherProvider == null) {
            // use the Container as a provider of URIMatchers
            uriMatcherProvider = new MatcherProvider<URIMatcher>() {
                public URIMatcher getMatcher(String name) {
                    return container.getInstance(URIMatcher.class, name);
                }
            };
        }

        if (requestFilter == null) {
            FilterDefinition filterDefinition = mappingConfiguration.getFilter();
            if ((filterDefinition != null) && (filterDefinition.getName() != null)) {
                requestFilter = container.getInstance(RequestFilter.class, filterDefinition.getName());
                if (requestFilter != null) {
                    // set the parameters of the filter. evaluate the values against the initial context
                    for (Map.Entry<String, String> entry : filterDefinition.getParameters().entrySet()) {
                        requestFilter.setParam(entry.getKey(), SimpleExpression.evaluate(entry.getValue(), mappingConfiguration.getContext()));
                    }
                }
            }
        }
    }

    /**
     * Creates an ActionMapping based on the URI and Struts2 Configuration
     * Iterates through each ActionMapDefinition injected for a matching URIPattern and Action
     **/
    public ActionMapping getMapping(ComponentURI uri, Configuration configuration) {
        ActionMapping actionMapping = null;
        MatchContext matchContext;

        if (LOG.isDebugEnabled()) {
            LOG.debug("uri:"+uri.toString());
        }
        if (mappingConfiguration != null) {
            // check the action map definitions in order
            for (ActionMapDefinition actionMapDefinition : mappingConfiguration.getActionMappings()) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("--- processing action-map id: '"+actionMapDefinition.getId()+"': ---");
                }

                matchContext = mappingConfiguration.prepareMatchContext();

                // check each URI pattern...
                Iterator<URIPattern> uriPatternIterator = actionMapDefinition.getPatterns().iterator();
                int patternNo = 0;
                while ((actionMapping == null) && (uriPatternIterator.hasNext())) {
                    URIPattern uriPattern = uriPatternIterator.next();
                    URIMatcher uriMatcher = uriMatcherProvider.getMatcher(uriPattern.getType());
                    if (uriMatcher != null) {
                        if (uriMatcher.match(uri, uriPattern, matchContext)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("\nmatchContext="+matchContext.toString());
                            }
                            // The URI matches, check each action selector...
                            Iterator<ActionSelector> selectorIterator = actionMapDefinition.getActionSelectors().iterator();
                            int selectorNo = 0;
                            while ((actionMapping == null) && (selectorIterator.hasNext())) {
                                ActionSelector actionSelector = selectorIterator.next();
                                actionMapping = actionMatcher.match(actionSelector, matchContext, configuration);
                                if (LOG.isDebugEnabled()) {
                                    if (actionMapping == null) {
                                        LOG.debug("No match for ActionMapDefinition['"+actionMapDefinition.getId()+"'].actionSelector["+selectorNo+"]");
                                    }
                                }
                                if (actionMapping != null) {
                                    // transfer params from the selector to the action
                                    transferParams(actionMapping, actionSelector.evaluateParams(matchContext));
                                }
                                selectorNo++;
                            }

                            if (actionMapping != null) {
                                // apply the properties to the action mapping
                                if (matchContext.isPropertiesDefined()) {
                                    // transfer properties from the match to the action
                                    transferParams(actionMapping, matchContext.evaluateProperties());

                                    if (LOG.isDebugEnabled()) {
                                        Map map = actionMapping.getParams();
                                        LOG.debug("actionMapping.params = {");
                                        for (Object key : map.keySet()) {
                                            LOG.debug("   "+key+":"+map.get(key));
                                        }
                                        LOG.debug("}");
                                    }
                                }
                            }

                            if (LOG.isDebugEnabled()) {
                                if (actionMapping == null) {
                                    LOG.debug("No matches for an action within this context");
                                }
                            }
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("No match for this URI to "+(uriPattern.getId() != null ? " URIPattern with ID "+uriPattern.getId() : "ActionMapDefinition['"+actionMapDefinition.getId()+"'].URIPattern["+patternNo+"]"));
                            }
                        }
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("No URIMatcher with name '"+uriPattern.getType()+"' defined in the container. Either the name is incorrect or the bean is not defined in struts.xml/struts-plugin.xml");
                        }
                    }
                    patternNo++;
                }

                if (actionMapping != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Successfully matched ActionMapDefinition['"+actionMapDefinition.getId()+"']");
                    }
                    break;
                }
            }
        }

        return actionMapping;
    }

    private void transferParams(ActionMapping actionMapping, Map<String, String> properties) {
        if ((properties != null) && (properties.size() > 0)) {
            Map<String, String> params = actionMapping.getParams();
            if (params == null) {
                actionMapping.setParams(new HashMap<String, String>(properties));
            } else {
                params.putAll(properties);
            }
        }
    }

    /**
     * Borrowed from DefaultActionMapper
     * This isn't going to work in every scenario
     *
     * *** Currently uses a relfective invocation to maintain API compatibility with Struts 2.0
     * */
    public String getUriFromActionMapping(ActionMapping mapping) {
        StringBuffer uri = new StringBuffer();

        if (mapping.getNamespace() != null) {
            uri.append(mapping.getNamespace());
            if (!"/".equals(mapping.getNamespace())) {
                uri.append("/");
            }
        }
        String name = mapping.getName();
        String params = "";
        if (name.indexOf('?') != -1) {
            params = name.substring(name.indexOf('?'));
            name = name.substring(0, name.indexOf('?'));
        }
        uri.append(name);

        if (null != mapping.getMethod() && !"".equals(mapping.getMethod())) {
            uri.append("!").append(mapping.getMethod());
        }

        if (extensionMethod != null) {
            try {
                // *** temporary reflective invokcation of the getExtenion method of ActionMapping for struts 2.0
                // *** compatibility
                String extension = (String) extensionMethod.invoke(mapping);
                if (extension == null) {
                    extension = "";
                    // Look for the current extension, if available
                    ActionContext context = ActionContext.getContext();
                    if (context != null) {
                        ActionMapping orig = (ActionMapping) context.get(ServletActionContext.ACTION_MAPPING);
                        if (orig != null) {
                            extension = (String) extensionMethod.invoke(orig);
                        }
                    }
                }

                if (extension != null) {
                    if (extension.length() == 0 || (extension.length() > 0 && uri.indexOf('.' + extension) == -1)) {
                        if (extension.length() > 0) {
                            uri.append(".").append(extension);
                        }
                        if (params.length() > 0) {
                            uri.append(params);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                LOG.error(e);
            } catch (IllegalArgumentException e) {
                LOG.error(e);
            } catch (InvocationTargetException e) {
                LOG.error(e);
            }
        }

        return uri.toString();
    }

    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Inject the name of the ActionMapConfiguration instance to use.
     * The inject is requested from the Container.
     *
     * Name injection is used rather than instance injection so the bean can be easily overridden in struts.xml
     *
     * @param mappingConfigurationName
     */
    @Inject("struts.urlplugin.actionMapConfiguration")
    public void setMappingConfigurationName(String mappingConfigurationName) {
        this.mappingConfigurationName = mappingConfigurationName;
    }

    /**
     * Inject the name of the ActionMatcher instance to use.
     * The inject is requested from the Container.
     *
     * Name injection is used rather than instance injection so the bean can be easily overridden in struts.xml
     *
     * @param actionMatcherName
     */
    @Inject("struts.urlplugin.actionMatcher")
    public void setActionMatcherName(String actionMatcherName) {
        this.actionMatcherName = actionMatcherName;
    }

}
