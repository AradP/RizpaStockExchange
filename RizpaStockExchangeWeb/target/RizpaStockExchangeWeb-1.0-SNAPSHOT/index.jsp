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
<form action="HelloServlet">

  Please enter your username
  <input type="text" name="un"/><br>

  <input type="submit" value="submit">

</form>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>