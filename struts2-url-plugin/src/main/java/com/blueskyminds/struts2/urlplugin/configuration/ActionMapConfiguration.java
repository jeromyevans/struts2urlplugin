package com.blueskyminds.struts2.urlplugin.configuration;

import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.List;
import java.util.Map;

/**
 * Provides the configuration of URIs to Action mappings
 *
 */
public interface ActionMapConfiguration {

    /** Get list of URI to action mappings in order of precedence */
    List<ActionMapDefinition> getActionMappings();

    /** Get the filter to apply to the request (optional) */
    FilterDefinition getFilter();

    /**
     * Prepares an initial MatchContext
     *
     * @return new MatchContext instance
     */
    MatchContext prepareMatchContext();

    /**
     * Get the initial context map
     * @return
     */
    Map<String, String> getContext();
}
