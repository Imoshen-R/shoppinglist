package de.aswggmbh.demo;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Eigenständiges Servlet-Beispiel; die MVC-Einkaufsliste liegt unter /.
@WebServlet(urlPatterns = "/list")
public class ListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("{\"items\":[]}");
    }
}
