<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}../static/style.css" rel="stylesheet">
    <title>Error page</title>
</head>
<body>
Request from ${pageContext.errorData.requestURI} is failed <br/>
Servlet name: ${pageContext.errorData.servletName} <br/>
Status code: ${pageContext.errorData.statusCode} <br/>
Exception: ${pageContext.exception} <br/>
Message: ${pageContext.exception.message} <br/>
Stack trace: ${pageContext.exception.stackTrace}
<a href="../../index.jsp">Back to index</a>
</body>
</html>
