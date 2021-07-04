<%@ page import="models.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Stock" %>
<%@ page import="bl.UsersSessionManager" %>
<%@ page import="bl.StockManager" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Users And Stocks Page</title>
    <script>
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


Welcome ${cookie['username'].value}
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
        <td>Company Name</td>
        <td>Symbol</td>
        <td>Price</td>
        <td>Order Period</td>
    </tr>
    </thead>
    <tbody>
    <% ArrayList<Stock> stocks = StockManager.getInstance().getStocks();%>
    <% for (Stock stock : stocks) { %>
    <tr>
        <td><%= stock.getCompanyName() %>
        </td>
        <td><%= stock.getSymbol() %>
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
    <form method="post" action="servlets/UploadXMLFileServlet" enctype="multipart/form-data">
        <input type="file" name="file" />
        <input type="submit" value="upload" />
    </form>

<input id="ajaxfile" type="file"/> <br/>
<button onclick="uploadFile()"> Upload </button>

<!-- Ajax to Java File Upload Logic -->
<script>
    async function uploadFile() {
        let formData = new FormData();
        formData.append("file", ajaxfile.files[0]);
        await fetch('servlets/UploadXMLFileServlet', {
            method: "POST",
            body: formData
        });
        alert('The file upload with Ajax and Java was a success!');
    }
</script>
<%--</c:if>--%>
</body>
</html>