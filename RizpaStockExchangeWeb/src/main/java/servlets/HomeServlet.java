package servlets;

import bl.UsersSessionManager;
import enums.Role;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebServlet(name = "HomeServlet", urlPatterns = {"/RizpaStockExchangeWeb_war/src/main/java/servlets/HomeServlet.java"})
public class HomeServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String username = request.getParameter("username");
        Role role = Role.valueOf(request.getParameter("role"));

        try {
            User user = UsersSessionManager.login(username, role);

            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser", user);
            response.sendRedirect("Home.jsp"); // logged-in page
        } catch (UserAlreadyExistsException e) {
            response.sendError(303);
        }
    }

    public void destroy() {
    }
}