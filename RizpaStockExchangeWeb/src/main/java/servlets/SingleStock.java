package servlets;

import bl.StockManager;
import models.Stock;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SingleStock extends HttpServlet {
    private final String SINGLE_STOCK_URL = "../pages/views/SingleStock.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String stockName = request.getParameter("stockname");
        Stock stock = StockManager.getInstance().getStockBySymbol(stockName);

        request.getSession(false).setAttribute("selectedStock", stock);

        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("location='"  + SINGLE_STOCK_URL + "';");
        out.println("</script>");
    }
}
