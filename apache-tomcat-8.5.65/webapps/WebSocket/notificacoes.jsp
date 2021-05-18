<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@include file="WEB-INF/style.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Home</title>
</head>
<body>
<script type="text/javascript">

    var websocket = null;

    window.onload = function () { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://' + location.hostname + ':8081/WebSocket/ws');
        document.getElementById("chat").focus();
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
    }

    function onClose(event) {
        writeToHistory('WebSocket closed (code ' + event.code + ').');
        document.getElementById('chat').onkeydown = null;
    }

    function onMessage(message) { // print the received message
        var icon = window.parent.document.getElementById('badge');
        icon.innerText = (parseInt(icon.innerText) + 1).toString();
        var msg = JSON.parse(message.data);
        switch (msg.tipo) {
            case "mesa":
                writeToHistory("Mesa " + msg.mesa + " foi " + msg.estado + " a " + msg.data + ".");
                writeToLiveMesas(msg);
                break;
            case "eleicao":
                writeToHistory("Eleicao " + msg.nome + " " + msg.estado + " a " + msg.data + ".");
                break;
            case "utilizador":
                writeToHistory("Utilizador " + msg.nome + " " + msg.estado + " a " + msg.data + ".");
                writeToLiveUsers(msg);
                break;
            case "voto":
                writeToHistory("Votante " + msg.nome + " (" + msg.profissao + ") " + " votou na eleicao " + msg.eleicao + " ,na mesa " + msg.mesa + "a" + msg.data + ".");
                break;
            default:
                writeToHistory("Notificacao com forma desconhecida.");
                break;

        }
    }

    function onError(event) {
        console.log(event)
        writeToHistory('WebSocket error.');
        document.getElementById('chat').onkeydown = null;
    }

    function writeToHistory(text) {
        var history = document.getElementById('history');
        var line = document.createElement('p');
        line.style.wordWrap = 'break-word';
        line.innerHTML = text;
        history.appendChild(line);
        history.scrollTop = history.scrollHeight;
    }

    function writeToLiveUsers(user) {
        var table = document.getElementById('tabela');
        if (user.estado === "login") {
            if (document.getElementById(user.nome) == null) {
                var row = table.insertRow();
                row.id = user.nome;

                var cell1 = row.insertCell();
                var cell2 = row.insertCell();
                var cell3 = row.insertCell();

                cell1.innerHTML = user.nome;
                cell2.innerHTML = user.cc;
                cell3.innerHTML = user.ligacao;
            } else {
                var target = document.getElementById(user.nome);
                target.remove();
                var row = table.insertRow();
                row.id = user.nome;

                var cell1 = row.insertCell();
                var cell2 = row.insertCell();
                var cell3 = row.insertCell();

                cell1.innerHTML = user.nome;
                cell2.innerHTML = user.cc;
                cell3.innerHTML = user.ligacao;
            }
        } else {
            var target = document.getElementById(user.nome);
            target.remove();
        }
    }

    function writeToLiveMesas(msg) {
        var table = document.getElementById('tabela1');
        if (document.getElementById(msg.mesa) != null) {
            var target = document.getElementById(msg.mesa);
            target.remove();
        }
        var row = table.insertRow();
        row.id = msg.mesa;

        var cell1 = row.insertCell();
        var cell2 = row.insertCell();

        cell1.innerHTML = msg.mesa;
        cell2.innerHTML = msg.estado;
    }


</script>

<div>
    <div id="container">
        <div id="history"></div>
    </div>
</div>

<h2>Utilizadoes Ativos</h2>
<div id="users">
    <table style="width:80%" id="tabela">
        <tr>
            <th>Nome</th>
            <th>Número CC</th>
            <th>Tipo Ligação</th>
        </tr>
    </table>
</div>
<br>
<h2>Mesas de Voto</h2>
<div id="mesas">
    <table style="width:80%" id="tabela1">
        <tr>
            <th>Departamento</th>
            <th>Estado</th>
        </tr>
    </table>
</div>


</body>
</html>