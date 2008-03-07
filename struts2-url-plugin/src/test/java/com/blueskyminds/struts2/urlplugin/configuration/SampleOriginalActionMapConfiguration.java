package com.blueskyminds.struts2.urlplugin.configuration;

import com.blueskyminds.struts2.urlplugin.configuration.ActionMapConfiguration;
import com.blueskyminds.struts2.urlplugin.configuration.ActionMapDefinition;
import com.blueskyminds.struts2.urlplugin.configuration.URIPattern;
import com.blueskyminds.struts2.urlplugin.configuration.ActionSelector;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Sets up an ActionMapConfiguration for testing.
 *
 * This sample sets up the original action mapping settings for Struts 2:
 *   /path/name.action ->  namespace: /path action: name  
 *
 */
public class SampleOriginalActionMapConfiguration implements ActionMapConfiguration {

    private List<ActionMapDefinition> actionMapDefinitions;

    public SampleOriginalActionMapConfiguration() {
        init();
    }

    private void init() {
        actionMapDefinitions = new LinkedList<ActionMapDefinition>();

        ActionMapDefinition jsonMapping = new ActionMapDefinition("default");
        URIPattern pattern1 = new URIPattern("1", "regex", ".*", "^(.+)/(.*)\\.action$");
        pattern1.addParameter("path", "$1");
        pattern1.addParameter("name", "$2");
        URIPattern pattern2 = new URIPattern("2", "regex", ".*", "^/{0,1}(.*)\\.action$");
        pattern2.addParameter("path", "/");
        pattern2.addParameter("name", "$1");

        jsonMapping.addURIPattern(pattern1);
        jsonMapping.addURIPattern(pattern2);
        jsonMapping.addActionSelector(new ActionSelector("plainText", "${path}", "plainText", "${name}", "execute"));
        actionMapDefinitions.add(jsonMapping);
    }

    /**
     * Get list of URI to action mappings in order of precedence
     */
    public List<ActionMapDefinition> getActionMappings() {
        return actionMapDefinitions;
    }

    /**
     * Prepares an initial MatchContext
     *
     * @return new MatchContext instance
     */
    public MatchContext prepareMatchContext() {
        return new MatchContext();
    }

    /**
     * Get the filter to apply to the request (optional)
     */
    public FilterDefinition getFilter() {
        return null;  
    }

    /**
     * Get the initial context map
     *
     * @return
     */
    public Map<String, String> getContext() {
        return new HashMap<String, String>();
    }
}
