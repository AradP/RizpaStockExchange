<%@ page import="models.User" %>
<%@ page import="models.Stock" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Transaction" %>
<%@ page import="models.Order" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Single Stock Page</title></head>
<body>
<% Stock selectedStock = (Stock) session.getAttribute("selectedStock");
%>

<!-- Selected Stock Data Table --->
<table>
    <thead>
    <tr>
        <td><%= selectedStock.getSymbol()%>
        </td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><%= selectedStock.getCompanyName() %>
        </td>
        <td><%= selectedStock.getPrice() %>
        </td>
        <td><%= selectedStock.getOrderPeriod()%>
        </td>
    </tr>
    </tbody>
</table>

<!-- Selected Stock Previous Transactions Table --->
<table>
    <thead>
    <tr>
        <% ArrayList<Transaction> transactions = selectedStock.getCompletedTransactions();
            for (int i = 0; i < transactions.size(); i++) {
        %>
        <td><%= transactions.get(i).getTimeStamp()%>
        </td>
        <%} %>
    </tr>
    </thead>
    <tbody>
    <tr>
        <% for (int j = 0; j < transactions.size(); j++) { %>
        <td><%= transactions.get(j).getAmountOfStocks() %>
        </td>
        <td><%= transactions.get(j).getPrice()%>
        </td>
        <%} %>
    </tr>
    </tbody>
</table>

<!-- Get the current logged in user -->
<% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>

<!-- Selected Stock Pending Orders -->
<c:if test="currentUser.role.name().equals(`ADMIN`)">
    <!-- Selected Stock Sell Orders Table --->
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
    <table>
        <thead>
        <tr>
            <% ArrayList<Order> buyOrders = selectedStock.getPendingBuyOrders();
                for (int i = 0; i < buyOrders.size(); i++) {
            %>
            <td><%= buyOrders.get(i).getTimestamp()%>
            </td>
            <%} %>
        </tr>
        </thead>
        <tbody>
        <tr>
            <% for (int j = 0; j < buyOrders.size(); j++) { %>
            <td><%= buyOrders.get(j).getOrderType().toString() %>
            </td>
            <td><%= buyOrders.get(j).getCount()%>
            </td>
            <td><%= buyOrders.get(j).getRequestedExchangeRate()%>
            </td>
            <td><%= buyOrders.get(j).getCreator()%>
            </td>
            <%} %>
        </tr>
        </tbody>
    </table>
</c:if>
</body>
</html>