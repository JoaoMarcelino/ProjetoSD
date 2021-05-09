<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
</head>
<body>
<h1><b>e-Voting UC</b></h1>
<s:form action="login">
    <s:textfield placeholder="Número CC" name="username"/><br>
    <s:textfield placeholder="Password" name="pass"/><br>
    <s:submit value="Login"/>
    <s:url action="login" var="urlTag">
        <s:param name="username">Admin</s:param>
    </s:url>
    <s:a href="%{urlTag}">I'm an Admin</s:a>
</s:form>
</body>
</html>