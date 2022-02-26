<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="locale/locale" var="local" />

<fmt:message bundle="${local}" key="header.profile" var="profile" />
<fmt:message bundle="${local}" key="header.basket" var="basket" />
<fmt:message bundle="${local}" key="header.logout" var="logout" />
<fmt:message bundle="${local}" key="header.users" var="users" />
<fmt:message bundle="${local}" key="header.products" var="products" />
<fmt:message bundle="${local}" key="header.orders" var="orders" />

<html>
<body>
    <header>
        <h2>CraftShop</h2>
        <div class="row">
<%--            <div class="col btn-group">--%>
<%--                <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--                    Nickname--%>
<%--                </button>--%>
<%--                <ul class="dropdown-menu">--%>
<%--                    <li><a class="dropdown-item" href="#">${profile}</a></li>--%>
<%--                    <li><a class="dropdown-item" href="#">${basket}</a></li>--%>
<%--                    <li><hr class="dropdown-divider"></li>--%>
<%--                    <li><a class="dropdown-item" href="#">${logout}</a></li>--%>
<%--                </ul>--%>
<%--            </div>--%>

<%--            <div class="col btn-group">--%>
<%--                <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--                    Nickname--%>
<%--                </button>--%>
<%--                <ul class="dropdown-menu">--%>
<%--                    <li><a class="dropdown-item" href="#">${users}</a></li>--%>
<%--                    <li><a class="dropdown-item" href="#">${products}</a></li>--%>
<%--                    <li><a class="dropdown-item" href="#">${orders}</a></li>--%>
<%--                    <li><hr class="dropdown-divider"></li>--%>
<%--                    <li><a class="dropdown-item" href="#">${logout}</a></li>--%>
<%--                </ul>--%>
<%--            </div>--%>

            <div class="col">
                <a href="${pageContext.request.contextPath}/view/test.jsp">Войти</a>
            </div>

            <div class="col locale">
                <a href="${pageContext.servletContext.contextPath}/controller?command=change_language&language=en"><button type="button" class="btn btn-primary btn-sm ">EN</button></a>
                <a href="${pageContext.servletContext.contextPath}/controller?command=change_language&language=ru"><button type="button" class="btn btn-primary btn-sm">RU</button></a>
            </div>
        </div>
    </header>
</body>
</html>
