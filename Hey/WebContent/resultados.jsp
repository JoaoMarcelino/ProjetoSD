<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
</head>
<body>
<s:fielderror fieldName="all"/>
<s:if test="heyBean.loggedInAsAdmin==true">
    <s:url action="listPessoas" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votantes</s:a>

    <s:url action="listEleicoes" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Eleições</s:a>

    <s:url action="listMesas" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Mesas de Voto</s:a>

    <s:url action="votePage" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votar</s:a>

    <h1>Resultados de <s:property value="titulo"/></h1>


    <s:url action="votePage" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votar</s:a>

    <s:url action="listEleicoes" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Eleições</s:a>

    <s:if test="resultados!=null">
        <s:label value="Total de Votos:"/>
        <s:property value="resultados.totalVotos"/><br>
        <s:label value="Votos em Branco:"/>
        <s:property value="resultados.brancos"/><br>
        <s:label value="Votos em Nulo:"/>
        <s:property value="resultados.nulos"/><br>
        <b><s:label value="Vencedores"/></b><br>
        <s:iterator value="resultados.vencedores">
            <s:property/>
        </s:iterator><br>
        <b><s:label value="Listas"/></b><br>
        <s:iterator value="resultados.nomesListas">
            <s:property/>
        </s:iterator><br>
        <s:iterator value="resultados.resultados">
            <s:property/>
        </s:iterator><br>
    </s:if>
    <s:else>
        <b><i>Eleicao ainda não terminou.</i></b>
        <br>
    </s:else>

    <s:if test="v!=null">
        <br>
        <br>
        <b><s:label value="Nome:"/></b>
        <s:property value="v.pessoa.nome"/><br>
        <b><s:label value="Eleicao:"/></b>
        <s:property value="titulo"/><br>
        <b><s:label value="Hora de Voto:"/></b>
        <s:date name="v.data" format="dd/MM/yyyy HH:mm"/><br>
        <b><s:label value="Mesa de Voto:"/></b>
        <s:property value="mesa"/><br>
    </s:if>

    <h2>Consultar Voto Indivídual</h2>
    <s:form action="getVoto">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:textfield placeholder="Número Cartão CC" name="numeroCC"/>
        <s:submit value="Consultar Voto"/>
    </s:form>
</s:if>
</body>
</html>