<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>Index Result</title>
  <link rel="stylesheet" type="text/css" href="/theme.css"/>
</head>
<body>
  <p>Current example: <s:property value="name"/> </p>
  <a href="/examples/">Back to Examples Index</a><br/>
  <a href="/examples/example1/">Example 1</a><br/>
  <a href="/examples/example2/">Example 2</a><br/>
  <a href="/u001/user.action">Go to User #001</a><br/>
</body>
</html>