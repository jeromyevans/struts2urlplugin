<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>Example Result</title>
  <link rel="stylesheet" type="text/css" href="/theme.css"/>
</head>
<body>
  <p>Current Message: <s:property value="message"/></p>

  <!-- The namespace is specified explicitly in case the current namespace is /example2 -->
  <s:form action="/example.action" namespace="/">
    <p>Execute Example1 Action (in / namespace):</p>
    <s:textfield name="message" label="New Message" value="%{'msg1'}"/>
    <s:submit name="submit" value="Submit"/>
  </s:form>

  <s:form action="/example2/example2.action">
    <p>Execute Example2 Action (in /example2 namespace):</p>
    <s:textfield name="message" label="New Message" value="%{'msg2'}"/>
    <s:submit name="submit" value="Submit"/>
  </s:form>
</body>
</html>