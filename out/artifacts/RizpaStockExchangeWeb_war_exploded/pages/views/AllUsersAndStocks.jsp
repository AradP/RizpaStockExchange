<%@ page import="models.User" %>
<%@ page import="constants.Constants" %>
<%@ page import="models.TradeActivity" %>
<%@ page import="java.util.ArrayList" %>
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
<div class="toast" id="myToast" style="position: absolute; top: 0; right: 0;">
    <div class="toast-header">
        <strong class="mr-auto"><i class="fa fa-grav"></i> Transaction Action!</strong>
        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div id="toastBody" class="toast-body"/>
</div>
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
    <form method="post" action="/RizpaStockExchangeWeb_war/servlets/UploadXMLFileServlet"
          enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="submit" value="upload">
    </form>

    <h3> Acount current money: <%= loggedUser.getCurrentMoney() %>
    </h3>

    <form id="updateMoneyForm" name="updateMoneyForm">
        <label for="moneyToAddInput">Money to add (more or equal to 1)</label>
        <input type="number" id="moneyToAddInput" name="moneyToAddInput" min="1"/><br>
        <input type="submit" value="Add">
    </form>

    Trade activities
    <!-- Trade activities Table --->
    <table>
        <thead>
        <tr>
            <td>Time stamp</td>
            <td>Symbol</td>
            <td>Type</td>
            <td>Price</td>
            <td>Money before</td>
            <td>Money after</td>
        </tr>
        </thead>
        <tbody>
        <% ArrayList<TradeActivity> activities = loggedUser.getTradeActivities();%>
        <% for (TradeActivity activity : activities) { %>
        <tr class="activity-row">
            <td><%= activity.getTimeStamp() %>
            </td>
            <td><%= activity.getSymbol() %>
            </td>
            <td><%= activity.getTradeActivityTypeAsString() %>
            </td>
            <td><%= activity.getPrice() %>
            </td>
            <td><%= activity.getMoneyLeftBefore() %>
            </td>
            <td><%= activity.getMoneyLeftAfter() %>
            </td>
            <%} %>
        </tr>
        </tbody>
    </table>

    <button onclick="toggleShowCreateNewStockSection()">Create new stock</button>

    <div id="newStockSection" style="display: none">
        <form id="createNewStockForm">
            <label for="CompanyNameInput">Company name</label>
            <input type="text" id="CompanyNameInput" name="CompanyNameInput" minlength="1"/><br>
            <label for="SymbolInput">Symbol</label>
            <input type="text" id="SymbolInput" name="SymbolInput" minlength="1"/><br>
            <label for="AmountOfStocksInput">Amount of stocks</label>
            <input type="number" id="AmountOfStocksInput" name="AmountOfStocksInput" minlength="1"/><br>
            <label for="CompanyValueInput">Company value</label>
            <input type="number" id="CompanyValueInput" name="CompanyValueInput" minlength="1"/><br>
            <input type="submit" value="Create">
        </form>
    </div>

    <% } else { %>
    TODO: OMER's PART
    <% } %>
</div>
</body>
</html>