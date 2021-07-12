var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var STOCK_LIST_URL = buildUrlWithContextPath("stockslist");

function refreshUsersList(users) {
    //clear all current users
    $("#users_table").empty();
    
    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        $('<tr><td>' + user.name + '</td><td>' + user.role + '</td></tr>')
            .appendTo($("#users_table"));
    });
}

function refreshStocksList(stocks) {
    //clear all current users
    $("#stocks_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(stocks || [], function(index, stock) {
        // create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<tr><td class="stock-symbol" onclick="chooseStock(this)">' + stock.symbol + '</td>' +
            '<td>' + stock.companyName + '</td><td>' + stock.price + '</td><td>' + stock.ordersPeriod + '</td></tr>')
            .appendTo($("#stocks_table"));

    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxStocksList() {
    $.ajax({
        url: STOCK_LIST_URL,
        success: function(stocks) {
            refreshStocksList(stocks);
        }
    });
}

//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStocksList, refreshRate);
});

function chooseStock(stock) {
    window.location.href = '${pageContext.request.contextPath}/servlets/SingleStock?stockname=' + stock.innerText;
}