<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 2020-11-06
  Time: 11:31 a.m.
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
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script>
        $( function() {
            $( "#from,#to" ).datepicker();
            $( "#from,#to" ).datepicker( "option", "dateFormat", "yy-mm-dd" );
        } );
    </script>
    <link rel="stylesheet" href="css/post_form.css">
    <title>Search posts</title>
</head>
<body class="bg-light text-center">
<div class="container-fluid">
<jsp:include page = "nav-bar.jsp" flush = "true" />
        <h2>Search Posts</h2>
<form class="form-postInfo" action="${pageContext.request.contextPath}/searchPosts" method="post">
    <div class="form-group">
        <label for="author">Author</label><br>
        <input type="text" class="form-control" id="author" name="user"><br>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="from">From: </label>
                <input type="text" class="form-control" id="from" name="from" />
            </div>
            <div class="col-md-6 mb-3">
                <label for="to">To: </label>
                <input type="text" class="form-control" id="to" name="to" />
            </div>
        </div>
    </div>
    <div class="form-group">
        <label for="hashTags">Hash tags</label><br>
        <select id="hashTags" class="form-control" name="hashTags" multiple size="20">
            <option value="" disabled selected>--Select--</option>
            <c:if test="${hashTags != null}">
            <c:forEach var="hashTag" items="${hashTags}">
                <option value="${hashTag}">#${hashTag}</option>
            </c:forEach>
            </c:if>
        </select><br>
    </div>
    <button type="submit" class="btn btn-lg btn-primary btn-block">Search</button>
</form>
</div>
</body>
</html>
