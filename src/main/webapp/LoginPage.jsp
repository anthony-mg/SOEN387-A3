<%--
  Created by IntelliJ IDEA.
  User: nianliu
  Date: 2020-11-06
  Time: 9:24 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        html,
        body {
            height: 100%;
            width: 100%;
        }

        body {
            background: url('img/intro-bg.jpg') no-repeat bottom center scroll;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
            display: -ms-flexbox;
            display: -webkit-box;
            display: flex;
            -ms-flex-align: center;
            -ms-flex-pack: center;
            -webkit-box-align: center;
            align-items: center;
            -webkit-box-pack: center;
            justify-content: center;
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #eee;
        }

        .form-signin {
            width: 100%;
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }
        .form-signin .form-signin-heading{
            margin-bottom: 10px;
            color: #FFFFFF;
        }
        .form-signin .checkbox {
            font-weight: 400;
        }
        .form-signin .form-control {
            position: relative;
            box-sizing: border-box;
            height: auto;
            padding: 10px;
            font-size: 16px;
        }
        .form-signin .form-control:focus {
            z-index: 2;
        }
        .form-signin input[type="email"] {
            margin-bottom: 10px;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 0;
        }
        .form-signin input[type="password"] {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }
        .form-signin .button
        {
            display: inline-block;
            letter-spacing: 0.10em;
            margin-bottom: 1.5em;
            padding: 0.5em 5em 0.5em 6em;
            border: 2px solid rgba(255,255,255,1);
            border-radius: 6px;
            font-size: 18px;
            color: #FFF;
            width: 100%;
            background: transparent;
        }

    </style>
    <title>Login Page</title>
</head>
<body class="text-center">
<form class="form-signin" action="${pageContext.request.contextPath}/LoginServlet" method="post">
    <h1 class="h3 mb-3 form-signin-heading">Please Login</h1>
    <label for="inputEmail" class="sr-only">user@email.com</label>
    <input type="email" id="inputEmail" class="form-control" placeholder="Email address" name="email" required autofocus>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Password" required>
    <button class="button" type="submit">Sign in</button>
    <button class="btn btn-lg btn-outline-primary btn-block" type="button" onclick = "window.location.href = 'Error.jsp'">Register</button>
    <%
        if(request.getAttribute("loginError")!=null){
    %>
    <div class="alert alert-danger mt-2">
        <%=request.getAttribute("loginError")%>
    </div>
    <% request.removeAttribute("loginError"); }
    %>
</form>
</body>
</html>
