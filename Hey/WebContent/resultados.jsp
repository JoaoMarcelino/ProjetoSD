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
	<s:a href="%{urlTag}">Pessoas</s:a>

	<s:url action="listEleicoes.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Eleições</s:a>

	<h1>Resultados de <s:property value="titulo"/></h1>

	<s:if test="resultados!=null">
		<s:label value="Total de Votos:" /><s:property value="resultados.totalVotos" /><br>
		<s:label value="Votos em Branco:" /><s:property value="resultados.brancos" /><br>
		<s:label value="Votos em Nulo:" /><s:property value="resultados.nulos" /><br>
		<b><s:label value="Vencedores" /></b><br>
		<s:iterator value="resultados.vencedores">
			<s:property/>
		</s:iterator><br>
		<b><s:label value="Listas" /></b><br>
		<s:iterator value="resultados.nomesListas">
			<s:property/>
		</s:iterator><br>
		<s:iterator value="resultados.resultados">
			<s:property/>
		</s:iterator><br>
	</s:if>
	<s:else>
		<b><i>Eleicao não existe ou ainda não terminou.</i></b>
	</s:else>

</body>
</html>