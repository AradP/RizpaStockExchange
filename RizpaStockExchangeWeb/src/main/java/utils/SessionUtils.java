package utils;


import constants.Constants;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static User getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? (User) sessionAttribute : null;
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}