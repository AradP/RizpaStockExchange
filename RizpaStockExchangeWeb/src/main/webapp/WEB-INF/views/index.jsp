<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Welcome To Rizpa Stock Exchange!" %></h1>
<br/>
<!-- This is the login part -->
<form action="servlets/HomeServlet" method="post">
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