<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Employee Dashboard</title>

<jsp:include page="header.jsp" />

</head>
<body class="grey lighten-4">
	<nav class="navbar-fixed">
	<div class="nav-wrapper red">
		<a href="#" class="brand-logo flow-text">Fabflix</a>
		<ul id="nav-mobile" class="right hide-on-med-and-down">
			<li><a href="/Fabflix/servlet/LogOutEmployee">Log Out</a></li>
		</ul>
	</div>
	</nav>
	<div class="container">
		<h3 class="header flow-text">Employee Dashboard</h3>
		<div class="section">
			<a class="btn waves-effect waves-light red" href="#result">Metadata</a>
		</div>
		<div class="section">
			<c:if test="${not empty requestScope.error}">
				<div class="chip">
					<i class="fa fa-minus-circle red-text" aria-hidden="true"></i>
					${fn:escapeXml(requestScope.error)}
				</div>
			</c:if>
		</div>
		<form action="/Fabflix/servlet/_Dashboard" method="POST">
			<div class="row">
				<div class="section">
					<h5 class="flow-text">Insert Star</h5>
				</div>
				<input id="page" name="method" type="hidden" value="insertstar">
				<div class="input-field col s3">
					<input id="fn" name="fn" type="text" class="validate"> <label
						for="title">First Name</label>
				</div>
				<div class="input-field col s3">
					<input id="ln" name="ln" type="text" class="validate"> <label
						for="year">Last Name</label>
				</div>
			</div>
			<button class="btn waves-effect waves-light red" type="submit">Submit</button>
		</form>
		<div class="section"></div>
		<form action="/Fabflix/servlet/_Dashboard" method="POST">
			<div class="row">
				<div class="section">
					<h5 class="flow-text">Add Movie</h5>
				</div>
				<input id="page" name="method" type="hidden" value="addmovie">
				<div class="input-field col s3">
					<input id="title" name="title" type="text" class="validate">
					<label for="title">Movie Title</label>
				</div>
				<div class="input-field col s3">
					<input id="year" name="year" type="text" class="validate">
					<label for="title">Movie Year</label>
				</div>
				<div class="input-field col s3">
					<input id="dir" name="dir" type="text" class="validate"> <label
						for="title">Movie Director</label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s3">
					<input id="banner_url" name="banner_url" type="text"
						class="validate"> <label for="title">Banner Url</label>
				</div>
				<div class="input-field col s3">
					<input id="trailer_url" name="trailer_url" type="text"
						class="validate"> <label for="title">Trailer Url</label>
				</div>
				<div class="input-field col s3">
					<input id="genre" name="genre" type="text" class="validate">
					<label for="title">Genre</label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s3">
					<input id="fn" name="fn" type="text" class="validate"> <label
						for="title">Actor First Name</label>
				</div>
				<div class="input-field col s3">
					<input id="ln" name="ln" type="text" class="validate"> <label
						for="year">Actor Last Name</label>
				</div>
			</div>
			<button class="btn waves-effect waves-light red" type="submit">Submit</button>
		</form>
	</div>

	<!-- Modal Structure -->
	<div id="result" class="modal">
		<div class="modal-content">${metadata}</div>
	</div>
</body>
<script>
	$(document).ready(function() {
		$('.modal').modal();
	});
</script>
</html>
