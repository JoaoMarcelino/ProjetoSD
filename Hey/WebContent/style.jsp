<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Hey!</title>
    <style type="text/css">
        input#chat {
            width: 456px;
            border: 1px solid #AACAAC;
        }

        #container {
            width: 450px;
        }

        #history {
            height: 180px;
            border: 1px solid #AACAAC;
            padding: 5px;
            font-family: Courier, monospace;
            font-size: .9em;
            overflow-y: scroll;
            width: 100%;
        }

        #history p {
            margin: 0;
            padding: 0;
        }


        .notification {
            background: antiquewhite;
            color: black;
            text-decoration: none;
            padding: 15px 26px;
            position: relative;
            display: inline-block;
            border-radius: 2px;

        }

        .notification:hover {
            background: aliceblue;
        }

        .notification .badge {
            position: absolute;
            top: -10px;
            right: -10px;
            padding: 5px 10px;
            border-radius: 50%;
            background: #AACAAC;
            color: black;
        }

        .float {
            position: fixed;

            top: 40px;
            right: 40px;

            border-radius: 50px;
            text-align: center;
            box-shadow: 2px 2px 3px #999;
        }

    </style>
</head>
<body>
</body>
</html>