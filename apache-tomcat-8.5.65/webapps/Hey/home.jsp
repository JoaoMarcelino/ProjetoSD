<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Home</title>
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

    <s:fielderror fieldName="home" cssStyle="padding-left: 20px; color: white;"/>
    <a id="icon" href="#" class="notification float">
        <span>Notificações</span>
        <span id="badge" class="badge">0</span>
        <iframe src="notificacoes.jsp" id="iframe" style="display: none;">
        </iframe>
    </a>
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

    <s:fielderror fieldName="home" cssStyle="padding-left: 20px; color: white;"/>
</s:else>

<h1>Home Screen</h1>
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