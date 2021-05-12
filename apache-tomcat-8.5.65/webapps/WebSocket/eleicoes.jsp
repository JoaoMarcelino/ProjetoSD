<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
</head>
<body>
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

<h1>Eleições Registadas</h1>

<s:iterator value="listEleicoes">
    <b><s:label value="Título:"/></b>
    <s:property value="titulo"/><br>
    <b><s:label value="Descrição:"/></b>
    <s:property value="descricao"/><br>
    <b><s:label value="Data de Início:"/></b>
    <s:date name="dataInicio" format="dd/MM/yy HH:mm"/><br>
    <b><s:label value="Data de Fim:"/></b>
    <s:date name="dataFim" format="dd/MM/yy HH:mm"/><br>
    <b><s:label value="Profissões Permitidas:"/></b>
    <s:iterator value="profissoesPermitidas">
        <s:property/>
    </s:iterator><br>
    <b><s:label value="Mesas Associadas:"/></b>
    <br>
    <s:iterator value="mesas">
        <s:property value="departamento"/>
        <s:property value="ip"/>
        <s:property value="port"/>
        <s:property value="status"/>
        <s:form action="removeMesaEleicoes">
            <s:hidden name="departamento" value="%{departamento}"/>
            <s:hidden name="titulo" value="%{titulo}"/>
            <s:submit value="Desassociar Mesa"/>
        </s:form>
        <br>
    </s:iterator>
    <s:form action="listListas">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:submit value="Listas"/>
    </s:form>
    <br>
    <s:form action="listResultados">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:submit value="Consultar Resultados"/>
    </s:form>
    <br>
    <s:form action="addMesaEleicoes">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:label value="Associar Mesa:"/>
        <s:textfield name="nome" placeholder="Departamento"/>
        <s:submit value="Associar Mesa"/>
    </s:form>
    <br>
    <br>
</s:iterator>
<br>
<br>

<h2>Criar Eleição</h2>
<s:if test="heyBean.message!=null">
    <b><i><s:label name="heyBean.message"/></i></b>
    <br>
</s:if>

<s:form action="addEleicoes">
    <s:label value="Título:"/>
    <s:textfield name="titulo"/><br>
    <s:label value="Descrição:"/>
    <s:textarea name="descricao"/><br>
    <s:label value="Data de Inicio:"/>
    <s:textfield type="datetime-local" name="dataInicio"/><br>
    <s:label value="Data de Fim:"/>
    <s:textfield type="datetime-local" name="dataFim"/><br>
    <s:label value="Tipo:"/>
    <s:select list="profs" name="yourProf"/><br>
    <s:submit value="Criar Eleicao"/>
</s:form>


<h2>Alterar Dados de Eleição</h2>
<s:form action="editEleicoes">
    <s:label value="Título Antigo:"/>
    <s:textfield name="titulo"/><br>
    <s:label value="Título Novo:"/>
    <s:textfield name="tituloNovo"/><br>
    <s:label value="Descrição Nova:"/>
    <s:textarea name="descricaoNova"/><br>
    <s:label value="Data de Inicio Nova:"/>
    <s:textfield type="datetime-local" name="dataInicioNova"/><br>
    <s:label value="Data de Fim Nova:"/>
    <s:textfield type="datetime-local" name="dataFimNova"/><br>
    <s:submit value="Editar Eleicao"/>
</s:form>

</s:if>
<s:else>

    <s:url action="votePage" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votar</s:a>

    <s:url action="listEleicoes" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Eleições</s:a>

    <s:iterator value="listEleicoes">
    <b><s:label value="Título:"/></b>
    <s:property value="titulo"/><br>
    <b><s:label value="Descrição:"/></b>
    <s:property value="descricao"/><br>
    <b><s:label value="Data de Início:"/></b>
    <s:date name="dataInicio" format="dd/MM/yy HH:mm"/><br>
    <b><s:label value="Data de Fim:"/></b>
    <s:date name="dataFim" format="dd/MM/yy HH:mm"/><br>
    <b><s:label value="Profissões Permitidas:"/></b>
    <s:iterator value="profissoesPermitidas">
        <s:property/>
    </s:iterator><br>
    <b><s:label value="Mesas Associadas:"/></b>
    <br>
    <s:iterator value="mesas">
    <s:property value="departamento"/>
    <s:property value="ip"/>
    <s:property value="port"/>
    <s:property value="status"/>
    </s:iterator>
    <s:form action="listListas">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:submit value="Listas"/>
    </s:form>
    <br>
    <s:form action="listResultados">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:submit value="Consultar Resultados"/>
    </s:form>
    <br>
    </s:iterator>
</s:else>

	</body>
</html>