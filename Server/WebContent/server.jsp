<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Prime Numbers</title>
</head>
<body>

	<c:choose>
		<c:when test="${serverBean.number == 1}">
			<p>Here is the first prime number.</p>
		</c:when>
		<c:when test="${serverBean.number > 1}">
			<p>Here are the first <c:out value="${serverBean.number}"/> prime numbers.</p>
		</c:when>
		<c:otherwise>
			<p>Are you trying to hack into the system?</p>
		</c:otherwise>
	</c:choose>

	<c:forEach items="${serverBean.primes}" var="value">
		<c:out value="${value}" /><br>
	</c:forEach>

	<p><a href="<s:url action="index" />">Start</a></p>

</body>
</html>