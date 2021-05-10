<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
</head>
<body>

<s:url action="listPessoas" var="urlTag">
</s:url>
<s:a href="%{urlTag}">Votantes</s:a>

<s:url action="listEleicoes" var="urlTag">
</s:url>
<s:a href="%{urlTag}">Elei��es</s:a>

<s:url action="listMesas" var="urlTag">
</s:url>
<s:a href="%{urlTag}">Mesas de Voto</s:a>

<s:url action="votePage" var="urlTag">
</s:url>
<s:a href="%{urlTag}">Votar</s:a>

<h1>Votantes Registados</h1>

<s:iterator value="listPessoas">
    <b><s:label value="Nome:"/></b>
    <s:property value="nome"/><br>
    <b><s:label value="N�mero Cart�o de Cidad�o:"/></b>
    <s:property value="numberCC"/>
    <s:date name="expireCCDate" format="dd/MM/yy"/><br>
    <b><s:label value="Departamento:"/></b>
    <s:property value="departamento"/><br>
    <b><s:label value="Profiss�o:"/></b>
    <s:property value="profissao"/><br>
    <b><s:label value="Morada:"/></b>
    <s:property value="morada"/><br>
    <b><s:label value="Telefone:"/></b>
    <s:property value="telefone"/><br>
    <b><s:label value="isAdmin:"/></b>
    <s:property value="admin"/><br>

    <br>
    <br>
</s:iterator>
<br>
<br>

<h2>Registar Votante</h2>
<s:if test="heyBean.message!=null">
    <b><i><s:label name="heyBean.message"/></i></b>
    <br>
</s:if>

<s:form action="addPessoas">
    <s:label value="Nome:"/>
    <s:textfield name="nome"/><br>
    <s:label value="Password:"/>
    <s:textfield type="password" name="password"/><br>
    <s:label value="Numero Cart�o Cidad�o:"/>
    <s:textfield name="numberCC"/><br>
    <s:label value="Data de Validade CC:"/>
    <s:textfield type="date" name="expireCCDate"/><br>
    <s:label value="Profiss�o:"/>
    <s:select list="profs" name="yourProf"/><br>
    <s:label value="Departamento:"/>
    <s:select label="Departamento:" list="deps" name="yourDep"/><br>
    <s:label value="Morada:"/>
    <s:textfield name="morada"/><br>
    <s:label value="Telefone:"/>
    <s:textfield name="telefone"/><br>
    <s:label value="Admin:"/>
    <s:checkbox name="admin" fieldValue="true"/><br>
    <s:submit value="Registar Votante"/>
</s:form>

</body>
</html>