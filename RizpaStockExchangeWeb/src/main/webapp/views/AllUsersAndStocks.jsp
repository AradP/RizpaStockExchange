<%@ page import="models.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Stock" %>
<%@ page import="bl.UsersSessionManager" %>
<%@ page import="bl.StockManager" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title>
    <script>
        function chooseStock(stock) {
            window.location.href = '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.innerText;
        }
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
    <form method = "post" action = "/RizpaStockExchangeWeb_war/servlets/UploadXMLFileServlet"
    enctype = "multipart/form-data" >
        <input type = "file" name = "file">
        <input type = "submit" value = "upload">
    </form >
<% } else { %>
TODO: OMER's PART
<% } %>
</body>
</html>