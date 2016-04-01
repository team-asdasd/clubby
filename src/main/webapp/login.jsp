<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="security.shiro.facebook.FacebookSettings" %>

<shiro:user>
    <% response.sendRedirect("/"); %>
</shiro:user>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="app/shared/partials/_head.jsp"></jsp:include>

    <style>
        body {
            padding-top: 20px;
        }
    </style>
    <title>Clubby - Login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-social/4.12.0/bootstrap-social.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Please sign in</h3>
                </div>
                <div class="panel-body">
                    <form name="loginform" action="" method="POST" accept-charset="UTF-8" role="form">
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" placeholder="Username or Email" name="username" type="text">
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="Password" name="password" type="password" value="">
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input name="rememberMe" type="checkbox" value="true"> Remember Me
                                </label>
                            </div>
                            <input class="btn btn-lg btn-success btn-block" type="submit" value="Login">

                            <a href="https://www.facebook.com/dialog/oauth?client_id=<% out.print(FacebookSettings.getAppId()); %>&redirect_uri=<% out.print(FacebookSettings.getRedirectUrl()); %>&scope=email" class="btn btn-lg btn-block btn-social btn-facebook" style="text-align: center;">
                                <span class="fa fa-facebook"></span> Sign in with Facebook
                            </a>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="app/shared/partials/_scripts.jsp"></jsp:include>
</body>
</html>