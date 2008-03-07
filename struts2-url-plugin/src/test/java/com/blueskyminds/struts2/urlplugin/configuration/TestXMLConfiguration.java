package com.blueskyminds.struts2.urlplugin.configuration;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.struts2.urlplugin.configuration.digester.XMLActionMapConfiguration;
import com.blueskyminds.struts2.urlplugin.matcher.MatchContext;

import java.util.List;

/**
 * Date Started: 31/01/2008
 * <p/>
 * History:
 */
public class TestXMLConfiguration extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestXMLConfiguration.class);

    public void testReadConfig() throws Exception {
        XMLActionMapConfiguration configuration = new XMLActionMapConfiguration();
        configuration.setConfiguration("struts-test1-urls.xml");

        MatchContext matchContext = configuration.prepareMatchContext();

        List<ActionMapDefinition> actionMappings = configuration.getActionMappings();
        assertEquals(1, actionMappings.size());

        ActionMapDefinition definition = actionMappings.get(0);
        assertEquals("test1", definition.getId());
        List<URIPattern> patterns = definition.getPatterns();
        assertEquals(2, patterns.size());
        URIPattern pattern1 = patterns.get(0);
        URIPattern pattern2 = patterns.get(1);

        assertNull(pattern1.getId());
        assertEquals(".*", pattern1.getMethod());
        assertEquals("^(.+)/(.*)\\.action$", pattern1.getPath());
        assertEquals(2, pattern1.getParams().size());
        assertEquals("$1", pattern1.getParams().get("path"));
        assertEquals("$2", pattern1.getParams().get("name"));

        assertNull(pattern2.getId());
        assertEquals(".*", pattern2.getMethod());
        assertEquals("^/{0,1}(.*)\\.action$", pattern2.getPath());
        assertEquals(2, pattern2.getParams().size());
        assertEquals("/", pattern2.getParams().get("path"));
        assertEquals("$1", pattern2.getParams().get("name"));
   }

    public void testReadConfig2() throws Exception {
        XMLActionMapConfiguration configuration = new XMLActionMapConfiguration();
        configuration.setConfiguration("struts-test2-urls.xml");

        List<ActionMapDefinition> actionMappings = configuration.getActionMappings();
        assertEquals(1, actionMappings.size());

        ActionMapDefinition definition = actionMappings.get(0);
        assertEquals("test1", definition.getId());
        List<URIPattern> patterns = definition.getPatterns();
        assertEquals(2, patterns.size());
        URIPattern pattern1 = patterns.get(0);
        URIPattern pattern2 = patterns.get(1);

        assertNull(pattern1.getId());
        assertEquals(".*", pattern1.getMethod());
        assertEquals("^(.+)/(.*)\\.action$", pattern1.getPath());
        assertEquals(2, pattern1.getParams().size());
        assertEquals("$1", pattern1.getParams().get("path"));
        assertEquals("$2", pattern1.getParams().get("name"));

        assertNull(pattern2.getId());
        assertEquals(".*", pattern2.getMethod());
        assertEquals("^/{0,1}(.*)\\.action$", pattern2.getPath());
        assertEquals(2, pattern2.getParams().size());
        assertEquals("/", pattern2.getParams().get("path"));
        assertEquals("$1", pattern2.getParams().get("name"));

        List<ActionSelector> selectors = definition.getActionSelectors();
        assertEquals(2, selectors.size());
        ActionSelector firstSelector = selectors.get(0);
        ActionSelector secondSelector = selectors.get(1);
        assertEquals(1, firstSelector.getParams().size());
   }

    public void testReadConfig3() throws Exception {
        XMLActionMapConfiguration configuration = new XMLActionMapConfiguration();
        configuration.setConfiguration("struts-test3-urls.xml");

        MatchContext matchContext = configuration.prepareMatchContext();
        assertEquals("(/{0,1}.+)", matchContext.get("namespace"));
        assertEquals("([\\w|\\d|\\-]*)", matchContext.get("action"));
        assertEquals("\\.action", matchContext.get("ext"));

        List<ActionMapDefinition> actionMappings = configuration.getActionMappings();
        assertEquals(1, actionMappings.size());

        ActionMapDefinition definition = actionMappings.get(0);
        assertEquals("test1", definition.getId());
        List<URIPattern> patterns = definition.getPatterns();
        assertEquals(2, patterns.size());
        URIPattern pattern1 = patterns.get(0);
        URIPattern pattern2 = patterns.get(1);

        assertNull(pattern1.getId());
        assertEquals(".*", pattern1.getMethod());
        assertEquals("^${namespace}/${action}${ext}$", pattern1.getPath());
        assertEquals(2, pattern1.getParams().size());
        assertEquals("$1", pattern1.getParams().get("path"));
        assertEquals("$2", pattern1.getParams().get("name"));

        assertNull(pattern2.getId());
        assertEquals(".*", pattern2.getMethod());
        assertEquals("^/{0,1}${action}${ext}$", pattern2.getPath());
        assertEquals(2, pattern2.getParams().size());
        assertEquals("/", pattern2.getParams().get("path"));
        assertEquals("$1", pattern2.getParams().get("name"));

        List<ActionSelector> selectors = definition.getActionSelectors();
        assertEquals(2, selectors.size());
        ActionSelector firstSelector = selectors.get(0);
        ActionSelector secondSelector = selectors.get(1);
        assertEquals(1, firstSelector.getParams().size());
   }
}
