package servlets;

import bl.UsersSessionManager;
import enums.Role;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        Role role = Role.valueOf(request.getParameter("role"));

        try {
            User user = UsersSessionManager.getInstance().login(username, role);

            Cookie userCookie = new Cookie("username", user.getName());

            response.setContentType("text/html");

            response.addCookie(userCookie);
            response.sendRedirect("/RizpaStockExchangeWeb_war/views/AllUsersAndStocks.jsp");
        } catch (UserAlreadyExistsException e) {
            response.setContentType("text/html");
            response.getWriter().println("This user is already logged");
        }
    }

    public void destroy() {
    }
}