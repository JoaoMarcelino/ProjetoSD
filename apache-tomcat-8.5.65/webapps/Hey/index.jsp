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
<h1><b>e-Voting UC</b></h1>
<s:form action="login" method="post">
    <input placeholder="Número CC" name="loginNumberCC"/><br>
    <input placeholder="Password" name="loginPassword"/><br>
    <s:submit value="Login"/>
</s:form>


<a id="icon" href="#" class="notification float" >
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
            document.getElementById("badge").innerText =0;
            iframe.style.display = "block";
        }
    });
</script>
</body>
</html>