# struts2urlplugin
Struts2 plugin to support pattern matching in URL for action mapping

A plugin for Struts 2 that allows developers to control how URLs are mapped to their actions:

* using regular expressions;
* passing parameters to actions through the path or namespace;
* controlling the permitted HTTP methods;
* using alternate action methods;
* defining index and default actions; and
* plugging in custom pattern matchers or action selectors
* The plugin complements the default configuration as well as the Convention plugin, REST plugin, CodeBehind plugin, ZeroConfiguration and CompoundActionMapper.

**Never released for General Availability - use it as an example**
Cloned from its original site at googlecode.

# Example Configuration

The plugin enables declarative configuration of the ActionMapping and includes default configuration for common use-cases.

The following example configures an ActionMapper that matches any .action extension, allows parameters in the path to the action and enables index actions.

```xml
<!DOCTYPE action-mappings PUBLIC "-//Blue Sky Minds//DTD Struts2 URL Plugin Configuration 0.1//EN" "http://www.blueskyminds.com.au/dtd/struts-urls-0.1.dtd">

<action-mappings>
  <!--
    Add some patterns to the context so they can be reused.
    This makes the regex a little more human-readable
   -->
   <context>
     <param name="namespace">(/{0,1}.+)</param>
     <param name="action">([\w|\d|\-]*)</param>
     <param name="ext">\.action</param>
   </context>

   <!--
    Define a global filter so the ActionMapper will only process urls matching this pattern
    This ensures the ActionMapper doesn't waste cycles examining requests for unrelated resources
   -->
   <filter name="regexFilter">
     <param name="pattern">${ext}</param>
   </filter>

  <!--
  Map any URI ending with .action to an action matching the name (regex group #2) and with a package namespace
  matching the path (regex group #1).

   The namespace and action names are matched using plainText comparison first, then using
 the namedVariable matcher.
   -->
  <action-map id="default">
    <uri-patterns>
      <uri-pattern method=".*" path="^${namespace}/${action}${ext}$">   <!-- /namespace/example.action -->
        <param name="path">$1</param>
        <param name="name">$2</param>
      </uri-pattern>
      <uri-pattern method=".*" path="^/{0,1}${action}${ext}$">  <!-- /example.action | example.action -->
        <param name="path">/</param>
        <param name="name">$1</param>
      </uri-pattern>
    </uri-patterns>
    <action-selectors>
      <action namespace="${path}" name="${name}" method="execute"/>
      <action namespaceMatcher="namedVariable" namespace="${path}" actionMatcher="plainText" name="${name}" method="execute"/>
    </action-selectors>
  </action-map>

  <!-- Map to an action called index where the URL ends with a / and the HTTP method is GET -->
  <action-map id="defaultIndex">
    <uri-patterns>
      <uri-pattern method="GET" path="^${namespace}/$">   <!-- /namespace/ -->
        <param name="path">$1</param>
      </uri-pattern>
      <uri-pattern method="GET" path="^/{0,1}${action}/$">         <!-- /example/ | example/ -->
        <param name="path">$1</param>
      </uri-pattern>
    </uri-patterns>
    <action-selectors>
      <action namespace="${path}" name="index" method="execute"/>
      <action namespaceMatcher="namedVariable" namespace="${path}" actionMatcher="plainText" name="index" method="execute"/>
    </action-selectors>
  </action-map>

</action-mappings>
```
