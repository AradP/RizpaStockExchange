package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BackToHomePageServlet extends HttpServlet {
    private final String ALL_USERS_AND_STOCKS_URL = "../views/AllUsersAndStocks.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(ALL_USERS_AND_STOCKS_URL);
    }
}
