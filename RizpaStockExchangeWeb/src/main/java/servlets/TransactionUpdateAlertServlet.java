package servlets;

import bl.UserManager;
import models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransactionUpdateAlertServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        response.setContentType("text/html");
        User user = (User) request.getServletContext().getAttribute("loggedUser");
        User currentUser = UserManager.getInstance().getUserByName(user.getName());
        response.getWriter().print(currentUser.getAlertMsg());
        currentUser.setAlertMsg("");
        } catch (Exception ex) {

        }
    }
}
