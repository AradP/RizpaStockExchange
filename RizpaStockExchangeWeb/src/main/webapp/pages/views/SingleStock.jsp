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
    <title>Single Stock Page</title></head>
<body>
<div class="container">

    <% User loggedUser = (User) request.getSession(false).getAttribute(Constants.USERNAME);
        Stock selectedStock = (Stock)request.getSession(false).getAttribute("selectedStock");
    %>

    Logged in as <%=loggedUser.getName()%>

    <br>

    Stock Information Page About: <%= selectedStock.getSymbol() %>

    <br>
    <!-- Selected Stock Data Table --->
    Stock Data
    <table>
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
            <td><%= selectedStock.getOrderPeriod()%>
            </td>
        </tr>
        </tbody>
    </table>

    <!-- Selected Stock Previous Transactions Table --->
    Previous Transactions
    <table>
        <thead>
        <tr>
            <td>Date</td>
            <td>Amount</td>
            <td>Price</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <% ArrayList<Transaction> transactions = selectedStock.getCompletedTransactions();
                for (int i = 0; i < transactions.size(); i++) {
            %>
            <td><%= transactions.get(i).getTimeStamp()%>
            </td>
            <td><%= transactions.get(i).getAmountOfStocks() %>
            </td>
            <td><%= transactions.get(i).getPrice()%>
            </td>
            <%} %>
        </tr>
        </tbody>
    </table>

    <!-- Selected Stock Pending Orders -->
    <% if (loggedUser.getRole().name().equals("ADMIN")) { %>
    <!-- Selected Stock Sell Orders Table --->
    Pending Orders
    <table>
        <thead>
        <tr>
            <% ArrayList<Order> sellOrders = selectedStock.getPendingSellOrders();
                for (int i = 0; i < sellOrders.size(); i++) {
            %>
            <td><%= sellOrders.get(i).getTimestamp()%>
            </td>
            <%} %>
        </tr>
        </thead>
        <tbody>
        <tr>
            <% for (int j = 0; j < sellOrders.size(); j++) { %>
            <td><%= sellOrders.get(j).getOrderType().toString() %>
            </td>
            <td><%= sellOrders.get(j).getCount()%>
            </td>
            <td><%= sellOrders.get(j).getRequestedExchangeRate()%>
            </td>
            <td><%= sellOrders.get(j).getCreator()%>
            </td>
            <%} %>
        </tr>
        </tbody>
    </table>

    <!-- Selected Stock Buy Orders Table --->
    Buy Orders
    <table>
        <thead>
        <tr>
            <% ArrayList<Order> buyOrders = selectedStock.getPendingBuyOrders();
                for (Order order : buyOrders) {
            %>
            <td><%= order.getTimestamp()%>
            </td>
            <%} %>
        </tr>
        </thead>
        <tbody>
        <tr>
            <% for (Order buyOrder : buyOrders) { %>
            <td><%= buyOrder.getOrderType().toString() %>
            </td>
            <td><%= buyOrder.getCount()%>
            </td>
            <td><%= buyOrder.getRequestedExchangeRate()%>
            </td>
            <td><%= buyOrder.getCreator()%>
            </td>
            <%} %>
        </tr>
        </tbody>
    </table>
    <%}%>
</div>
</body>
</html>