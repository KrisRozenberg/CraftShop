<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="current_page" value="${pageContext.request.requestURI}" scope="session"/>
<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="locale/locale" var="local" />

<fmt:message bundle="${local}" key="registration.login" var="login" />
<fmt:message bundle="${local}" key="registration.password" var="password" />
<fmt:message bundle="${local}" key="registration.submit" var="submit" />
<fmt:message bundle="${local}" key="form.invalid.login" var="invalidLogin" />
<fmt:message bundle="${local}" key="form.invalid.password" var="invalidPassword" />
<fmt:message bundle="${local}" key="logIn.invalidMessage" var="invalidMessage" />

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CraftShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="../static/style.css" />
<%--    <script type="text/javascript">--%>
<%--        window.history.forward();--%>
<%--        function noBack() {--%>
<%--            window.history.forward();--%>
<%--        }--%>
<%--    </script>--%>
</head>
<body>
<%--<body onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">--%>
<c:import url="header.jsp"/>

<div class="container">
    <div class="row">
        <form class="row g-3 sign-in needs-validation" novalidate
              action="${pageContext.request.contextPath}/controller" method="post">
            <input type="hidden" name="command" value="log_in">
            <div class="col-md-4">
                <label for="validationCustom01" class="form-label">${login}</label>
                <input type="text" name="login" class="form-control" id="validationCustom01" required
                       pattern="[A-Za-zА-Яа-я0-9]{3,25}">
                <div class="invalid-feedback">
                    ${invalidLogin}
                </div>
            </div>
            <div class="col-md-4">
                <label for="validationCustom02" class="form-label">${password}</label>
                <input type="password" name="password" class="form-control" id="validationCustom02" required
                       pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}">
                <div class="invalid-feedback">
                    ${invalidPassword}
                </div>
            </div>
            <c:if test="${requestScope.errorMessage != null}">
                <div class="alert alert-info" role="alert">
                    <fmt:setBundle basename="locale/locale" var="error"/>
                    <fmt:message key="${requestScope.errorMessage}" bundle="${error}"/>
                </div>
            </c:if>
            <br>
            <div class="col-12 ">
                <button class="btn btn-primary mt-4 offset-md-1" type="submit">${submit}</button>
            </div>
        </form>
    </div>
</div>

<c:import url="footer.jsp"/>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')

        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }

                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>
</body>
</html>
