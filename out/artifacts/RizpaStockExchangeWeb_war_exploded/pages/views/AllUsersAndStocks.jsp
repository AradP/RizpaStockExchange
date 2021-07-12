<%@ page import="models.User" %>
<%@ page import="constants.Constants" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title>
    <link rel="stylesheet" href="../../common/bootstrap.min.css">
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="../../common/context-path-helper.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="./../allUsersAndStocks/allUsersAndStocks.js"></script>
    <link rel="stylesheet" href="./../allUsersAndStocks/allUsersAndStocks.css">
    <script>
        function chooseStock(stock) {
            window.location.href = '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.innerText;
        }
    </script>
</head>
<body>
<div class="container">
    <% String xmlUploadMsg = (String) request.getAttribute("xmlUploadMsg");
        User loggedUser = (User) request.getSession(false).getAttribute(Constants.USERNAME);
    %>

    <% if (xmlUploadMsg != null) { %>
    <script>
        var xmlUploadMsgJs = '<%= (String) request.getAttribute("xmlUploadMsg")%>';
        window.addEventListener("load", function () {
            alert(xmlUploadMsgJs);
        })
    </script>
    <%}%>

    <h3>Logged in as <%=loggedUser.getName()%>
    </h3>

    <br>

    <h2>Welcome!</h2>
<%--    <a href="/RizpaStockExchangeWeb_war/pages/chatroom/chatroom.html">To Chat</a>--%>
    <a href="../chatroom/chatroom.html">Click here to enter the chat room</a>


    <h4>Online Users</h4>
    <br>
    <!-- place holder to show the users list -->
    <table class="styled-table">
        <thead>
        <tr>
            <td>Username</td>
            <td>Role</td>
        </tr>
        </thead>
        <tbody id="users_table">
        </tbody>
    </table>
    <br>
    <h4>Stocks</h4>
    <br>
    <!-- place holder to hold all the chat lines -->
    <table class="styled-table">
        <thead>
        <tr>
            <td>Symbol</td>
            <td>Company Name</td>
            <td>Price</td>
            <td>Order Period</td>
        </tr>
        </thead>
        <tbody id="stocks_table">
        </tbody>
    </table>

    <!-- Upload XML file -->
    <% if (loggedUser.getRole().name().equals("TRADER")) { %>
    <br>
    <h4>Upload XML file</h4>
    <form method="post" action="/RizpaStockExchangeWeb_war/servlets/UploadXMLFileServlet"
          enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="submit" value="upload">
    </form>
    <% } else { %>
    TODO: OMER's PART
    <% } %>
</div>
</body>
</html>