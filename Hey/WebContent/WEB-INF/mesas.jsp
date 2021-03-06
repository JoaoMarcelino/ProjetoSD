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
    <ul>
        <li>
            <s:a href="home">In?cio</s:a>
        </li>
        <li>
            <s:url action="listPessoas.action" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Votantes</s:a>
        </li>
        <li>
            <s:url action="listEleicoes.action" var="urlTag">
            </s:url>
            <s:a href="%{urlTag}">Elei??es</s:a>
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

    <s:fielderror fieldName="mesas" cssStyle="padding-left: 20px; color: white;"/>
    <h1>Mesas de Voto</h1>
    <s:iterator value="listMesas">
        <b><s:label value="Departamento:"/></b>
        <s:property value="departamento"/><br>
        <b><s:label value="IP Multicast:"/></b>
        <s:property value="ip"/><br>
        <b><s:label value="Port Multicast:"/></b>
        <s:property value="port"/><br>
        <b><s:label value="Estado:"/></b>
        <s:if test="status==true">
            <s:label value="Ligada"/>
        </s:if>
        <s:else>
            <s:label value="Desligada"/>
        </s:else>
        <br>
        <b><s:label value="Membros:"/></b>
        <s:iterator value="membros">
            <s:property value="nome"/>
        </s:iterator>
        <s:form action="deleteMesas">
            <s:hidden name="yourDep" value="%{departamento}"/>
            <s:submit value="Remover"/>
        </s:form>
        <br>
        <br>
    </s:iterator>
    <br>
    <br>

    <h2>Adicionar Mesa</h2>


    <s:form action="addMesas">
        <s:label value="Departamento:"/>
        <s:select label="Departamento:" list="deps" name="yourDep"/><br>
        <s:label value="IP:"/>
        <s:textfield name="ip"/><br>
        <s:label value="Port:"/>
        <s:textfield name="port"/><br>
        <s:label value="Membro #1:"/>
        <s:textfield name="membros[0]"/><br>
        <s:label value="Membro #2:"/>
        <s:textfield name="membros[1]"/><br>
        <s:label value="Membro #3:"/>
        <s:textfield name="membros[2]"/><br>
        <s:submit value="Adicionar Mesa"/>
    </s:form>

    <h2>Editar Mesa</h2>
    <s:form action="editMesas">
        <s:label value="Departamento:"/>
        <s:select label="Departamento:" list="deps" name="yourDep"/><br>
        <s:label value="Novo Membro #1:"/>
        <s:textfield name="membros[0]"/><br>
        <s:label value="Novo Membro #2:"/>
        <s:textfield name="membros[1]"/><br>
        <s:label value="Novo Membro #3:"/>
        <s:textfield name="membros[2]"/><br>
        <s:submit value="Editar Mesa"/>
    </s:form>

    <a id="icon" href="#" class="notification float">
        <span>Notifica??es</span>
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
</body>
</html>