<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="locale/locale" var="local" />

<fmt:message bundle="${local}" key="footer.contacts.title" var="title" />
<fmt:message bundle="${local}" key="footer.creator" var="creator" />

<html>
<body>
    <footer>
        <div>
            <p>${title}</p>
            <p>e-mail: rozenberg.kristina@gmail.com</p>
        </div>
        <p>${creator}</p>
    </footer>
</body>
</html>
