package utils;


import constants.Constants;
import enums.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Role getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.ROLE) : null;
        return sessionAttribute != null ? (Role) sessionAttribute : null;
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}