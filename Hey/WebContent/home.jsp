<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
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


</s:if>
<s:else>

  <s:url action="vote.action" var="urlTag">
  </s:url>
  <s:a href="%{urlTag}">Votar</s:a>

  <s:url action="" var="urlTag">
  </s:url>
  <s:a href="%{urlTag}">Eleições</s:a>
</s:else>

  <h1>Home Screen</h1>
  <s:if test="heyBean.message!=null">
    <b><i><s:label name="heyBean.message"> </s:label></i></b>
    <br>
  </s:if>
</body>
</html>