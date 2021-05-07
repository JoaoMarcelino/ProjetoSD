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
<s:if test="heyBean.username=='Admin'">
	<s:url action="listPessoas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Votantes</s:a>

	<s:url action="listEleicoes.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Eleições</s:a>

	<s:url action="listMesas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Mesas de Voto</s:a>

	<h1>Listas de <s:param value="titulo"/> </h1>

	<s:iterator value="listas">
		<s:property value="nome" /><br>
		<s:property value="tipoLista" /><br>
		<s:iterator value="listaPessoas">
			<s:param value="nome"/>
		</s:iterator>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>

	<h2>Adicionar Lista</h2>
	<s:if test="heyBean.message!=null">
		<b><i><s:label name="heyBean.message"> </s:label></i></b>
		<br>
	</s:if>

	<s:form action="addListas" method="post">
		<s:label value="Nome:" /> <s:textfield name="nome" /><br>
		<s:label value="Tipo:" /><s:select  list="profs" name="yourProf"/><br>
		<s:submit value="Registar Votante"/>
	</s:form>
</s:if>

<s:else>
		<s:property value="heyBean.username"/>
</s:else>
</body>
</html>