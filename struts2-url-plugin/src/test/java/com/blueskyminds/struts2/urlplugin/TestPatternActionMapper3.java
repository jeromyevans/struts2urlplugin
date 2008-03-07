package com.blueskyminds.struts2.urlplugin;

import junit.framework.TestCase;
import com.blueskyminds.struts2.urlplugin.configuration.ActionMapConfiguration;
import com.blueskyminds.struts2.urlplugin.configuration.MockConfigurationFactory;
import com.blueskyminds.struts2.urlplugin.configuration.digester.XMLActionMapConfiguration;
import com.blueskyminds.struts2.urlplugin.utils.ComponentURI;
import com.blueskyminds.struts2.urlplugin.matcher.action.name.ActionNameMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.name.PlainTextActionNameMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.namespace.NamespaceMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.namespace.PlainTextNamespaceMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.ActionMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.action.DefaultActionMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.MockMatcherProvider;
import com.blueskyminds.struts2.urlplugin.matcher.uri.URIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.uri.DefaultURIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.uri.keyword.KeywordURIMatcher;
import com.blueskyminds.struts2.urlplugin.matcher.uri.regex.RegExURIMatcher;
import com.opensymphony.xwork2.config.Configuration;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * Uses the XML configuration
 *
 * Date Started: 23/02/2008
 * <p/>
 * History:
 */
public class TestPatternActionMapper3 extends TestCase {

    private ActionMapConfiguration actionMapConfiguration;
    private Configuration configuration;

    private MockMatcherProvider<ActionNameMatcher> actionMatcherProvider;
    private MockMatcherProvider<NamespaceMatcher> namespaceMatcherProvider;
    private MockMatcherProvider<URIMatcher> uriMatcherProvider;
    private ActionMatcher actionMatcher;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        actionMapConfiguration = new XMLActionMapConfiguration();
        ((XMLActionMapConfiguration) actionMapConfiguration).setConfiguration("struts-test3-urls.xml");

        //setup the MatcherProvider.  The matchers control how to match an ActionConfig
        actionMatcherProvider = new MockMatcherProvider<ActionNameMatcher>();
        actionMatcherProvider.addMatcher(PlainTextActionNameMatcher.DEFAULT_NAME, new PlainTextActionNameMatcher());

        namespaceMatcherProvider = new MockMatcherProvider<NamespaceMatcher>();
        namespaceMatcherProvider.addMatcher(PlainTextNamespaceMatcher.DEFAULT_NAME, new PlainTextNamespaceMatcher());

        uriMatcherProvider = new MockMatcherProvider<URIMatcher>();
        uriMatcherProvider.addMatcher(RegExURIMatcher.DEFAULT_NAME, new RegExURIMatcher());
        uriMatcherProvider.addMatcher(DefaultURIMatcher.DEFAULT_NAME, new DefaultURIMatcher());
        uriMatcherProvider.addMatcher(KeywordURIMatcher.DEFAULT_NAME, new KeywordURIMatcher());

        actionMatcher = new DefaultActionMatcher(namespaceMatcherProvider, actionMatcherProvider);

        configuration = MockConfigurationFactory.createConfiguration();
    }

    public void testMapper() {
        URLPatternActionMapper actionMapper = new URLPatternActionMapper(actionMapConfiguration, actionMatcher, uriMatcherProvider);
        ActionMapping mapping = actionMapper.getMapping(new ComponentURI("GET", "/example/example.action", null), configuration);

        assertNotNull(mapping);
        assertEquals(MockConfigurationFactory.ACTION1_NAME, mapping.getName());
        assertEquals(MockConfigurationFactory.PACKAGE1_NAMESPACE, mapping.getNamespace());

        assertNull(actionMapper.getMapping(new ComponentURI("GET", "/example/acb.action", null), configuration));
        assertNull(actionMapper.getMapping(new ComponentURI("GET", "/example/.action", null), configuration));
        assertNull(actionMapper.getMapping(new ComponentURI("GET", "/example/example", null), configuration));
        assertNull(actionMapper.getMapping(new ComponentURI("GET", "/example/example/example.action", null), configuration));

        assertNotNull(actionMapper.getMapping(new ComponentURI("POST", "/example/example.action", null), configuration));

        assertNotNull(actionMapper.getMapping(new ComponentURI("GET", "/example.action", null), configuration));
        assertNull(actionMapper.getMapping(new ComponentURI("GET", "/example2.action", null), configuration));
        assertNotNull(actionMapper.getMapping(new ComponentURI("GET", "/example/example2.action", null), configuration));
    }
}
