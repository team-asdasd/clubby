<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<h1>APP</h1>

<p>users only mwahahahahahah</p>

<shiro:hasRole name="admin">
    <p>Admin section: <a href="/admin">here</a></p>
</shiro:hasRole>