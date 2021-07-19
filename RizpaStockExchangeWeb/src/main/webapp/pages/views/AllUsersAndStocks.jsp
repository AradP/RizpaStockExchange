<%@ page import="models.User" %>
<%@ page import="constants.Constants" %>
<%@ page import="models.TradeActivity" %>
<%@ page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="../../common/context-path-helper.js"></script>
    <script src="./../allUsersAndStocks/allUsersAndStocks.js"></script>
    <link rel="stylesheet" href="./../allUsersAndStocks/allUsersAndStocks.css">
    <script>
        function chooseStock(stock) {
            window.location.href = '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.firstChild.textContent;
        }
    </script>
</head>
<body>
<div aria-live="polite" aria-atomic="true" class="position-relative">
    <div class="toast-container fixed-top top-0 end-0 p-3">
        <div class="toast" id="myToast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="mr-auto"><i class="fa fa-grav"></i> Transaction Action!</strong>
            </div>
            <div class="d-flex">
                <div id="toastBody" class="toast-body"/>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
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

    <h3 id="AccountCurrentMoney">Acount current money: 0</h3>

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

    <% } %>
</div>
</body>
</html>