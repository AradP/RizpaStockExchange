package servlets;

import models.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserUpdateMoneyServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int moneyToAddInput = Integer.parseInt(request.getParameter("moneyToAdd"));

            try {
                User user = SessionUtils.getUsername(request);
                User userCurrent = ServletUtils.getUserManager(getServletContext()).addMoneyToSpecUser(user, moneyToAddInput);
            } catch (Exception ex) {

            }
        } catch (NumberFormatException ex) {

        }
    }
}