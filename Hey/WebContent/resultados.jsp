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
<s:fielderror fieldName="resultados"/>
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

    <s:url action="logout" var="urlTag">
    </s:url>
    <s:a href="%{urlTag}">Logout</s:a>

    <h1>Resultados de <span id="titulo"><s:property value="titulo"/></span></h1>

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

    <div id="eleicao">
        <table style="width:80%" id="tabela">
        </table>
    </div>

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


<script type="text/javascript">

    var websocket = null;

    window.onload = function () { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://' + location.hostname + ':8081/WebSocket/ws');
    }

    function connect(host) { // connect to the host websocket
        if ('WebSocket' in window)
            websocket = new WebSocket(host);
        else if ('MozWebSocket' in window)
            websocket = new MozWebSocket(host);
        else {
            writeToHistory('Get a real browser which supports WebSocket.');
            return;
        }

        websocket.onopen = onOpen; // set the 4 event listeners below
        websocket.onclose = onClose;
        websocket.onmessage = onMessage;
        websocket.onerror = onError;
    }

    function onOpen(event) {
        var deps = ["Web", "DA", "DCT", "DCV", "DEC", "DEEC", "DEI", "DEM", "DEQ", "DF", "DM", "DQ"];
        var profs = ["Estudante", "Docente", "Funcionário"];

        var table = document.getElementById('tabela');
        var header = table.createTHead();
        var row = header.insertRow();
        var cell = row.insertCell();

        for (var i = 0; i < profs.length; i++) {
            var cell = row.insertCell();
            cell.innerHTML = "<b>" + profs[i] + "</b>";
        }

        for (var i = 0; i < deps.length; i++) {
            var row = table.insertRow();
            var cell = row.insertCell();
            cell.innerHTML = deps[i];
            for (var j = 0; j < profs.length; j++) {
                var cell = row.insertCell();
                cell.innerHTML = 0;
                cell.id = deps[i] + profs[j];
            }
        }
        var titulo = document.getElementById('titulo').innerText;
        websocket.send(titulo);
    }

    function onClose(event) {
    }

    function onMessage(message) { // print the received message
        console.log(message);
        var msg = JSON.parse(message.data);
        switch (msg.tipo) {
            case "voto":
                fillTable(msg);
                break;
            default:
                break;

        }
    }

    function onError(event) {
        console.log(event)
    }

    function fillTable(msg) {
        console.log(msg);
        if (document.getElementById('titulo').innerText === msg.eleicao) {
            var dep = msg.mesa;
            var prof = msg.profissao;
            var celula = document.getElementById(dep + prof);
            celula.innerHTML = parseInt(celula.innerHTML) + 1;
        }
    }

</script>
</body>
</html>