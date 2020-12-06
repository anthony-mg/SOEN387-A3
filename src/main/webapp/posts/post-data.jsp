<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 2020-11-04
  Time: 7:23 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="css/post_form.css">
    <title>Post page</title>
</head>
<body class="bg-light text-center">
<div class="container-fluid">
    <jsp:include page = "nav-bar.jsp" flush = "true" />

    <c:if test="${post != null}">
    <h2>Edit Post</h2>
    <form class="form-postInfo" action="modify" method="post" enctype="multipart/form-data">
        <input type="hidden" id="postId" name="postId" value="${post.postId}">
        </c:if>

        <c:if test="${post == null}">
        <h2>Add New Post</h2>
        <form class="form-postInfo" action="addPost" method="post" enctype="multipart/form-data">
            </c:if>
            <div class="form-group">
                <label for="title">Title </label>
                <input required class="form-control" type="text" id="title" name="title" value="${post.title}">
            </div>
            <div class="form-group">
                <label for="postText">Text </label>
                <textarea required name="postText" id="postText" rows="10" class="form-control">${post.text}</textarea>
            </div>
            <c:if test="${post != null}">
            <div class="form-group">
                <label for="groupValue">Post Group </label>
                <input disabled class="form-control" type="text" id="groupValue" name="groupValue" value="${post.group}">
            </div>
            </c:if>
            <div class="form-group">
                <label for="groups">Groups</label><br>
                <select id="groups" class="form-control" name="groups" size="5">
                    <option value="public" selected>public</option>
                    <c:if test="${groups != null}">
                        <c:forEach var="group" items="${groups}">
                            <option value="${group}">${group}</option>
                        </c:forEach>
                    </c:if>
                </select><br>
            </div>
            <div class="form-group">
                <label for="attach">Upload attachments </label>
                <input class="form-control" type="file" name="file" multiple="multiple" id="attach">
            </div>
            <button type="submit" class="btn btn-lg btn-primary btn-block">Submit</button>
            <c:if test="${post != null}">
                <a class="btn btn-danger mt-2 btn-lg btn-block" onclick="return confirm('Are you sure?');" href="${pageContext.request.contextPath}/delete?postId=${post.postId}">Delete Post</a>
            </c:if>
        </form>

        <c:if test="${not empty attachmentNames}">
        <div class="form-group">
            <h2>Edit Posts Attachments:</h2>
        </div>
        <c:forEach var="file" items="${attachmentNames}" varStatus="status">
        <form class="form-postInfo border border-primary" action="editAttach" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <a href="#">${file.value}</a>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <input type="file" class="form-control-file" name="file_${status.count}">
                </div>
                <div class="col-md-3">
                    <input class="btn btn-info btn-secondary" type="submit"  value="Replace">
                </div>
                <div class="col-md-3">
                    <a class="btn btn-danger" onclick="return confirm('Are you sure?');" href="${pageContext.request.contextPath}/deleteAttach?postId=${post.postId}&attachmentID=${file.key}">delete</a><br>
                </div>
            </div>
            <input type="hidden" id="attachmentId" name="attachmentId" value="${file.key}">
            <input type="hidden" id="post" name="postId" value="${post.postId}">
        </form>
        </c:forEach>
        </c:if>
</div>
</body>
</html>
