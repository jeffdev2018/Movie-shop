<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Basic Browse</title>

<jsp:include page="header.jsp" />
<style>
html, body, .my-wrapper {
	height: 80%;
}
</style>
</head>
<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />
	<div class="my-wrapper center-align valign-wrapper">
		<div class="row container">
			<div class="col s12">
				<h3 class="header red-text">Browse by Title</h3>
			</div>
			<div class="col s12">
				<c:forEach var="option" items="${requestScope.browse_options}">
					<div class="col s2">
						<a class="waves-effect btn-flat btn-large"
							href="/Fabflix/servlet/BasicSearch?page=1&show=9&tag=${option}&SORT=&DESC=">${option}</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>