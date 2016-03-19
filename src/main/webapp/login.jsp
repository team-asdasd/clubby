<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/_head.jsp"></jsp:include>
    <style>
        body {
            padding-top: 20px;
        }
    </style>
    <title>Clubby - Login</title>
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
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="partials/_scripts.jsp"></jsp:include>
</body>
</html>