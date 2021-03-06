package servlets;

import bl.StockManager;
import bl.UserManager;
import exceptions.stocks.CompanyAlreadyExistException;
import exceptions.stocks.StockSymbolAlreadyExistException;
import models.Stock;
import models.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateNewStockServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String companyName = request.getParameter("companyName");
            String symbol = request.getParameter("symbol");
            int amountOfStocks = Integer.parseInt(request.getParameter("amountOfStocks"));
            int companyValue = Integer.parseInt(request.getParameter("companyValue"));

            if (StockManager.getInstance().isSymbolExists(symbol)) {
                response.getWriter().print("Symbol exist already");
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else if (StockManager.getInstance().getStockByCompany(companyName) != null) {
                response.getWriter().print("Company exist already");
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                StockManager.getInstance().addStock(new Stock(symbol, companyName, companyValue / amountOfStocks));
                User usernameFromSession = SessionUtils.getUsername(request);
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                User userCurrent = userManager.AddStockToUserByIPO(usernameFromSession, symbol, amountOfStocks);
            }
        } catch (NumberFormatException ex) {

        } catch (StockSymbolAlreadyExistException e) {
            e.printStackTrace();
        } catch (CompanyAlreadyExistException e) {
            e.printStackTrace();
        }
    }
}