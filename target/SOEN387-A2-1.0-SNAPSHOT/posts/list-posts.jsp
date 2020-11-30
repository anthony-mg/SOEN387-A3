<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 2020-11-04
  Time: 7:23 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="js/jQuery_modal.js"></script>
    <style>
        .table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
            background-color: #d7ffe0;
        }
    </style>
    <title>Message Board System</title>
</head>
<body>
<div class="container-fluid">
    <jsp:include page = "nav-bar.jsp" flush = "true" />
    <jsp:include page = "notifications.jsp" flush = "true" />
    <h2 style="text-align:center;">Posts</h2>
    <div class="table-responsive">
        <table class="table table-borderless table-striped table-hover" >
            <thead class="thead-light">
            <tr class="d-flex">
                <th class="col-2">title</th>
                <th class="col-5">post</th>
                <th class="col-2">user</th>
                <th class="col-2">date</th>
                <th class="col-1">updated</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="post" items="${posts}">
                <tr class="d-flex">
                    <td class="col-sm-2"><a href="${pageContext.request.contextPath}/view?postId=${post.postId}">${post.title}</a></td>
                    <td class="col-sm-5">${post.text}</td>
                    <td class="col-sm-2">${post.user}</td>
                    <td class="col-sm-2">${post.dateString}</td>
                    <td class="col-sm-1">${post.updated}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
