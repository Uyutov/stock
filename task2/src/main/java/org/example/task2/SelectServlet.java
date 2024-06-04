package org.example.task2;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "selectServlet", value = "/select")
public class SelectServlet extends HttpServlet {

    public void init() {}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] selectedItems = request.getParameterValues("items");
        Map<String, Integer> map = new HashMap<>();

        for(String item : selectedItems)
        {
            String[] selectParts = item.split(" ");
            map.put(selectParts[0], Integer.parseInt(selectParts[1]));
        }

        HttpSession session = request.getSession();
        session.setAttribute("selected_options", map);

        response.sendRedirect("cart.jsp");
    }

    public void destroy() {
    }
}