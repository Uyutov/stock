<%--
  Created by IntelliJ IDEA.
  User: vladimir
  Date: 4.06.24
  Time: 13:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Select</title>
</head>
<body>
    <h1>Hello <%= (String) session.getAttribute("name")!%></h1>
    <form action="select" method="post">
        <label for="items">Select items</label>
        <select id="items" name="items" multiple="multiple">
            <option value="Phone 10" name="Phone">Phone 10</option>
            <option value="Pineapple 1" name="Pineapple">Pineapple 1</option>
            <option value="TV 30" name="TV">TV 30</option>
            <option value="Shoes 3" name="Shoes">Shoes 3</option>
            <option value="Pug 100" name="Pug">Pug 100</option>
        </select>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
