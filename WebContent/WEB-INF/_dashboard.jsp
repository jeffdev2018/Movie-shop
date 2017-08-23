<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Employee Login</title>

<jsp:include page="header.jsp" />
<style>
html, body, .my-wrapper {
	height: 90%;
}

body {
	background-image: url("/Fabflix/images/bg1.jpg");
}
</style>
</head>
<body class="grey darken-4">
	<div class="my-wrapper valign-wrapper center-align">
		<div class="row container">
			<div class="col s12 m8 offset-m2 l6 offset-l3">
				<div class="card-panel grey lighten-5 z-depth-1">
					<h3 class="flow-text red-text">Employee Login</h3>
					<c:if test="${not empty requestScope.error}">
						<div class="chip">
							<i class="fa fa-minus-circle red-text" aria-hidden="true"></i>
							${fn:escapeXml(requestScope.error)}
						</div>
					</c:if>
					<form action="/Fabflix/servlet/EmployeeLogin" method="POST">
						<div class="input-field">
							<input id="user" type="text" class="validate" name="usr">
							<label for="user">E-mail</label>
						</div>
						<div class="input-field">
							<input id="password" type="password" class="validate" name="pwd">
							<label for="password">Password</label>
						</div>

						<button type="SUBMIT" class="center-align btn red flow-text">Log
							In</button>
					</form>
					<br> <a href="/Fabflix/">Log in as an customer?</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
