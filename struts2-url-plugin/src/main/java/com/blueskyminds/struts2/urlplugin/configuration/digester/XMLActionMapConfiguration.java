package com.blueskyminds.struts2.urlplugin.configuration.digester;

import com.blueskyminds.struts2.urlplugin.configuration.*;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;
import com.opensymphony.xwork2.util.ClassLoaderUtil;
import com.opensymphony.xwork2.inject.Inject;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.xml.sax.SAXException;

/**
 * Contains all the configuration for the URI to Action mapping read from an XML file
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class XMLActionMapConfiguration implements ActionMapConfiguration {

    private static final Log LOG = LogFactory.getLog(XMLActionMapConfiguration.class);

    private Map<String, String> initialContext;
    private List<ActionMapDefinition> actionMappings;
    private FilterDefinition filterDefinition;

    public XMLActionMapConfiguration() {        
        actionMappings = new LinkedList<ActionMapDefinition>();
        initialContext = new HashMap<String, String>();
    } 

    public boolean readConfiguration(InputStream inputStream) {
        boolean okay = false;
        Digester digester = new Digester();

        URL dtd = ClassLoaderUtil.getResource("struts-urls-0.1.dtd", XMLActionMapConfiguration.class);
        digester.register("-//Blue Sky Minds//DTD Struts2 URL Plugin Configuration 0.1//EN", dtd);
        digester.setValidating(false);
        
        digester.push(this);
        digester.addSetProperties("action-mappings");
        digester.addObjectCreate("action-mappings/context", HashMap.class);
        digester.addCallMethod("action-mappings/context/param", "put", 2);
        digester.addCallParam("action-mappings/context/param", 0, "name");
        digester.addCallParam("action-mappings/context/param", 1);     // body
        digester.addSetNext("action-mappings/context", "setContext");
        digester.addObjectCreate("action-mappings/filter", FilterDefinition.class);
        digester.addSetProperties("action-mappings/filter");
        digester.addCallMethod("action-mappings/filter/param", "setParam", 2);
        digester.addCallParam("action-mappings/filter/param", 0, "name");
        digester.addCallParam("action-mappings/filter/param", 1);     // body
        digester.addSetNext("action-mappings/filter", "setFilter");
        digester.addObjectCreate("action-mappings/action-map", ActionMapDefinition.class);
        digester.addSetProperties("action-mappings/action-map");
        digester.addSetNext("action-mappings/action-map", "addDefinition");
        digester.addObjectCreate("action-mappings/action-map/uri-patterns/uri-pattern", URIPattern.class);
        digester.addSetProperties("action-mappings/action-map/uri-patterns/uri-pattern");
        digester.addSetNext("action-mappings/action-map/uri-patterns/uri-pattern", "addURIPattern");
        digester.addCallMethod("action-mappings/action-map/uri-patterns/uri-pattern/param", "addParameter", 2);
        digester.addCallParam("action-mappings/action-map/uri-patterns/uri-pattern/param", 0, "name");
        digester.addCallParam("action-mappings/action-map/uri-patterns/uri-pattern/param", 1);     // body
        digester.addObjectCreate("action-mappings/action-map/action-selectors/action", ActionSelector.class);
        digester.addSetProperties("action-mappings/action-map/action-selectors/action");
        digester.addSetNext("action-mappings/action-map/action-selectors/action", "addActionSelector");
        digester.addCallMethod("action-mappings/action-map/action-selectors/action/param", "addParameter", 2);
        digester.addCallParam("action-mappings/action-map/action-selectors/action/param", 0, "name");
        digester.addCallParam("action-mappings/action-map/action-selectors/action/param", 1);     // body

        try {
            digester.parse(inputStream);
            okay = true;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return okay;
    }

    public void addDefinition(ActionMapDefinition definition) {
        actionMappings.add(definition);
    }

    public List<ActionMapDefinition> getActionMappings() {
        return actionMappings;
    }

    @Inject("struts.urlplugin.configFile")
    public void setConfiguration(String fileName) {
        InputStream input = ClassLoaderUtil.getResourceAsStream(fileName, XMLActionMapConfiguration.class);
        if (input != null) {
            readConfiguration(input);
        } else {
            LOG.error("Could not open the Struts2UrlPLugin XML Configuration file specified by struts.urlplugin.configFile="+fileName);
        }
    }

    /**
     * Prepares a MatchContext setup with the initial context parameters
     *
     * @return new MatchContext instance
     */
    public MatchContext prepareMatchContext() {
        MatchContext matchContext = new MatchContext();
        matchContext.putAll(initialContext);
        return matchContext;
    }

    public FilterDefinition getFilter() {
        return filterDefinition;
    }

    public void setFilter(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    /**
     * Get the initial context map
     *
     * @return
     */
    public Map<String, String> getContext() {
        return initialContext;
    }

    /**
     * Setup parameters for the initial MatchContext
     *
     * @param context
     */
    public void setContext(Map<String, String> context) {
        this.initialContext = context;
    }
}
