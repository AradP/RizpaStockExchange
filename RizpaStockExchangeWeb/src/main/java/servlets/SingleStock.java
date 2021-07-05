package servlets;

import bl.StockManager;
import models.Stock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SingleStock extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String stockName = request.getParameter("stockname");

        Stock stock = StockManager.getInstance().getStockBySymbol(stockName);

        request.setAttribute("stock", stock);
        try {
            RequestDispatcher rd = request.getRequestDispatcher("/Single");
            rd.forward(request, response);
//            request.getRequestDispatcher("/RizpaStockExchangeWeb_war/SingleStock.jsp").forward(request, response);

//        response.sendRedirect("/RizpaStockExchangeWeb_war/SingleStock.jsp");
        } catch (ServletException e) {
            response.getWriter().println("Can't forward to the chosen stock because: " + e.getMessage());
        }
    }
}
