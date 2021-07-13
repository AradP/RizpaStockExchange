package servlets;

import bl.UsersSessionManager;
import enums.Role;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        Role role = Role.valueOf(request.getParameter("role"));

        try {
            User user = UsersSessionManager.getInstance().login(username, role);
            request.setAttribute("loggedUser", user);

            try {
                RequestDispatcher rd = request.getRequestDispatcher("/AllUsersAndStocksPage");
                rd.forward(request, response);
            } catch (ServletException e) {
                response.getWriter().println("Can't forward to the home page because: " + e.getMessage());
            }

//            Cookie userCookie = new Cookie("username", user.getName());
//
//            response.setContentType("text/html");
//
//            response.addCookie(userCookie);
//            response.sendRedirect("/RizpaStockExchangeWeb_war/views/AllUsersAndStocks.jsp");
        } catch (UserAlreadyExistsException e) {
            request.setAttribute("loggedUser", (User) request.getServletContext().getAttribute("loggedUser"));
            RequestDispatcher rd = request.getRequestDispatcher("/AllUsersAndStocksPage");
            rd.forward(request, response);
            // response.setContentType("text/html");
            //response.getWriter().println("This user is already logged");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try {
            request.setAttribute("loggedUser", (User) request.getServletContext().getAttribute("loggedUser"));
            try {
                RequestDispatcher rd = request.getRequestDispatcher("/AllUsersAndStocksPage");
                rd.forward(request, response);
            } catch (ServletException e) {
                response.getWriter().println("Can't forward to the home page because: " + e.getMessage());
            }
        } catch (Exception e) {

        }
    }

    public void destroy() {
    }
}