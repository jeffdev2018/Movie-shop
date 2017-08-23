<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!--Show Movies-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Confirmation</title>

<jsp:include page="header.jsp" />
</head>

<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />

	<jsp:include page="fab.jsp" />
	<div class="container">
		<div class="section">
			<h4 class="flow-text">Congratulations, your order has been
				confirmed.</h4>
		</div>
		<div class="section">
			<div class="row">
				<ul class="collection with-header">
					<li class="collection-header"><h4>Shopping Cart</h4></li>
					<c:forEach var="item" items="${sessionScope.cart}">
						<li class="collection-item">
							<div>
								${item.key}<a class="secondary-content">${item.value}</a>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>