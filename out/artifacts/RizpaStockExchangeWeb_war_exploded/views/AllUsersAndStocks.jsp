<%@ page import="models.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Stock" %>
<%@ page import="bl.UsersSessionManager" %>
<%@ page import="bl.StockManager" %>
<%@ page import="models.TradeActivity" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script>
        function chooseStock(stock) {
            window.location.href = '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.innerText;
        }

        function toggleShowCreateNewStockSection() {
            var x = document.getElementById("newStockSection");
            if (x.style.display === "none") {
                x.style.display = "block";
            } else {
                x.style.display = "none";
            }
        }

        $(document).ready(function () {

            /* attach a submit handler to the form */
            $("#createNewStockForm").submit(function (event) {

                $.ajax({
                    type: "POST",
                    url: "/RizpaStockExchangeWeb_war/servlets/CreateNewStockServlet",
                    data: {
                        companyName: $('#CompanyNameInput').val(),
                        symbol: $('#SymbolInput').val(),
                        amountOfStocks: $('#AmountOfStocksInput').val(),
                        companyValue: $('#CompanyValueInput').val(),
                    },
                    success: function (resp) {
                        console.log(resp);
                    },
                    error: function (req, status, err) {
                        alert(req.responseText)
                    }
                }).done(function (data) {
                    console.log(data);
                    location.reload();
                });

                event.preventDefault();
            });

            /* attach a submit handler to the form */
            $("#updateMoneyForm").submit(function (event) {

                $.ajax({
                    type: "POST",
                    url: "/RizpaStockExchangeWeb_war/servlets/UserUpdateMoneyServlet",
                    data: {moneyToAdd: $('#moneyToAddInput').val()},
                    success: function (resp) {
                        console.log(resp);
                    },
                    error: function (req, status, err) {
                        console.log('Something went wrong', status, err);
                        console.log(req);
                    }
                }).done(function (data) {
                    console.log(data);
                    location.reload();
                });

                event.preventDefault();
            });
        });
    </script>
</head>
<body>

<!-- Users and roles table -->
Logged Users table
<table>
    <thead>
    <tr>
        <td>Username</td>
        <td>Role</td>
    </tr>
    </thead>
    <tbody>
    <% ArrayList<User> users = UsersSessionManager.getInstance().getUsers();%>
    <% for (User user : users) { %>
    <tr>
        <td><%= user.getName() %>
        </td>
        <td><%= user.getRole() %>
        </td>
        <%} %>
    </tr>
    </tbody>
</table>

Current Stocks in the System
<!-- Stocks Table --->
<table>
    <thead>
    <tr>
        <td>Symbol</td>
        <td>Company Name</td>
        <td>Price</td>
        <td>Order Period</td>
    </tr>
    </thead>
    <tbody>
    <% ArrayList<Stock> stocks = StockManager.getInstance().getStocks();%>
    <% for (Stock stock : stocks) { %>
    <tr class="stock-row">
        <td class="stock-symbol" onclick="chooseStock(this)"><%= stock.getSymbol() %>
        </td>
        <td><%= stock.getCompanyName() %>
        </td>
        <td><%= stock.getPrice() %>
        </td>
        <td><%= stock.getOrderPeriod() %>
        </td>
        <%} %>
    </tr>
    </tbody>
</table>

<%--<!-- Get the current logged in user -->--%>
<%--<% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>--%>


<% User loggedUser = (User) request.getAttribute("loggedUser");
    request.getServletContext().setAttribute("loggedUser", loggedUser);
%>

<!-- Upload XML file -->
<% if ((loggedUser == null) || loggedUser.getRole().name().equals("TRADER")) { %>
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
</body>
</html>