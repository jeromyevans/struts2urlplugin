<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>Index Result</title>
  <link rel="stylesheet" type="text/css" href="/theme.css"/>
</head>
<body>
  <h1>Example Index</h1>
  <p>This example includes property values passed through the namespace of actions.</p>
  <p>It uses the standard configuration "struts-namespace-urls.xml" and defines the properties
  in struts.xml (eg /{userId}/user.action)  </p>
  <a href="/u001/user.action">Go to User #1</a><br/>
  <a href="/examples/example1/">Example 1</a><br/>
  <a href="/examples/example2/">Example 2</a><br/>
</body>
</html>