<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Home</title>
</head>
<body>
<s:if test="heyBean.username=='Admin'">
    <s:url action="listPessoas.action" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votantes</s:a>

    <s:url action="listEleicoes.action" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Eleições</s:a>

    <s:url action="listMesas.action" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Mesas de Voto</s:a>

    <s:url action="votePage" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Votar</s:a>

</s:if>
<s:else>
  <s:url action="votePage" var="urlTag">
  </s:url>
  <s:a href="%{urlTag}">Votar</s:a>

  <s:url action="listEleicoes" var="urlTag">
  </s:url>
  <s:a href="%{urlTag}">Eleições</s:a>
</s:else>

<h1>Home Screen</h1>
<s:if test="heyBean.message!=null">
    <b><i><s:label name="heyBean.message"> </s:label></i></b>
    <br>
</s:if>

<a id="icon" href="#" class="notification float">
    <span>Notificações</span>
    <span id="badge" class="badge">0</span>
    <iframe src="notificacoes.jsp" id="iframe" style="display: none;">
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
</body>
</html>