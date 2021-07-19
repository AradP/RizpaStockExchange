package servlets;

import bl.UserManager;
import com.google.gson.Gson;
import models.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TradeActivitiesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            User usernameFromSession = SessionUtils.getUsername(request);
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            User userCurrent = userManager.getUserByName(usernameFromSession.getName());
            String json = gson.toJson(userCurrent.getTradeActivities());
            out.println(json);
            out.flush();
        }
    }
}