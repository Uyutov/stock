<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: vladimir
  Date: 4.06.24
  Time: 13:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html
>
<head>
    <title>Cart</title>
</head>
<body>
<h1>Dear <%=session.getAttribute("name")%>, your order:</h1>
<table>
    <%
        Map<String, Integer> items = (Map<String, Integer>) session.getAttribute("selected_options");
        int index = 0;
        int sum = 0;
    %>
    <% for(Map.Entry<String, Integer> item : items.entrySet()) { %>
    <%
        index++;
        sum += item.getValue();
    %>
    <tr>
        <td><%=index%>) <%=item.getKey()%> <%=item.getValue()%></td>
    </tr>
    <% } %>
</table>
<p>Total: <%=sum%></p>
</body>
</html>
