<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ page import="utils.SessionUtils" %>
    <%@ page import="constants.Constants" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Chat</title>
        <link rel="stylesheet" href="../../common/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    </head>
    <body>
        <div class="container">
            <% String usernameFromSession = SessionUtils.getUsername(request);%>
            <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
            <% if (usernameFromSession == null) {%>
            <h1>Welcome to the Online Chat</h1>
            <br/>
            <h2>Please enter a unique user name:</h2>
            <form method="GET" action="login">
                <input type="text" name="<%=Constants.USERNAME%>" value="<%=usernameFromParameter%>"/>
                <input type="submit" value="Login"/>
            </form>
            <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
            <% if (errorMessage != null) {%>
            <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
            <% } %>
            <% } else {%>
            <h1>Welcome back, <%=usernameFromSession%></h1>
            <a href="../chatroom/chatroom.html">Click here to enter the chat room</a>
            <br/>
            <a href="login?logout=true" id="logout">logout</a>
            <% }%>
        </div>
    </body>
</html>