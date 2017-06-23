<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%
    String previewUrl = request.getHeader("Referer");
    if(previewUrl!= null && previewUrl.indexOf("index.html") != -1){
        response.setStatus(402);
    }
%>--%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
    <title>蒲蒲团·管理后台</title>
    <!-- BOOTSTRAP STYLES-->
    <link rel="stylesheet" type="text/css" href="dep/bootstrap-3.3.6/css/bootstrap.min.css"/>
    <!-- FONTAWESOME STYLES-->
    <link rel="stylesheet" type="text/css" href="dep/font-awesome-4.5.0/css/font-awesome.min.css"/>
    <!-- CUSTOM STYLES-->
    <link rel="stylesheet" type="text/css" href="src/css/login.css"/>
</head>
<body>

<div class="container">
    <c:if test="${message!=null && message!='访问成功'}">
        <div id="message_box" class="alert alert-danger" role="alert" >${message}</div>
    </c:if>

    <div class="card signin-card">

        <form class="form-signin" method="post" action="/login">
            <h2 class="form-signin-heading">蒲蒲团·管理后台</h2>
            <input name="account" class="form-control" type="text" placeholder="输入账号" required autofocus>
            <input name="password" class="form-control" type="password" placeholder="密码" required>
            <%--<div class="checkbox">--%>
            <%--<label>--%>
            <%--<input type="checkbox" value="remember-me"> 记住账号--%>
            <%--</label>--%>
            <%--</div>--%>
            <input class="btn btn-lg btn-primary btn-block" type="submit" value="登录">
        </form>

    </div>
</div>
</body>
</html>