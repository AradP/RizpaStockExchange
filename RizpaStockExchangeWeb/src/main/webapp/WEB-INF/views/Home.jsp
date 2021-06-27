<%@ page import="models.User" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Home Page</title></head>
<body>
<center>
<%--    <% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>--%>

    Welcome <%= request.getParameter("username")%>
</center>
</body>
</html>