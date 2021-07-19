var refreshRate = 600; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var STOCK_LIST_URL = buildUrlWithContextPath("stockslist");

function refreshUsersList(users) {
    //clear all current users
    $("#users_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function (index, user) {
        $('<tr><td>' + user.name + '</td><td>' + user.role + '</td></tr>')
            .appendTo($("#users_table"));
    });
}

function refreshStocksList(stocks) {
    //clear all current users
    $("#stocks_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(stocks || [], function (index, stock) {
        // create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<tr onclick="chooseStock(this)"><td class="stock-symbol">' + stock.symbol + '</td>' +
            '<td>' + stock.companyName + '</td><td>' + stock.price + '</td><td>' + stock.ordersPeriod + '</td></tr>')
            .appendTo($("#stocks_table"));

    });
}

function refreshTradeActivities(activities) {
    //clear all current users
    $("#trade_activities_table").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(activities || [], function (index, activity) {
        // create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<tr><th>' + activity.timeStamp + '</th>' +
            '<td>' + activity.symbol + '</td><td>' + activity.tradeActivityType + '</td><td>' + activity.price + '</td><td>' + activity.moneyLeftBefore + '</td><td>' + activity.moneyLeftAfter + '</td></tr>')
            .appendTo($("#trade_activities_table"));

    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

function ajaxStocksList() {
    $.ajax({
        url: STOCK_LIST_URL,
        success: function (stocks) {
            refreshStocksList(stocks);
        }
    });
}

function ajaxTradeActivities() {
    $.ajax({
        type: "GET",
        url: "/RizpaStockExchangeWeb_war/servlets/TradeActivitiesServlet",
        success: function (activities) {
            refreshTradeActivities(activities);
        }
    });
}

function ajav() {
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
}

function getMoney() {
    $.ajax({
        type: "GET",
        url: "/RizpaStockExchangeWeb_war/servlets/UserUpdateMoneyServlet",
        success: function (resp) {
            document.getElementById("AccountCurrentMoney").textContent = "Account current money: " + resp;
        }
    });
}

//activate the timer calls after the page is loaded
$(function () {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStocksList, refreshRate);
    setInterval(ajav, refreshRate);
    setInterval(getMoney, refreshRate);
    setInterval(ajaxTradeActivities, refreshRate);

    /* attach a submit handler to the form */
    $("#createNewStockForm").submit(function (event) {
        $.ajax({
            type: "POST",
            url: "/RizpaStockExchangeWeb_war/servlets/CreateNewStockServlet",
            data: {
                companyName: $('#CompanyNameInput').val(),
                symbol: $('#SymbolInput').val(),
                amountOfStocks: $('#AmountOfStocksInput').val(),
                companyValue: $('#CompanyValueInput').val(),
            },
            success: function (resp) {
                console.log(resp);
            },
            error: function (req, status, err) {
                alert(req.responseText)
            }
        }).done(function (data) {
            console.log(data);
            location.reload();
        });
        event.preventDefault();
    });

    /* attach a submit handler to the form */
    $("#updateMoneyForm").submit(function (event) {
        $.ajax({
            type: "POST",
            url: "/RizpaStockExchangeWeb_war/servlets/UserUpdateMoneyServlet",
            data: {moneyToAdd: $('#moneyToAddInput').val()},
            success: function (resp) {
                console.log(resp);
                location.reload();
            },
            error: function (req, status, err) {
                console.log('Something went wrong', status, err);
                console.log(req);
            }
        });
        event.preventDefault();
    });
});

function toggleShowCreateNewStockSection() {
    var x = document.getElementById("newStockSection");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}