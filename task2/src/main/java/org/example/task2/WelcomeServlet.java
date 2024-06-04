package org.example.task2;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "welcomeServlet", value = "/welcome")
public class WelcomeServlet extends HttpServlet {

    public void init() {}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        HttpSession session = request.getSession();
        session.setAttribute("name", name);

        response.sendRedirect("select.jsp");
    }

    public void destroy() {
    }
}