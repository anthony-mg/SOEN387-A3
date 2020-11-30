<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 2020-11-05
  Time: 9:12 p.m.
  To change this template use File | Settings | File Templates.
--%>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark mb-3">
    <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/posts">Posts</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/newPost">Add Post</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchPage">Search</a></li>
    </ul>
    <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link" href="LogOutServlet">Logout</a></li>
    </ul>
</nav>
