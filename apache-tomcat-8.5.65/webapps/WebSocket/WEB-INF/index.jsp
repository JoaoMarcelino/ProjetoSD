<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
</head>
<body>
<s:fielderror fieldName="index"/>
<h1><b>e-Voting UC</b></h1>
<s:form action="login" method="post">
    <input placeholder="Numero CC" name="loginNumberCC"/><br>
    <input placeholder="Password" type="password" name="loginPassword"/><br>
    <s:submit value="Login"/>
</s:form>

<p> <a href="<s:property value="heyBean.authUrl"/>">Login c/ Facebook</a>   </p>
</body>
</html>