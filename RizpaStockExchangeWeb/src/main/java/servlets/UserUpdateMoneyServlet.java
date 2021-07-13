package servlets;

import bl.UsersSessionManager;
import models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserUpdateMoneyServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int moneyToAddInput = Integer.parseInt(request.getParameter("moneyToAdd"));

            try {
                User user = (User) request.getServletContext().getAttribute("loggedUser");
                User userCurrent = UsersSessionManager.getInstance().addMoneyToSpecUser(user, moneyToAddInput);
                request.setAttribute("loggedUser", userCurrent);

               /* try {
                    RequestDispatcher rd = request.getRequestDispatcher("/AllUsersAndStocksPage");
                    rd.forward(request, response);
                } catch (ServletException e) {
                }*/
            } catch (Exception ex) {

            }
        } catch (NumberFormatException ex) {

        }
    }
}