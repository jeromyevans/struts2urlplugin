package com.blueskyminds.struts20.urlplugin.configuration;

import com.opensymphony.xwork2.config.impl.MockConfiguration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.Configuration;
import com.blueskyminds.struts20.urlplugin.configuration.Example1Action;

/**
 * Used to setup Struts2.0 configuration for testing
 *
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class MockConfigurationFactory {

    public static final String DEFAULT_PACKAGE_NAME = "default";
    public static final String DEFAULT_PACKAGE_NAMESPACE = "/";
    public static final String PACKAGE1_NAME = "example";
    public static final String PACKAGE1_NAMESPACE = "/example";
    public static final String ACTION1_NAME = "example";
    public static final String ACTION2_NAME = "example2";

    /**
     * Defines the following actions:
     *    package: /
     *         action: example
     *
     *    package: /example
     *         action: example
     *         action: example2
     * @return
     */
    public static Configuration createConfiguration() {
        // setup a Struts2 configuration
        Configuration configuration = new MockConfiguration();
        PackageConfig defaultPackage = new PackageConfig(DEFAULT_PACKAGE_NAME, DEFAULT_PACKAGE_NAMESPACE, false);

        // Example1Action is used in the default package as well as package1.  The only difference is namespace
        ActionConfig default1Action = new ActionConfig();
        default1Action.setClassName(Example1Action.class.getName());
        defaultPackage.addActionConfig(ACTION1_NAME, default1Action);

        PackageConfig example1Package = new PackageConfig(PACKAGE1_NAME, PACKAGE1_NAMESPACE, false);
        ActionConfig example1Action = new ActionConfig();
        example1Action.setClassName(com.blueskyminds.struts20.urlplugin.configuration.Example1Action.class.getName());
        example1Package.addActionConfig(ACTION1_NAME, example1Action);

        // Example2Action is used only in package1
        ActionConfig example2Action = new ActionConfig();
        example2Action.setClassName(Example2Action.class.getName());
        example1Package.addActionConfig(ACTION2_NAME, example2Action);

        configuration.addPackageConfig(DEFAULT_PACKAGE_NAME, defaultPackage);
        configuration.addPackageConfig(PACKAGE1_NAME, example1Package);

        return configuration;
    }
}
