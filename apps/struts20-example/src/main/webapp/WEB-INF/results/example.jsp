<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>Example Result</title>
  <link rel="stylesheet" type="text/css" href="/theme.css"/>
</head>
<body>
  <a href="/examples/">Back to Example Index</a>
  <p>Current Message: <s:property value="message"/></p>

  <!-- The namespace is specified explicitly in case the current namespace is /example2 -->
  <s:form action="/examples/actualExample.action">
    <p>Execute ActualExample Action (in /examples namespace):</p>
    <s:textfield name="message" label="New Message" value="%{'msg1'}"/>
    <s:submit name="submit" value="Submit"/>
  </s:form>

</body>
</html>