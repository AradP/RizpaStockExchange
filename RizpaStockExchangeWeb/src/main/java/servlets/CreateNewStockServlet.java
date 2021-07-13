package servlets;

import bl.StockManager;
import bl.UsersSessionManager;
import exceptions.stocks.CompanyAlreadyExistException;
import exceptions.stocks.StockSymbolAlreadyExistException;
import models.Stock;
import models.User;

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
                User user = (User) request.getServletContext().getAttribute("loggedUser");
                User userCurrent = UsersSessionManager.getInstance().AddStockToUserByIPO(user, symbol, amountOfStocks);
            }

            try {
                User user = (User) request.getServletContext().getAttribute("loggedUser");
                request.setAttribute("loggedUser", UsersSessionManager.getInstance().getUserByName(user.getName()));

               /* try {
                    RequestDispatcher rd = request.getRequestDispatcher("/AllUsersAndStocksPage");
                    rd.forward(request, response);
                } catch (ServletException e) {
                }*/
            } catch (Exception ex) {

            }
        } catch (NumberFormatException ex) {

        } catch (StockSymbolAlreadyExistException e) {
            e.printStackTrace();
        } catch (CompanyAlreadyExistException e) {
            e.printStackTrace();
        }
    }
}
