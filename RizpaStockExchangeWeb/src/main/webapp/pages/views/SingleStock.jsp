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
    <script src="../../common/context-path-helper.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="./../singleStock/singleStock.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
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
    <% User loggedUser = (User) request.getSession(false).getAttribute(Constants.USERNAME);
        Stock selectedStock = (Stock) request.getSession(false).getAttribute("selectedStock");
    %>
    <a href="/RizpaStockExchangeWeb_war/pages/views/AllUsersAndStocks.jsp"
       class="btn btn-primary" role="button">Back</a>
    <h3>Logged in as <%=loggedUser.getName()%>
    </h3>

    <br>

    <h2>Stock Information Page About: <%= selectedStock.getSymbol() %>
    </h2>

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
    <% if (loggedUser.getRole().name().equals("TRADER")) { %>
    <h2 id="currentAmountOfStocksContent"/>
    <%}%>
</div>
<% if (loggedUser.getRole().name().equals("TRADER")) { %>
<div>
    <form id="formOrderType">
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" class="form-check-input" id="radioNone" name="orderTypeGroup" value="None" checked>None
            </label>
        </div>
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" class="form-check-input" id="radioFOK" name="orderTypeGroup" value="FOK">FOK
            </label>
        </div>
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" class="form-check-input" id="radioIOC" name="orderTypeGroup" value="IOC">IOC
            </label>
        </div>
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" class="form-check-input" id="radioLMT" name="orderTypeGroup" value="LMT">LMT
            </label>
        </div>
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" class="form-check-input" id="radioMKT" name="orderTypeGroup" value="MKT">MKT
            </label>
        </div>
    </form>

    <form id="TransactionActivityForm" name="TransactionActivityForm" style="display: none">
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" id="radioFOKSell" class="form-check-input" value="Sell" name="isSellRadioGroup"
                       checked>Sell
            </label>
        </div>
        <div class="form-check-inline">
            <label class="form-check-label">
                <input type="radio" id="radioFOKBuy" class="form-check-input" value="Buy" name="isSellRadioGroup">Buy
            </label>
        </div>
        <label for="numberOfStocks">Number of stocks</label>
        <input required type="number" id="numberOfStocks" name="numberOfStocks" min="1"/><br>
        <label for="lowestPrice">Lowest price</label>
        <input type="number" id="lowestPrice" name="lowestPrice" min="1"/><br>
        <label for="highestPrice">Highest price</label>
        <input type="number" id="highestPrice" name="highestPrice" min="1"/><br>
        <input type="submit" value="Create a transaction">
    </form>
</div>
<%}%>
<!-- Selected Stock Pending Orders -->
<% if (loggedUser.getRole().name().equals("ADMIN")) { %>
<div>
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
        </tr>
        </thead>
        <tbody id="pendingBuyOrders_table">
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
        <tbody id="pendingSellOrders_table">
        </tbody>
    </table>
</div>
<%}%>
<div id="chart_div"></div>
</body>
</html>