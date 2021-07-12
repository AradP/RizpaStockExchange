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

    Logged in as <%=loggedUser.getName()%>

    <br>

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
    <!--
    Here we could give chat/logout, and the executed request was relative to this page url (=== <context path>/pages/chatroom/),
    in this option it would have come out eventually with '<context path>/pages/chatroom/chat/logout'
    (note that this option is the one used with the form above for the action of 'sendChat'...)

    Another option, just to prove that the browser takes everything relative to the current page (<context path>/pages/chatroom/),
    is to use relative path here.
    So '../../chat/logout' steps backward 2 levels from current page (putting it in the root web app, right after the context path: '/<context path>/')
    and from there assembles the rest, so we end up with <context path>/chat/logout.
    (when this request will arrive to tomcat it will strip down the context path and will expect to find a registered servlet with the mapping of /chat/logout)
    -->
    <a href="../../chat/logout">Logout</a>

    <!-- Upload XML file -->
    <% if (loggedUser.getRole().name().equals("TRADER")) { %>
    <br>
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