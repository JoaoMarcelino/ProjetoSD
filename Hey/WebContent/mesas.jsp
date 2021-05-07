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
	<s:url action="listPessoas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Votantes</s:a>

	<s:url action="listEleicoes.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Elei��es</s:a>

	<s:url action="listMesas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Mesas de Voto</s:a>

	<h1>Mesas de Voto</h1>
	<s:iterator value="heyBean.allMesas">
		<s:property value="departamento" /><br>
		<s:property value="ip" /><br>
		<s:property value="port" /><br>
		<s:if test="status==true">
			<s:label value="Ligada"/>
		</s:if>
		<s:else>
			<s:label value="Desligada"/>
		</s:else>
		<br>
		<s:iterator value="membros">
			<s:property value="nome" />
		</s:iterator>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>

	<h2>Adicionar Mesa</h2>
	<s:if test="heyBean.message!=null">
		<b><i><s:label name="heyBean.message"> </s:label></i></b>
		<br>
	</s:if>

	<s:form action="addMesas" method="post">
		<s:label value="Departamento:" /><s:select label="Departamento:" list="deps" name="yourDep"/><br>
		<s:label value="IP:" /> <s:textfield name="ip" /><br>
		<s:label value="Port:" /> <s:textfield name="port" /><br>
		<s:label value="Membro #1:" /> <s:textfield name="membros[0]" /><br>
		<s:label value="Membro #2:" /> <s:textfield name="membros[1]" /><br>
		<s:label value="Membro #2:" /> <s:textfield name="membros[2]" /><br>
		<s:submit value="Adicionar Mesa"/>
	</s:form>

</body>
</html>