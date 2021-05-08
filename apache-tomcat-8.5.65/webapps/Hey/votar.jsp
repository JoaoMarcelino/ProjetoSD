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

<h1>Votar Antecipadamente</h1>
<s:if test="heyBean.message!=null">
	<b><i><s:label name="heyBean.message"> </s:label></i></b>
	<br>
</s:if>
<s:iterator value="eleicoesDisponiveis">
	<b><s:label value="Título:"/></b>
	<s:property value="titulo" /><br>
	<b><s:label value="Descrição:"/></b>
	<s:property value="descricao" /><br>
	<b><s:label value="Data de Início:"/></b>
	<s:date name="dataInicio" format="dd/MM/yy HH:mm" /><br>
	<b><s:label value="Data de Fim:"/></b>
	<s:date name="dataFim" format="dd/MM/yy HH:mm" /><br>
	<b><s:label value="Profissões Permititdas:"/></b>
	<s:iterator value="profissoesPermitidas">
		<s:property/>
	</s:iterator><br>
	<b><s:label value="Mesas Associadas:"/></b>
	<s:iterator value="mesas">
		<s:property value="departamento"/>
		<s:property value="ip"/>
		<s:property value="port"/>
		<s:property value="status"/>
		<br>
	</s:iterator><br>
	<b><s:label value="Listas:"/></b>
	<s:iterator value="listas">
		<s:label value="Nome:"/>
		<s:property value="nome" /><br>
		<s:label value="Tipo:"/>
		<s:property value="tipoLista" /><br>
		<s:label value="Listas:"/>
		<s:iterator value="listaPessoas">
			<s:property value="nome"/>
		</s:iterator><br>
	</s:iterator><br>
	<br>
	<br>
</s:iterator>
<br>
<br>

<s:form action="votoAntecipado" method="votarAntecipado">
	<s:label value="Número CC:" />
	<s:textfield name="numero" /><br>
	<s:label value="Password:" />
	<s:password name="pass" /><br>
	<s:label value="Nome da Eleicao:" />
	<s:textfield name="titulo" /><br>
	<s:label value="Tipo de Voto:" />
	<s:select  list="choices" name="myChoice"/><br>
	<s:label value="Lista:" />
	<s:textfield name="nome" /><br>
	<s:submit value="Votar"/>
</s:form>

</body>
</html>