<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hey!</title>
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

	<s:url action="votePage.action" var="urlTag">
	</s:url>
	<s:a href="%{urlTag}">Votar</s:a>

	<h1>Listas de <s:param value="titulo"/> </h1>

	<s:iterator value="listas">
		<b><s:label value="Nome:"/></b>
		<s:property value="nome" /><br>
		<b><s:label value="Tipo de Lista:"/></b>
		<s:property value="tipoLista" /><br>
		<b><s:label value="Membros:"/></b>
		<s:iterator value="listaPessoas">
			<s:property value="nome"/>
		</s:iterator>
		<s:form action="deleteListas" method="delete">
			<s:hidden name="nome" value="%{nome}" />
			<s:hidden name="titulo" value="%{titulo}" />
			<s:submit  value="Remover"/>
		</s:form>
		<br>
		<br>
	</s:iterator>
	<br>
	<br>

	<h2>Adicionar Lista</h2>
	<s:if test="heyBean.message!=null">
		<b><i><s:label name="heyBean.message"/></i></b>
		<br>
	</s:if>

	<s:form action="addListas" method="post" id="form">
		<s:hidden name="titulo" /><br>
		<s:label value="Nome:" />
		<s:textfield name="nome" /><br>
		<s:label value="Tipo:" />
		<s:select  list="profs" name="yourProf"/><br>
		<s:label value="Número de Membros:" />
		<s:textfield id="numero" /><br>
		<s:label value="Membro #1:" />
		<s:textfield placeholder="membros#1" name="membros[0]" id="membros[0]" /><br>
		<s:submit value="Registar Votante" id="botao"/>
	</s:form>
</s:if>

<s:else>
		<s:property value="heyBean.username"/>
</s:else>

<script type="text/javascript">

	const input = document.getElementById('numero');

	input.addEventListener('change', updateValue);

	function updateValue(e) {
		var dc=document.getElementById('membros[0]');
		for(var i=0;i<input.value-1;i++){
			var itm = document.getElementById('membros[0]');
			var cln = itm.cloneNode(true);
			cln.placeholder="membros#"+parseInt(i+2);
			cln.name="membros["+parseInt(i+1)+"]";
			cln.id="membros["+parseInt(i+1)+"]";

			var tempDiv = document.createElement('br');
			var lbl = document.createElement('label');
			lbl.innerHTML = "Membro #"+parseInt(i+2)+":";
			dc.insertAdjacentElement('afterend', tempDiv);
			tempDiv.insertAdjacentElement('afterend', lbl);
			lbl.insertAdjacentElement('afterend', cln);
			dc=cln;
		}

	}

</script>
</body>
</html>