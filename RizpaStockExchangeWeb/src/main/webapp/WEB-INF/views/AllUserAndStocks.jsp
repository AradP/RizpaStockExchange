<%@ page import="models.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Stock" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="Error.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title></head>
<body>
<!-- Users and roles table -->
<table>
    <thead>
    <tr>
        <% ArrayList<User> users = (ArrayList<User>) session.getAttribute("currentLoggedUsers");
            for (int i = 0; i < users.size(); i++) {
        %>
        <td><%= users.get(i).getName()
        %>
        </td>
        <%} %>
    </tr>
    </thead>
    <tbody>
    <tr>
        <% for (int j = 0; j < users.size(); j++) { %>
        <td><%= users.get(j).getRole() %>
        </td>
        <%} %>
    </tr>
    </tbody>
</table>

<!-- Stocks Table --->
<table>
    <thead>
    <tr>
        <% ArrayList<Stock> stocks = (ArrayList<Stock>) session.getAttribute("currentStocks");
            for (int i = 0; i < stocks.size(); i++) {
        %>
        <td><%= stocks.get(i).getSymbol()%>
        </td>
        <%} %>
    </tr>
    </thead>
    <tbody>
    <tr>
        <% for (int j = 0; j < stocks.size(); j++) { %>
        <td><%= stocks.get(j).getCompanyName() %>
        </td>
        <td><%= stocks.get(j).getPrice() %>
        </td>
        <td><%= stocks.get(j).getOrderPeriod()%>
        </td>
        <%} %>
    </tr>
    </tbody>
</table>

<!-- Get the current logged in user -->
<% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>

<!-- Upload XML file -->
<c:if test="currentUser.role.name().equals(`TRADER`)">
    <input type="file" id="upload" name="upload" style="visibility: hidden; width: 1px; height: 1px" multiple/>
    <a href="" onclick="document.getElementById('upload').click(); return false">Upload File</a>
</c:if>
</body>
</html>