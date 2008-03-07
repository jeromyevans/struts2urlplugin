package com.blueskyminds.struts2.urlplugin.configuration;

import com.opensymphony.xwork2.config.impl.MockConfiguration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.Configuration;
import com.blueskyminds.struts2.urlplugin.configuration.Example1Action;
import com.blueskyminds.struts2.urlplugin.configuration.Example2Action;

/**
 * Used to setup Struts2 configuration for testing
 *
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class MockConfigurationFactory {

    public static final String DEFAULT_PACKAGE_NAME = "default";
    public static final String DEFAULT_PACKAGE_NAMESPACE = "/";
    public static final String SECOND_PAGE_NAME = "example";
    public static final String PACKAGE1_NAMESPACE = "/example";
    public static final String DEFAULT_ACTION_NAME = "example";
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

        // Example1Action is used in the default package as well as package1.  The only difference is namespace
        ActionConfig defaultACtion = new ActionConfig.Builder(DEFAULT_PACKAGE_NAME, DEFAULT_ACTION_NAME, Example1Action.class.getName())
            .build();

        PackageConfig defaultPackage = new PackageConfig.Builder(DEFAULT_PACKAGE_NAME)
                .namespace(DEFAULT_PACKAGE_NAMESPACE)
                .addActionConfig(ACTION1_NAME, defaultACtion)
                .build();

        ActionConfig example1Action = new ActionConfig.Builder(SECOND_PAGE_NAME, ACTION1_NAME, Example1Action.class.getName())
            .build();
        ActionConfig example2Action = new ActionConfig.Builder(SECOND_PAGE_NAME, ACTION2_NAME, Example2Action.class.getName())
            .build();

        PackageConfig secondPackage = new PackageConfig.Builder(SECOND_PAGE_NAME)
                .namespace(PACKAGE1_NAMESPACE)
                .addActionConfig(ACTION1_NAME, example1Action)
                .addActionConfig(ACTION2_NAME, example2Action)
                .build();

        configuration.addPackageConfig(DEFAULT_PACKAGE_NAME, defaultPackage);
        configuration.addPackageConfig(SECOND_PAGE_NAME, secondPackage);


        return configuration;
    }
}