<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>User Details Result</title>
  <link rel="stylesheet" type="text/css" href="/theme.css"/>
</head>
<body>  
  <p>The current userId is <s:property value="userId"/>.</p>
  <a href="/u001/user.action">User #1</a><br/>
  <a href="/u002/user.action">User #2</a><br/>
  <a href="/u003/user.action">User #3</a><br/>
  <a href="/u004/user.action">User #4</a><br/>
  <a href="/u005/user.action">User #5</a><br/>
  <a href="/examples/">Go to Examples Index</a>
</body>
</html>