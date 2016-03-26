<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="app/shared/partials/_head.jsp"></jsp:include>
    <link href="res/cover.css" rel="stylesheet">
    <title>Clubby - Team ASDASD</title>
</head>
<body>
<div class="site-wrapper">
    <div class="site-wrapper-inner">
        <div class="cover-container">
            <div class="masthead clearfix">
                <div class="inner">
                    <h3 class="masthead-brand">clubby</h3>
                    <nav>
                        <ul class="nav masthead-nav">
                            <li class="active"><a href="/">Home</a></li>
                            <shiro:user><li><a href="/app">App</a></li></shiro:user>
                            <shiro:hasRole name="administrator"><li><a href="/admin">Admin</a></li></shiro:hasRole>
                            <shiro:user><li><a href="logout">Logout</a></li></shiro:user>
                            <shiro:guest><li><a href="login.jsp">Login</a></li></shiro:guest>
                        </ul>
                    </nav>
                </div>
            </div>

            <div class="inner cover">
                <h1 class="cover-heading">"Every day is a new day. It is better to be lucky. But I would rather be
                    exact. Then when luck comes you are ready."</h1>
                <p class="lead"> - Ernest Hemingway, The Old Man and the Sea</p>
                <p class="lead">
                    <a href="https://www.google.com/search?q=Ernest+Hemingway%2C+The+Old+Man+and+the+Sea"
                       class="btn btn-lg btn-default">Learn more</a>
                </p>
            </div>
            <div class="mastfoot">
                <div class="inner">
                    <p>Created by Team ASDASD</p>
                    <p>&copy; 2016 TeamASDASD ALL RIGHTS RESERVED</p>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="app/shared/partials/_scripts.jsp"></jsp:include>
</body>
</html>
