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

    <s:fielderror fieldName="listas" cssStyle="padding-left: 20px; color: white;"/>
    <s:fielderror fieldName="resultados" cssStyle="padding-left: 20px; color: white;"/>
    <h1><s:property value="titulo"/></h1>
    <s:form action="listResultados">
        <s:hidden name="titulo" value="%{titulo}"/>
        <s:submit value="Consultar Resultados"/>
    </s:form>
    <br>
    <br>

    <s:iterator value="HeyBean.getEleicaoByTitulo(titulo)">
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
        </s:iterator>
        <br>
        <s:label value="Mesas Associadas:"/></b>
        <s:iterator value="mesas">
            <s:property value="departamento"/>
            <s:property value="ip"/>
            <s:property value="port"/>
            <s:property value="status"/>
            <s:form action="removeMesaEleicoes">
                <s:hidden name="departamento" value="%{departamento}"/>
                <s:hidden name="titulo" value="%{titulo}"/>
                <s:submit value="Desassociar Mesa"/>
            </s:form>
            <br>
        </s:iterator>
        <s:form action="addMesaEleicoes">
            <s:hidden name="titulo" value="%{titulo}"/>
            <s:label value="Associar Mesa:"/>
            <s:textfield name="nome" placeholder="Departamento"/>
            <s:submit value="Associar Mesa"/>
        </s:form>
        <br>
        <b><s:label value="Listas:"/></b>
        <s:iterator value="listas">
            <b><s:label value="Nome:"/></b>
            <s:property value="nome"/><br>
            <b><s:label value="Tipo de Lista:"/></b>
            <s:property value="tipoLista"/><br>
            <b><s:label value="Membros:"/></b>
            <s:iterator value="listaPessoas">
                <s:property value="nome"/>
            </s:iterator>
            <s:form action="deleteListas">
                <s:hidden name="nome" value="%{nome}"/>
                <s:hidden name="titulo" value="%{titulo}"/>
                <s:submit value="Remover"/>
            </s:form>
            <br>
            <br>
        </s:iterator>
    </s:iterator>

    <h2>Adicionar Lista</h2>
    <s:form action="addListas" id="form">
        <s:hidden name="titulo"/><br>
        <s:label value="Nome:"/>
        <s:textfield name="nome"/><br>
        <s:label value="Tipo:"/>
        <s:select list="profs" name="yourProf"/><br>
        <s:label value="Número de Membros:"/>
        <s:textfield id="numero"/><br>
        <s:label value="Membro #1:"/>
        <s:textfield placeholder="membros#1" name="membros[0]" id="membros[0]"/><br>
        <s:submit value="Adicionar Lista" id="botao"/>
    </s:form>


    <script type="text/javascript">

        const input = document.getElementById('numero');

        input.addEventListener('change', updateValue);

        function updateValue(e) {
            var dc = document.getElementById('membros[0]');
            for (var i = 0; i < input.value - 1 && i < 20; i++) {
                var itm = document.getElementById('membros[0]');
                if(document.getElementById('membros['+i+']')==null)
                    continue;
                var cln = itm.cloneNode(true);
                cln.placeholder = "membros#" + parseInt(i + 2);
                cln.name = "membros[" + parseInt(i + 1) + "]";
                cln.id = "membros[" + parseInt(i + 1) + "]";

                var tempDiv = document.createElement('br');
                var lbl = document.createElement('label');
                lbl.innerHTML = "Membro #" + parseInt(i + 2) + ":";
                dc.insertAdjacentElement('afterend', tempDiv);
                tempDiv.insertAdjacentElement('afterend', lbl);
                lbl.insertAdjacentElement('afterend', cln);
                dc = cln;
            }

        }

    </script>

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

    <s:fielderror fieldName="listas" cssStyle="padding-left: 20px; color: white;"/>
    <h1><s:property value="titulo"/></h1>

    <s:iterator value="HeyBean.getEleicaoByTitulo(titulo)">
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
        </s:iterator>
        <br>
        <b><s:label value="Mesas Associadas:"/></b>
        <s:iterator value="mesas">
            <s:property value="departamento"/>
            <s:property value="ip"/>
            <s:property value="port"/>
            <s:property value="status"/>
            <br>
        </s:iterator>
        <br>
        <b><s:label value="Listas:"/></b>
        <s:iterator value="listas">
            <b><s:label value="Nome:"/></b>
            <s:property value="nome"/><br>
            <b><s:label value="Tipo de Lista:"/></b>
            <s:property value="tipoLista"/><br>
            <b><s:label value="Membros:"/></b>
            <s:iterator value="listaPessoas">
                <s:property value="nome"/>
            </s:iterator>
            <br>
        </s:iterator>
    </s:iterator>
</s:else>

</body>
</html>