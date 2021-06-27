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
<form action="/src/main/java/servlets/HomeServlet.java" method="post">
  Please enter your username
  <input type="text" name="username"/><br>
<%--  <c:forEach items="${listRole}" var="role">--%>
<%--    <option value="${role.name()}">${role.name()}</option>--%>
<%--  </c:forEach>--%>
  <input type="submit" value="submit">
</form>
</body>
</html>