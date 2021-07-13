<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"
            type="text/javascript"></script>
    <script src="js/app-ajax.js" type="text/javascript"></script>
</head>
<body>
<h1><%= "Welcome To Rizpa Stock Exchange!" %></h1>S
<br/>
<!-- This is the login part -->
<form action="/RizpaStockExchangeWeb_war/servlets/HomeServlet" method="post">
  Please enter your username
  <input type="text" name="username"/><br>
<%--  <c:forEach items="${listRole}" var="role">--%>
<%--    <option value="${role.toString()}">${role.toString()}</option>--%>
<%--  </c:forEach>--%>
    <%-- TODO: get the values from the enum --%>
    <select name="role">
        <option value="ADMIN">Admin</option>
        <option value="TRADER">Trader</option>
    </select>
  <input type="submit" value="Login">
</form>
</body>
</html>