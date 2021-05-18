<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Votação</title>
</head>
<body>
<s:if test="heyBean.loggedInAsAdmin==true">
    <ul>
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

    <s:fielderror fieldName="votar" cssStyle="padding-left: 20px; color: white;"/>
    <h1>Votar</h1>

    <s:iterator value="eleicoesInfo">
        <b><s:label value="Título:"/></b>
        <s:property value="titulo"/><br>
        <b><s:label value="Descrição:"/></b>
        <s:property value="descricao"/><br>
        <b><s:label value="Data de Início:"/></b>
        <s:date name="dataInicio" format="dd/MM/yy HH:mm"/><br>
        <b><s:label value="Data de Fim:"/></b>
        <s:date name="dataFim" format="dd/MM/yy HH:mm"/><br>
        <b><s:label value="Profissões Permititdas:"/></b>
        <s:iterator value="profissoesPermitidas">
            <s:property/>,
        </s:iterator><br>
        <b><s:label value="Mesas Associadas:"/></b><br>
        <s:iterator value="mesas">
            <s:property value="departamento"/>
            <s:property value="ip"/>
            <s:property value="port"/>
            <s:property value="status"/>
            <br>
        </s:iterator><br>
        <b><s:label value="Listas:"/></b><br>
        <s:iterator value="listas">
            <s:label value="Nome:"/>
            <s:property value="nome"/><br>
            <s:label value="Tipo:"/>
            <s:property value="tipoLista"/><br>
            <s:label value="Membros:"/>
            <s:iterator value="listaPessoas">
                <s:property value="nome"/>,
            </s:iterator><br>
        </s:iterator><br>
        <br>
        <br>
    </s:iterator>

    <s:form action="votar" method="POST">
        <s:label value="Eleicao e Lista"/><br>
        <s:doubleselect name="myElection" list="eleicoes"
                        doubleName="myChoice" doubleList="getChoices(top)"/><br>
        <s:label value="Votar Antecipadamente?"/>
        <s:checkbox name="votarAntecipadamente" fieldValue="true"/><br>
        <s:submit value="Votar"/>
    </s:form>

    <a id="icon" href="#" class="notification float">
        <span>Notificações</span>
        <span id="badge" class="badge">0</span>
        <iframe src="notificacoes" id="iframe" style="display: none;">
        </iframe>
    </a>
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
</s:if>
<s:else>
    <ul>
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

    <s:fielderror fieldName="votar" cssStyle="padding-left: 20px; color: white;"/>
    <h1>Votar</h1>
    <s:iterator value="eleicoesInfo">
        <b><s:label value="Título:"/></b>
        <s:property value="titulo"/><br>
        <b><s:label value="Descrição:"/></b>
        <s:property value="descricao"/><br>
        <b><s:label value="Data de Início:"/></b>
        <s:date name="dataInicio" format="dd/MM/yy HH:mm"/><br>
        <b><s:label value="Data de Fim:"/></b>
        <s:date name="dataFim" format="dd/MM/yy HH:mm"/><br>
        <b><s:label value="Profissões Permititdas:"/></b>
        <s:iterator value="profissoesPermitidas">
            <s:property/>,
        </s:iterator><br>
        <b><s:label value="Mesas Associadas:"/></b><br>
        <s:iterator value="mesas">
            <s:property value="departamento"/>
            <s:property value="ip"/>
            <s:property value="port"/>
            <s:property value="status"/>
            <br>
        </s:iterator><br>
        <b><s:label value="Listas:"/></b><br>
        <s:iterator value="listas">
            <s:label value="Nome:"/>
            <s:property value="nome"/><br>
            <s:label value="Tipo:"/>
            <s:property value="tipoLista"/><br>
            <s:label value="Membros:"/>
            <s:iterator value="listaPessoas">
                <s:property value="nome"/>,
            </s:iterator><br>
        </s:iterator><br>
        <br>
        <br>
    </s:iterator>

    <s:form action="votar">
        <s:label value="Eleicao e Lista"/>
        <s:doubleselect label="Eleição e Lista" name="myElection" list="eleicoes"
                        doubleName="myChoice" doubleList="getChoices(top)"/><br>
        <s:submit value="Votar"/>
    </s:form>
</s:else>

</body>
</html>