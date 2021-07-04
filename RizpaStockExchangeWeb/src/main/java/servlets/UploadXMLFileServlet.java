package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UploadXMLFileServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("text/html");
//
//        String username = request.getParameter("username");
//        Role role = Role.valueOf(request.getParameter("role"));
//
//        try {
//            User user = UsersSessionManager.getInstance().login(username, role);
//
//            Cookie userCookie = new Cookie("username", user.getName());
//            response.addCookie(userCookie);
//            response.sendRedirect("/RizpaStockExchangeWeb_war/AllUsersAndStocks.jsp");
//        } catch (UserAlreadyExistsException e) {
//            response.sendError(303);
//        }
        System.out.println("WOWWWWW");
        System.out.println("aewqe" + request.getParameter("file"));
    }

    public void destroy() {
    }
}
