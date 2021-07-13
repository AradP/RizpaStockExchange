<%@ page import="models.User" %>
<%@ page import="models.Stock" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Transaction" %>
<%@ page import="models.Order" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<% User loggedUser = (User) request.getAttribute("loggedUser");
%>

<% Stock selectedStock = (Stock) request.getAttribute("selectedStock");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        $(document).ready(function () {
            console.log("statrtd")
            getCurrentAmountOfStocks();
        });
        function getCurrentAmountOfStocks() {
            var symbol = <%= selectedStock.getSymbol() %>;
            var userName = <%= loggedUser %>;
            $.ajax({
                type: "GET",
                url: "/RizpaStockExchangeWeb_war/servlets/UserSingleStockServlet",
                data: {
                    stockName: symbol,
                    userName: userName,
                },
                success: function (resp) {
                    document.getElementById("currentAmountOfStocksContent").textContent = "Current amount of stocks: ${resp.responseText}";
                    alert(resp.responseText);
                    console.log(resp);
                },
                error: function (req, status, err) {
                    alert(req.responseText)
                }
            }).done(function (data) {
                console.log(data);
            });
        }
    </script>
    <title>Single Stock Page</title></head>
<body>
<a href="${pageContext.request.contextPath}/servlets/HomeServlet" class="btn btn-info btn-lg active"
   role="button">Back</a>
Stock Information Page About: <%= selectedStock.getSymbol() %>

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

<!-- Upload XML file -->
<% if ((loggedUser == null) || loggedUser.getRole().name().equals("TRADER")) { %>
<h2 id="currentAmountOfStocksContent"/>
<% } %>


<!-- Get the current logged in user -->
<% User currentUser = (User) (request.getAttribute("currentSessionUser"));%>

<!-- Selected Stock Pending Orders -->
<c:if test="currentUser.role.name().equals(`ADMIN`)">
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