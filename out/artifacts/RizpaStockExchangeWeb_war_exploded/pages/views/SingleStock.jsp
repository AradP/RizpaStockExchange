<%@ page import="models.User" %>
<%@ page import="models.Stock" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Transaction" %>
<%@ page import="models.Order" %>
<%@ page import="constants.Constants" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Single Stock Page</title>
    <link rel="stylesheet" href="../../common/bootstrap.min.css">
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="../../common/context-path-helper.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="./../singleStock/singleStock.js"></script>
</head>
<body>
<div class="container">
    <% User loggedUser = (User) request.getSession(false).getAttribute(Constants.USERNAME);
        Stock selectedStock = (Stock) request.getSession(false).getAttribute("selectedStock");
    %>

    <h3>Logged in as <%=loggedUser.getName()%></h3>

    <br>

    <h2>Stock Information Page About: <%= selectedStock.getSymbol() %></h2>

    <br>
    <!-- Selected Stock Data Table --->
    <h4>Stock Data</h4>
    <table class="styled-table">
        <thead>
        </thead>
        <tbody>
        <tr>
            <td>Company</td>
            <td><%= selectedStock.getCompanyName() %>
            </td>
        </tr>
        <tr>
            <td>Price</td>
            <td><%= selectedStock.getPrice() %>
            </td>
        </tr>
        <tr>
            <td>Order Period</td>
            <td id="order_period">
            </td>
        </tr>
        </tbody>
    </table>

    <br>
    <br>
    <!-- Selected Stock Previous Transactions Table --->
    <h4>Previous Transactions</h4>
    <br>
    <table class="styled-table">
        <thead>
        <tr>
            <td>Date</td>
            <td>Amount</td>
            <td>Price</td>
        </tr>
        </thead>
        <tbody id="completed_transactions_table">
<%--        <tr>--%>
<%--            <% ArrayList<Transaction> transactions = selectedStock.getCompletedTransactions();--%>
<%--                for (int i = 0; i < transactions.size(); i++) {--%>
<%--            %>--%>
<%--            <td><%= transactions.get(i).getTimeStamp()%>--%>
<%--            </td>--%>
<%--            <td><%= transactions.get(i).getAmountOfStocks() %>--%>
<%--            </td>--%>
<%--            <td><%= transactions.get(i).getPrice()%>--%>
<%--            </td>--%>
<%--            <%} %>--%>
<%--        </tr>--%>
        </tbody>
    </table>

    <!-- Selected Stock Pending Orders -->
    <% if (loggedUser.getRole().name().equals("ADMIN")) { %>
    <!-- Selected Stock Sell Orders Table --->
    <h4>Pending Buy Orders</h4>
    <table class="styled-table">
        <thead>
        <tr>
            <td>Date</td>
            <td>Order Type</td>
            <td>Amount</td>
            <td>Requested Exchange Rate</td>
            <td>Creator</td>
<%--            <% ArrayList<Order> sellOrders = selectedStock.getPendingSellOrders();--%>
<%--                for (int i = 0; i < sellOrders.size(); i++) {--%>
<%--            %>--%>
<%--            <td><%= sellOrders.get(i).getTimestamp()%>--%>
<%--            </td>--%>
<%--            <%} %>--%>
        </tr>
        </thead>
        <tbody  id="pendingBuyOrders_table">
<%--        <tr>--%>
<%--            <% for (int j = 0; j < sellOrders.size(); j++) { %>--%>
<%--            <td><%= sellOrders.get(j).getOrderType().toString() %>--%>
<%--            </td>--%>
<%--            <td><%= sellOrders.get(j).getCount()%>--%>
<%--            </td>--%>
<%--            <td><%= sellOrders.get(j).getRequestedExchangeRate()%>--%>
<%--            </td>--%>
<%--            <td><%= sellOrders.get(j).getCreator()%>--%>
<%--            </td>--%>
<%--            <%} %>--%>
<%--        </tr>--%>
        </tbody>
    </table>

    <!-- Selected Stock Buy Orders Table --->
    <h4>Pending Sell Orders</h4>
    <table class="styled-table">
        <thead>
        <tr>
            <td>Date</td>
            <td>Order Type</td>
            <td>Amount</td>
            <td>Requested Exchange Rate</td>
            <td>Creator</td>
        </tr>
        </thead>
        <tbody  id="pendingSellOrders_table">
        </tbody>
    </table>
    <%}%>
</div>
</body>
</html>