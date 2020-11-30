<%--
  Created by IntelliJ IDEA.
  User: kiho2
  Date: 2020-11-12(012)
  Time: 10:40 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" buffer="none"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/post_form.css">
    <title>Post page</title>
</head>

<body class="bg-light text-center">
<div class="container-fluid">
    <jsp:include page = "nav-bar.jsp" flush = "true" />
    <form class="form-postInfo">
        <fieldset disabled="disabled">
            <h2>Post Info</h2>
            <div class="form-group">
                <label for="title">Title </label>
                <input type="text" class="form-control" name="title" id="title" value="${post.title}">
            </div>
            <div class="form-group">
                <label for="author">Author </label>
                <input type="text" class="form-control" name="author" id="author" value="${post.user}">
            </div>
            <div class="form-group">
                <label for="created">Created Date </label>
                <input type="text" class="form-control" name="created" id="created" value="${post.dateString}">
            </div>
            <c:if test="${post.isUpdated()}">
                <div class="form-group">
                    <label for="updated">Updated Date </label>
                    <input type="text" class="form-control" name="updated" id="updated" value="${post.getUpdatedDate()}">
                </div>
            </c:if>
            <div class="form-group">
                <label for="postText">Text </label>
                <textarea name="text" id="postText" rows="10" class="form-control">${post.text}</textarea>
            </div>
            <c:if test="${not empty attachmentNames}">
                <div class="form-group">
                    <label>Attachments </label>
                    <c:forEach var="file" items="${attachmentNames}">
                        <li class="list-group-item d-flex justify-content-between lh-condensed">
                            <a target="_blank" href="FileDownloadServlet?postId=${post.postId}&attachmentId=${file.key}">${file.value}</a>
                        </li>
                    </c:forEach>
                </div>
            </c:if>
        </fieldset>
        <c:set var="current_user" value='<%=request.getSession().getAttribute("user")%>'/>

        <c:if test='${post.user==current_user}'>
            <a class="btn btn-info btn-lg btn-secondary btn-block" href="${pageContext.request.contextPath}/edit?postId=${post.postId}">edit</a>
        </c:if>
    </form>
</div>
</body>
</html>

