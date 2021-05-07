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

	<h1>Eleições Registadas</h1>

	<s:iterator value="heyBean.allElections">

		<s:property value="titulo" /><br>
		<s:property value="descricao" /><br>
		<s:date name="dataInicio" format="dd/MM/yy HH:mm" /><br>
		<s:date name="dataFim" format="dd/MM/yy HH:mm" /><br>
		<s:iterator value="profissoesPermitidas">
			<s:property/>
		</s:iterator><br>
		<s:iterator value="mesas">
			<s:property value="departamento"/>
			<s:property value="ip"/>
			<s:property value="port"/>
			<s:property value="status"/>
			<br>
		</s:iterator>
		<s:form action="listResultados" method="get">
			<s:hidden name="titulo" value="%{titulo}" />
			<s:submit  value="Consultar Resultados"/>
		</s:form>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>

	<h2>Criar Eleição</h2>
	<s:if test="heyBean.message!=null">
		<b><i><s:label name="heyBean.message"> </s:label></i></b>
		<br>
	</s:if>

	<s:form action="addEleicoes" method="post">
		<s:label value="Título:" /> <s:textfield name="titulo" /><br>
		<s:label value="Descrição:" /> <s:textarea name="descricao" /><br>
		<s:label value="Data de Inicio:" /> <s:textfield type="datetime-local" name="dataInicio" /><br>
		<s:label value="Data de Fim:" /> <s:textfield type="datetime-local" name="dataFim" /><br>
		<s:label value="Tipo:" /><s:select  list="profs" name="yourProf"/><br>
		<s:submit value="Criar Eleicao"/>
	</s:form>


</body>
</html>