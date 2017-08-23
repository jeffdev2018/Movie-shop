<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!--Show Movies-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Shopping Cart</title>

<jsp:include page="header.jsp" />
</head>

<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />
	<div class="container">
		<div class="section">
			<div class="row">
				<h3 class="header red-text">Shopping Cart</h3>
				<p>To remove an item from your cart, simply set the number of
					copies to 0</p>
				<c:forEach var="item" items="${sessionScope.cart}">
					<form id="${item.key}Quantity"
						action="/Fabflix/servlet/Purchase?title=${item.key}" method="POST">
						<div class="row valign-wrapper">
							<div class="col s10">
								<h5>${item.key}</h5>
							</div>
							<div class="input-field col s1 valign">
								<input id="copies" type="number" min="0" name="copies"
									value="${item.value}"> <label for="copies">Copies</label>
							</div>
							<div class="col s1 valign">
								<a href="javascript:{}"
									onclick="document.getElementById('${item.key}Quantity').submit(); return false;"
									class="red-text text-darken-1"><i class="fa fa-check"
									aria-hidden="true"></i></a>
							</div>
						</div>
					</form>
				</c:forEach>
			</div>
			<c:if test="${fn:length(sessionScope.cart)  != 0}">
				<div class="row">
					<div class="col offset-s9 s3">
						<a href="/Fabflix/servlet/Checkout" class="btn red"><span
							class="truncate"><i class="fa fa-credit-card link-icon"
								aria-hidden="true"></i>Checkout</span></a>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>