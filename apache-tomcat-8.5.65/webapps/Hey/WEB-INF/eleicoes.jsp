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
<s:if test="heyBean.loggedInAsAdmin==true">
    <s:fielderror fieldName="all"/>
    <ul>
        <li>
            <s:a href="home">Início</s:a>
        </li>
        <li>
            <s:url action="listPessoas.action" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Votantes</s:a>
        </li>
        <li>
            <s:url action="listEleicoes.action" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Eleições</s:a>
        </li>
        <li>
            <s:url action="listMesas.action" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Mesas de Voto</s:a>
        </li>
        <li>
            <s:url action="votePage" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Votar</s:a>
        </li>
        <li style="float:right">
            <s:url action="logout" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}"><b>Logout</b></s:a>
        </li>
    </ul>

    <s:fielderror fieldName="eleicoes" cssStyle="padding-left: 20px; color: white;"/>
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
        <br>
        <s:form action="listListas">
            <s:hidden name="titulo" value="%{titulo}"/>
            <s:submit value="Ver Detalhes"/>
        </s:form>
        <br>
        <br>
        <br>
    </s:iterator>
    <br>
    <br>

    <h2>Criar Eleição</h2>
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

    <a id="icon" href="#" class="notification float">
        <span>Notificações</span>
        <span id="badge" class="badge">0</span>
        <iframe src="notificacoes" id="iframe" style="display: none;">
        </iframe>
    </a>
</s:if>
<s:else>

    <ul>
        <li>
            <s:a href="home">Início</s:a>
        </li>
        <li>
            <s:url action="votePage" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Votar</s:a>
        </li>
        <li>
            <s:url action="listEleicoes" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Eleições</s:a>
        </li>
        <li style="float:right">
            <s:url action="logout" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Logout</s:a>
        </li>
    </ul>

    <s:fielderror fieldName="eleicoes" cssStyle="padding-left: 20px; color: white;"/>
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
        <br>
        <s:form action="listListas">
            <s:hidden name="titulo" value="%{titulo}"/>
            <s:submit value="Ver Detalhes"/>
        </s:form>
        <br>
        <br>
    </s:iterator>
</s:else>

<script type="text/javascript">
    icon = document.getElementById("icon");
    icon.addEventListener("click", function () {
        iframe = document.getElementById("iframe");
        if (iframe.style.display == "block") {
            iframe.style.display = "none";
        } else {
            document.getElementById("badge").innerText = 0;
            iframe.style.display = "block";
        }
    });
</script>
</body>
</html>