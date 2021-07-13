package servlets;

import bl.StockManager;
import bl.UsersSessionManager;
import models.Stock;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSingleStockServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String stockName = request.getParameter("stockname");
        String userName = request.getParameter("userName");
        Stock currentStock = StockManager.getInstance().getStockBySymbol(stockName);
        int amountOfStocks = UsersSessionManager.getInstance().getUserByName(userName).getHoldings().getOrDefault(currentStock, 0);
        response.getWriter().print(amountOfStocks);
    }
}
