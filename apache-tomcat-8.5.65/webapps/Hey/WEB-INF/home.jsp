<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Home</title>
    <style type="text/css">
        .fa-facebook {
            background: #3B5998;
            color: white;
        }

        .fa {
            padding: 5px 0px;
            font-size: 30px;
            width: 10%;
            text-align: center;
            text-decoration: none;
            margin: 5px 0px;
        }

        .facebookBanner {
            font-size: 15px;
            margin: 0 0 30px 0;
        }

        .modal-content {
            text-align: center;
            background-color: #fefefe;
            margin: 5% auto 15% auto;
            /* 5% from the top, 15% from the bottom and centered */
            border: 0px solid #888;
            width: 50%;
            /* Could be more or less, depending on screen size */
        }
    </style>
</head>
<body>

<s:if test="heyBean.loggedInAsAdmin==true">

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

    <s:fielderror fieldName="home" cssStyle="padding-left: 20px; color: white;"/>
    <s:fielderror fieldName="dados" cssStyle="padding-left: 20px; color: white;"/>
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

    <s:fielderror fieldName="home" cssStyle="padding-left: 20px; color: white;"/>
    <s:fielderror fieldName="dados" cssStyle="padding-left: 20px; color: white;"/>
</s:else>

<s:div cssClass="modal-content">
    <h1>Início</h1>

    <s:if test="heyBean.fb.accessToken!=null">
        <p><b>Bem-vindo/a,<s:property value="heyBean.fb.accountName"/></b></p>
    </s:if>
    <s:else>
        <h3>Associar Conta do Facebook</h3>
        <a href="<s:property value="heyBean.fb.associationURL"/>" class="fa fa-facebook button"></a>
    </s:else>

    <h3>Atualizar dados pessoais</h3>
    <s:form action="editPessoas">
        <s:label value="Nome:"/>
        <s:textfield name="novoNome"/><br>
        <s:label value="Data de Validade CC:"/>
        <s:textfield type="date" name="novaValidade"/><br>
        <s:label value="Morada:"/>
        <s:textfield name="novaMorada"/><br>
        <s:label value="Telefone:"/>
        <s:textfield name="novoTelefone"/><br>
        <s:submit value="Guardar"/>
    </s:form>
</s:div>


<script type="text/javascript">
    icon = document.getElementById("icon");
    icon.addEventListener("click", function () {
        iframe = document.getElementById("iframe");
        if (iframe.style.display === "block") {
            iframe.style.display = "none";
        } else {
            document.getElementById("badge").innerText = 0;
            iframe.style.display = "block";
        }
    });
</script>
</body>
</html>