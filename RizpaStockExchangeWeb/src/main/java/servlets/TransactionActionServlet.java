package servlets;

import bl.BLManager;
import bl.StockManager;
import bl.UserManager;
import enums.OrderType;
import exceptions.stocks.StockException;
import models.Stock;
import models.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransactionActionServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String stockName = request.getParameter("stockname");
        StockManager stockManager = ServletUtils.getStockManager(getServletContext());
        Stock stock = stockManager.getStockBySymbol(stockName);
        request.getSession(false).setAttribute("selectedStock", stock);
        try {
            RequestDispatcher rd = request.getRequestDispatcher("/Single");
            rd.forward(request, response);
        } catch (ServletException e) {
            response.getWriter().println("Can't forward to the chosen stock because: " + e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        boolean isSell = Boolean.parseBoolean(request.getParameter("isSell"));
        int numberOfStocks = Integer.parseInt(request.getParameter("numberOfStocks"));
        double lowestPrice = Double.parseDouble(request.getParameter("lowestPrice"));
        double highestPrice = Double.parseDouble(request.getParameter("highestPrice"));
        String symbol = request.getParameter("symbol");
        OrderType orderType = OrderType.FOK;
        User usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        User userCurrent = userManager.getUserByName(usernameFromSession.getName());
        String returnValue = "";
        try {
            switch (request.getParameter("orderType")) {
                case "FOK":
                    orderType = OrderType.FOK;
                    if (isSell) {
                        returnValue = BLManager.getInstance().sellFOKOrder(symbol, numberOfStocks, (double) lowestPrice, userCurrent);

                    } else {
                        returnValue = BLManager.getInstance().buyFOKOrder(symbol, numberOfStocks, (double) highestPrice, userCurrent);
                    }
                    break;
                case "IOC":
                    orderType = OrderType.IOC;
                    if (isSell) {
                        returnValue = BLManager.getInstance().sellIOCOrder(symbol, numberOfStocks, (double) lowestPrice, userCurrent);

                    } else {
                        returnValue = BLManager.getInstance().buyIOCOrder(symbol, numberOfStocks, (double) highestPrice, userCurrent);
                    }
                    break;
                case "LMT":
                    orderType = OrderType.LMT;
                    if (isSell) {
                        returnValue = BLManager.getInstance().sellLMTOrder(symbol, numberOfStocks, (double) lowestPrice, userCurrent);

                    } else {
                        returnValue = BLManager.getInstance().buyLMTOrder(symbol, numberOfStocks, (double) highestPrice, userCurrent);
                    }
                    break;
                case "MKT":
                    orderType = OrderType.MKT;
                    if (isSell) {
                        returnValue = BLManager.getInstance().sellMKTOrder(symbol, numberOfStocks, userCurrent);

                    } else {
                        returnValue = BLManager.getInstance().buyMKTOrder(symbol, numberOfStocks, userCurrent);
                    }
                    break;
            }
            response.getWriter().println(returnValue);
        } catch (StockException e) {
            e.printStackTrace();
        }
    }
}