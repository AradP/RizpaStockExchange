var refreshRate = 200; //milli seconds
var GET_SELECTED_STOCK_URL = buildUrlWithContextPath("getSelectedStock");
var selectedStockSymbol;
var maxAmountOfStocksForSell;

function createTransactionAction(arg) {
    $.ajax({
        type: "POST",
        url: "/RizpaStockExchangeWeb_war/servlets/TransactionActionServlet",
        data: arg,
        success: function (resp) {
            if (resp != "0" && resp != "") {
                document.getElementById("toastBody").innerHTML = resp;
                $("#myToast").toast("show");
            }
        },
    }).done(function (data) {
        console.log(data);
    });
}


function refreshStockOrderPeriod(stock) {
    selectedStockSymbol = stock.symbol;
    $.ajax({
        type: "GET",
        url: "/RizpaStockExchangeWeb_war/servlets/UserSingleStockServlet",
        data: {
            stockName: stock.symbol,
        },
        success: function (resp) {
            document.getElementById("currentAmountOfStocksContent").textContent = "Current amount of stocks:" + resp;
            maxAmountOfStocksForSell = resp;
        },
        error: function (req, status, err) {
            alert(req.responseText)
        }
    }).done(function (data) {
        console.log(data);
    });

    $.ajax({
        type: "GET",
        url: "/RizpaStockExchangeWeb_war/servlets/TransactionUpdateAlertServlet",
        success: function (resp) {
            if (resp != "0" && resp != "") {
                document.getElementById("toastBody").innerHTML = resp;
                $("#myToast").toast("show");
            }
        }
    });

    //clear all current users
    $("#order_period").text(stock.ordersPeriod);

    $("#completed_transactions_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(stock.completedTransactions || [], function (index, transaction) {
        $('<tr><td>' + transaction.timeStamp + '</td><td>' + transaction.amountOfStocks + '</td><td>' + transaction.price + '</td></tr>')
            .appendTo($("#completed_transactions_table"));
    });

    $("#pending_buy_orders_table").empty();

    $.each(stock.pendingBuyOrders || [], function (index, order) {
        $('<tr><td>' + order.timeStamp + '</td><td>' + order.orderType + '</td><td>' + order.count + '</td><td>' + order.creator.name + '</td></tr>')
            .appendTo($("#pending_buy_orders_table"));
    });

    $("#pending_sell_orders_table").empty();

    $.each(stock.pendingSellOrders || [], function (index, order) {
        $('<tr><td>' + order.timeStamp + '</td><td>' + order.orderType + '</td><td>' + order.count + '</td><td>' + order.creator.name + '</td></tr>')
            .appendTo($("#pending_sell_orders_table"));
    });
}

function ajaxGetSelectedStock() {
    $.ajax({
        url: GET_SELECTED_STOCK_URL,
        success: function (users) {
            refreshStockOrderPeriod(users);
        }
    });
}

function showTransactionActivityForm() {
    var x = document.getElementById("TransactionActivityForm");
    x.style.display = "block";
}

function hideTransactionActivityForm() {
    var x = document.getElementById("TransactionActivityForm");
    x.style.display = "none";
}

//activate the timer calls after the page is loaded
$(function () {
    //The users list is refreshed automatically every second
    setInterval(ajaxGetSelectedStock, refreshRate);


    /* attach a submit handler to the form */
    $('input[type=radio][name=orderTypeGroup]').change(function () {
        var x = document.getElementById("TransactionActivityForm");
        if (this.value == 'None') {
            hideTransactionActivityForm();
        } else if (this.value == 'FOK') {
            showTransactionActivityForm();
            if ($('input:radio[name=isSellRadioGroup]:checked').val() == 'Sell') {
                document.getElementById("lowestPrice").style.display = "block";
                document.getElementById("highestPrice").style.display = "none";
            } else {
                document.getElementById("highestPrice").style.display = "block";
                document.getElementById("lowestPrice").style.display = "none";
            }
        } else if (this.value == 'IOC') {
            showTransactionActivityForm();
            if ($('input:radio[name=isSellRadioGroup]:checked').val() == 'Sell') {
                document.getElementById("lowestPrice").style.display = "block";
                document.getElementById("highestPrice").style.display = "none";
            } else {
                document.getElementById("highestPrice").style.display = "block";
                document.getElementById("lowestPrice").style.display = "none";
            }
        } else if (this.value == 'LMT') {
            showTransactionActivityForm();
            if ($('input:radio[name=isSellRadioGroup]:checked').val() == 'Sell') {
                document.getElementById("lowestPrice").style.display = "block";
                document.getElementById("highestPrice").style.display = "none";
            } else {
                document.getElementById("highestPrice").style.display = "block";
                document.getElementById("lowestPrice").style.display = "none";
            }
        } else if (this.value == 'MKT') {
            showTransactionActivityForm();
            document.getElementById("lowestPrice").style.display = "none";
            document.getElementById("highestPrice").style.display = "none";
        }
    });
    $('input[type=radio][name=isSellRadioGroup]').change(function () {
        if (this.value == 'Sell') {
            document.getElementById("highestPrice").style.display = "none";
            document.getElementById("lowestPrice").style.display = "block";
            document.getElementById("numberOfStocks").max = maxAmountOfStocksForSell;
            if ($('input:radio[name=orderTypeGroup]:checked').val() == 'MKT') {
                document.getElementById("lowestPrice").style.display = "none";
                document.getElementById("highestPrice").style.display = "none";
            }
        } else if (this.value == 'Buy') {
            document.getElementById("highestPrice").style.display = "block";
            document.getElementById("lowestPrice").style.display = "none";
            document.getElementById("numberOfStocks").max = "99999";
            if ($('input:radio[name=orderTypeGroup]:checked').val() == 'MKT') {
                document.getElementById("lowestPrice").style.display = "none";
                document.getElementById("highestPrice").style.display = "none";
            }
        }
    });

    /* attach a submit handler to the form */
    $("#TransactionActivityForm").submit(function (event) {
        var body = {
            isSell: ($('input:radio[name=isSellRadioGroup]:checked').val() == 'Sell'),
            numberOfStocks: $('#numberOfStocks').val(),
            lowestPrice: $('#lowestPrice').val() || 0,
            highestPrice: $('#highestPrice').val() || 0,
            symbol: selectedStockSymbol,
            orderType: $('input:radio[name=orderTypeGroup]:checked').val(),
        }
        createTransactionAction(body);
        event.preventDefault();
    });
});

