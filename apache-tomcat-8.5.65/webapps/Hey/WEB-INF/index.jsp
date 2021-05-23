<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Hey!</title>
    <style type="text/css">
        .modal-content {
            background-color: #fefefe;
            margin: 5% auto 15% auto;
            /* 5% from the top, 15% from the bottom and centered */
            border: 0px solid #888;
            width: 50%;
            /* Could be more or less, depending on screen size */
        }

        .imgcontainer {
            text-align: center;
            margin: 24px 0 12px 0;
            position: relative;
        }

        .container {
            padding: 10px 0px 0px 0px;
        }

        span.psw {
            float: right;
            padding-top: 16px;
        }

        .button {
            background-color: #04AA6D;
            color: white;
            font-weight: bold;
            font-size: medium;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .LoginInput {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .fa-facebook {
            background: #3B5998;
            color: white;
        }

        .fa {
            padding: 5px 0px;
            font-size: 30px;
            width: 100%;
            text-align: center;
            text-decoration: none;
            margin: 5px 0px;
        }

        .facebookBanner {
            font-size: 15px;
            margin: 0 0 30px 0;
        }
    </style>
</head>
<body>
<s:fielderror fieldName="index"/>

<div class="modal-content">
    <s:div cssClass="modal-content">
        <h1 class="imgcontainer"><b>e-Voting UC</b></h1>
        <s:form action="login" method="post">
            <div class="container">
                <input class="LoginInput" placeholder="Numero CC" name="loginNumberCC"/><br>
                <input class="LoginInput" placeholder="Password" type="password" name="loginPassword"/><br>
                <b><s:submit cssClass="button" value="Login"/></b>
                <a href="<s:property value="heyBean.fb.loginURL"/>" class="fa fa-facebook button"></a>
            </div>
        </s:form>
    </s:div>
</div>


</body>
</html>