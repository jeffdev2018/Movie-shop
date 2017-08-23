<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!--Show Movies-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Checkout</title>

<jsp:include page="header.jsp" />
</head>

<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />

	<jsp:include page="fab.jsp" />


	<div class="container">
		<div class="section">
			<c:if test="${not empty requestScope.error}">
				<div class="chip">
					<i class="fa fa-minus-circle red-text" aria-hidden="true"></i>
					${fn:escapeXml(requestScope.error)}
				</div>
			</c:if>
		</div>
		<div class="section">
			<div class="row">
				<h4 class="flow-text">Purchaser Information</h4>

				<form action="/Fabflix/servlet/ProcessOrder" method="POST">
					<div class="row">
						<div class="input-field col s6">
							<i class="fa fa-user-circle prefix" aria-hidden="true"></i> <input
								id="firstName" name="fn" type="text" class="validate"> <label
								for="firstName">First Name</label>
						</div>
						<div class="input-field col s6">
							<input id="lastName" name="ln" type="text" class="validate">
							<label for="lastName">Last Name</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s6">
							<i class="fa fa-credit-card-alt prefix" aria-hidden="true"></i><label
								for="ccID">Credit Card</label> <input id="ccID" name="ccID"
								type="number" class="validate">
						</div>
						<div class="input-field col s6">
							<label for="ccExp">Expiration</label> <input id="ccExp"
								name="ccExp" type="date" class="datepicker">
						</div>
					</div>

					<button type="submit" class="btn red">Place Order</button>
				</form>
			</div>
		</div>
	</div>
</body>

<script>
	$('.datepicker').pickadate({
		selectMonths : true,
		selectYears : 30,
		format : 'yyyy-mm-dd'
	});
</script>

</html>