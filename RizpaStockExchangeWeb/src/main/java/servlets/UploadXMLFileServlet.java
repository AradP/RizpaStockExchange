package servlets;

import bl.BLManager;
import constants.Constants;
import exceptions.stocks.StockException;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig
public class UploadXMLFileServlet extends HttpServlet {
    private static final String SAVE_DIR = "uploadFiles";
    private final String ALL_USERS_AND_STOCKS_URL = "../pages/views/AllUsersAndStocks.jsp";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the current user
        User user = (User) request.getSession(false).getAttribute(Constants.USERNAME);

        String message;

        Part filePart = null; // Retrieves <input type="file" name="file">
        try {
            filePart = request.getPart("file");

            if (filePart != null) {
                try {
                    // Save the file to our local folder
                    // gets absolute path of the web application
                    String appPath = request.getServletContext().getRealPath("");

                    // constructs path of the directory to save uploaded file
                    String savePath = appPath + File.separator + SAVE_DIR;

                    // creates the save directory if it does not exists
                    File fileSaveDir = new File(savePath);
                    if (!fileSaveDir.exists()) {
                        fileSaveDir.mkdir();
                    }

                    String tmpFileName = null;

                    for (Part part : request.getParts()) {
                        tmpFileName = extractFileName(part);
                        // refines the fileName in case it is an absolute path
                        tmpFileName = new File(tmpFileName).getName();
                        part.write(savePath + File.separator + tmpFileName);
                    }

                    BLManager.getInstance().loadConfigurationFileForUser(new File(savePath + File.separator + tmpFileName), user);
                    message = "Successfully updated the system";

                } catch (StockException | JAXBException invalidSystemDataFile) {
                    message = "There was a problem while uploading the file: " + invalidSystemDataFile.getMessage();
                }
            } else {
                message = "Please choose a file";
            }
        } catch (ServletException e) {
            message = "There was a problem while parsing the file: " + e.getMessage();
        }

        response.setStatus(200);
        request.setAttribute("xmlUploadMsg", message);

        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + message + "');");
        out.println("location='" + ALL_USERS_AND_STOCKS_URL + "';");
        out.println("</script>");
    }

    public void destroy() {
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
