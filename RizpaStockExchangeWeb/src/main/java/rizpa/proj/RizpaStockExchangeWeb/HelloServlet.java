package rizpa.proj.RizpaStockExchangeWeb;

import bl.UsersSessionManager;
import exceptions.users.UserAlreadyExistsException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String username = request.getParameter("un");

        try {
            UsersSessionManager.login(username);

            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser", username);
            response.sendRedirect("Home.jsp"); //logged-in page
        } catch (UserAlreadyExistsException e) {

            response.sendError(303);
        }

    }

    public void destroy() {
    }
}