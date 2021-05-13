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
<script type="text/javascript">

    var websocket = null;

    window.onload = function () { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://localhost:8081/WebSocket/ws');
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
                break;
            case "eleicao":
                writeToHistory("Eleicao " + msg.nome + " " + msg.estado + " a " + msg.data + ".");
                break;
            case "utilizador":
                writeToHistory("Utilizador " + msg.nome + " " + msg.estado + " a " + msg.data + ".");
                if (msg.estado === "login")
                    writeToLiveUsers(msg, true);
                else
                    writeToLiveUsers(msg, false);
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

    function writeToLiveUsers(user, status) {
        var usrs = document.getElementById('users');
        if (status === true) {
            if (document.getElementById(user.nome) == null) {
                var line = document.createElement('p');
                line.style.wordWrap = 'break-word';
                line.innerHTML = "Nome:" + user.nome + "   Numero CC:" + user.cc + "   Tipo de Ligação:" + user.ligacao + "<br>";
                line.id = user.nome;
                usrs.appendChild(line);
                usrs.scrollTop = usrs.scrollHeight;
            }
        } else {
            var target = document.getElementById(user.nome);
            target.remove();
            usrs.scrollTop = usrs.scrollHeight;
        }

    }

</script>

<div>
    <div id="container">
        <div id="history"></div>
    </div>
</div>

<b><i>Utilizadoes Ativos</i></b>
<div>
    <div id="container1">
        <div id="users"></div>
    </div>
</div>


</body>
</html>