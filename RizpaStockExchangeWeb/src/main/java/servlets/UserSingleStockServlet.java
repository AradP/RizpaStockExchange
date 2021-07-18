package servlets;

import bl.UserManager;
import models.Stock;
import models.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSingleStockServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String stockName = request.getParameter("stockName");
        User username = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        Stock currentStock = ServletUtils.getStockManager(getServletContext()).getStockBySymbol(stockName);
        int amountOfStocks = userManager.getUserByName(username.getName()).getHoldings().getOrDefault(currentStock, 0);
        response.getWriter().print(amountOfStocks);
    }
}