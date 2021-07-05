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
        // $(document).ready(function () {
        //     $('.stock-row').click(function () {
        //         var stockSymbolChosen = $(this).closest(".tr").find('.stock-symbol').text();
        //         alert("Chosen stock: " + stockSymbolChosen);
        //
        //         $.post("SingleStock.jsp", {
        //             stockSymbolChosen: stockSymbolChosen
        //         });
        //     });
        // });

        function chooseStock(stock) {
            alert("Stock Chosen: " + stock.innerText);

            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    var data = xhr.responseText;
                    alert("AAA: " + data);
                }
            }
            xhr.open('GET', '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.innerText, true);
            xhr.send(null);
        }

        function chooseFile() {
            document.getElementById('upload')
        }
    </script>
</head>
<body>

<%--<%--%>
<%--    Cookie[] cookies = null;--%>

<%--    // Get an array of Cookies associated with the this domain--%>
<%--    cookies = request.getCookies();--%>
<%--%>--%>
<%--<% for (Cookie cookie : cookies) { %>--%>
<%--Name: <%= cookie.getName() %>--%>
<%--value: <%= cookie.getValue() %>--%>
<%--<%--%>
<%--    }--%>
<%--%>--%>

<br>

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

<!-- Upload XML file -->
<%--<c:if test="currentUser.role.name().equals(`TRADER`)">--%>
<form method="post" action="/RizpaStockExchangeWeb_war/servlets/UploadXMLFileServlet" enctype="multipart/form-data">
    <input type="file" name="file"/>
    <input type="submit" value="upload"/>
</form>
<%--</c:if>--%>
</body>
</html>