<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Prime Numbers</title>
</head>
<body>
	Prime numbers needed?"<br>
	<s:form action="server" method="post">
		<s:textfield name="serverBean.number" />
		<s:submit />
	</s:form>
</body>
</html>