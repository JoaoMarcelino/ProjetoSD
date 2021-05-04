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
		<s:text name="nome" /><br>
		<s:text name="numberCC" /> <s:date name="expireCCDate" format="dd/MM/yy" /><br>
		<s:text name="departamento" /><br>
		<s:text name="profissao" /><br>
		<s:text name="morada" /><br>
		<s:text name="telefone" /><br>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>
	<h2>Registar Votante</h2>
	<s:form action="addPessoa" method="post" value="">
		<s:label value="Nome:" /> <s:textfield name="nome" /><br>
		<s:label value="Password:" /> <s:textfield type="password" name="password" /><br>
		<s:label value="Numero Cartão Cidadão:" /> <s:textfield name="numberCC" /><br>
		<s:label value="Data de Validade CC:" /> <s:textfield type="date" name="expireCCDate" /><br>
		<s:label value="Departamento:" /> <s:textfield name="departamento" /><br>
		<s:label value="Profissão:" /> <s:textfield name="profissao" /><br>
		<s:label value="Morada:" /> <s:textfield name="morada" /><br>
		<s:label value="Telefone:" /> <s:textfield name="telefone" /><br>
		<s:submit value="Registar Votante"/>
	</s:form>
</body>
</html>