<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="locale/locale" var="local" />

<fmt:message bundle="${local}" key="login.button" var="login" />

<html>
<head>
    <title>CraftShop</title>
</head>
<body>
<jsp:forward page="view/about.jsp"/>
</body>
</html>

