var refreshRate = 2000; //milli seconds
var GET_SELECTED_STOCK_URL = buildUrlWithContextPath("getSelectedStock");

function refreshStockOrderPeriod(stock) {
    //clear all current users
    $("#order_period").text(stock.ordersPeriod);

    $("#completed_transactions_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(stock.completedTransactions || [], function(index, transaction) {
        $('<tr><td>' + transaction.timeStamp + '</td><td>' + transaction.amountOfStocks + '</td><td>' + transaction.price + '</td></tr>')
            .appendTo($("#completed_transactions_table"));
    });

    $("#pending_buy_orders_table").empty();

    $.each(stock.pendingBuyOrders || [], function(index, order) {
        $('<tr><td>' + order.timeStamp + '</td><td>' + order.orderType + '</td><td>' + order.count + '</td><td>' + order.creator + '</td></tr>')
            .appendTo($("#pending_buy_orders_table"));
    });

    $("#pending_sell_orders_table").empty();

    $.each(stock.pendingSellOrders || [], function(index, order) {
        $('<tr><td>' + order.timeStamp + '</td><td>' + order.orderType + '</td><td>' + order.count + '</td><td>' + order.creator + '</td></tr>')
            .appendTo($("#pending_sell_orders_table"));
    });
}

function ajaxGetSelectedStock() {
    $.ajax({
        url: GET_SELECTED_STOCK_URL,
        success: function(users) {
            refreshStockOrderPeriod(users);
        }
    });
}

//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxGetSelectedStock, refreshRate);
});