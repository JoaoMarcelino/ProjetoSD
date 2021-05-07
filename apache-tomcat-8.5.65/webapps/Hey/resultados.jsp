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
	<s:a href="%{urlTag}">Eleições</s:a>

	<s:url action="listMesas.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Mesas de Voto</s:a>

	<s:url action="votePage.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Votar</s:a>

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
		<b><i>Eleicao ainda não terminou.</i></b>
	</s:else>

	<s:if test="v!=null">
		<s:label value="Nome:" /><s:property value="v.pessoa.nome" /><br>
		<s:label value="Eleicao:" /><s:property value="titulo" /><br>
		<s:label value="Hora de Voto:" /><s:property value="v.data"/><br>
		<s:label value="Departamento:" />
		<c:if test="v.mesa==null">
			<s:label value="Departamento:Admin Console" />
		</c:if>
		<s:else>
			<s:label value="Departamento:" /><s:property value="v.mesa.departamento"/><br>
		</s:else>
	</s:if>

</body>
</html>