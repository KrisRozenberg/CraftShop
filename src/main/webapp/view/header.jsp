<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="locale/locale" var="local" />

<fmt:message bundle="${local}" key="header.profile" var="profile" />
<fmt:message bundle="${local}" key="header.basket" var="basket" />
<fmt:message bundle="${local}" key="header.logOut" var="logOut" />
<fmt:message bundle="${local}" key="header.users" var="users" />
<fmt:message bundle="${local}" key="header.products" var="products" />
<fmt:message bundle="${local}" key="header.orders" var="orders" />
<fmt:message bundle="${local}" key="header.logIn" var="logIn" />

<html>
<body>
    <header>
        <h2>CraftShop</h2>
        <div class="row">
            <c:choose>
                <c:when test="${sessionScope.userRole eq 'ADMIN'}">
                    <div class="col btn-group">
                        <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                ${sessionScope.userLogin}
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">${users}</a></li>
                            <li><a class="dropdown-item" href="#">${products}</a></li>
                            <li><a class="dropdown-item" href="#">${orders}</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a href="${pageContext.servletContext.contextPath}/controller?command=log_out" class="dropdown-item" href="#">${logOut}</a></li>
                        </ul>
                    </div>
                </c:when>
                <c:when test="${sessionScope.userRole eq 'CLIENT'}">
                    <div class="col btn-group">
                        <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                ${sessionScope.userLogin}
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">${profile}</a></li>
                            <li><a class="dropdown-item" href="#">${basket}</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a href="${pageContext.servletContext.contextPath}/controller?command=log_out" class="dropdown-item" href="#">${logOut}</a></li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col">
                        <a href="${pageContext.request.contextPath}/view/logIn.jsp">${logIn}</a>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="col locale">
                <a href="${pageContext.servletContext.contextPath}/controller?command=change_language&language=en"><button type="button" class="btn btn-primary btn-sm ">EN</button></a>
                <a href="${pageContext.servletContext.contextPath}/controller?command=change_language&language=ru"><button type="button" class="btn btn-primary btn-sm">RU</button></a>
            </div>
        </div>
    </header>
</body>
</html>
