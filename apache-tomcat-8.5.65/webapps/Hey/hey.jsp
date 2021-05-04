<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hey!</title>
</head>
<body>

	<h1>Pessoas Registadas</h1>

	<s:iterator value="allUsers">
		<s:property value="nome" /><br>
		<s:property value="numberCC" /> <s:date name="expireCCDate" format="dd/MM/yy" /><br>
		<s:property value="departamento" /><br>
		<s:property value="profissao" /><br>
	</s:iterator>

</body>
</html>