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
    <jsp:useBean id="helper" class="helpers.PostHelper" type="helpers.PostHelper" scope="request"/>
    <form class="form-postInfo">
        <fieldset disabled="disabled">
            <h2>Post Info</h2>
            <div class="form-group">
                <label for="title">Title </label>
                <input type="text" class="form-control" name="title" id="title" value="<%=helper.getTitle()%>">
            </div>
            <div class="form-group">
                <label for="author">Author </label>
                <input type="text" class="form-control" name="author" id="author" value="<%=helper.getUser()%>">
            </div>
            <div class="form-group">
                <label for="created">Created Date </label>
                <input type="text" class="form-control" name="created" id="created" value="<%=helper.getCreatedDate()%>">
            </div>
            <div class="form-group">
                <label for="created">Group </label>
                <input type="text" class="form-control" name="group" id="group" value="<%=helper.getGroup()%>">
            </div>
            <%=helper.getUpdatedDate()%>
            <div class="form-group">
                <label for="postText">Text </label>
                <textarea name="text" id="postText" rows="10" class="form-control"><%=helper.getText()%></textarea>
            </div>
            <%=helper.getAttachments()%>
        </fieldset>
        <div class="row mb-5">
            <div class="col-md-6">
                <a target='_blank' class="btn btn-outline-success btn-block" href="${pageContext.request.contextPath}/download?postId=<%=helper.getPostId()%>">Download Post</a>
            </div>
            <div class="col-md-6">
                <a target='_blank' class="btn btn-outline-warning btn-block" href="${pageContext.request.contextPath}/viewXml?postId=<%=helper.getPostId()%>">View XML</a>
            </div>
        </div>
        <%=helper.showEdit()%>
    </form>
</div>
</body>
</html>

