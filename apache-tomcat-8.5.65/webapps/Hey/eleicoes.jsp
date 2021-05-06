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
	<h1>Eleições Registadas</h1>

	<s:iterator value="heyBean.allElections">
		<s:property value="titulo" /><br>
		<s:property value="descricao" /><br>
		<s:date name="dataInicio" format="dd/MM/yy" /><br>
		<s:date name="dataFim" format="dd/MM/yy" /><br>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>

	<s:url action="pessoas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Pessoas</s:a>

	<s:url action="eleicoes.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Eleições</s:a>
</body>
</html>