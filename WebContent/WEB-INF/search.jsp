<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Search</title>

<jsp:include page="header.jsp" />
</head>

<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />

	<div class="container">
		<div class="section">
			<h3 class="header red-text">Advanced Search</h3>
		</div>
		<div class="row">
			<form action="/Fabflix/servlet/AdvSearch" method="GET">
				<div class="row">
					<div class="section">
						<h5 class="flow-text">Movie</h5>
					</div>
					<input id="page" name="page" type="hidden" value="1"> <input
						id="show" name="show" type="hidden" value="9">

					<div class="input-field col s4">
						<input id="title" name="title" type="text" class="validate">
						<label for="title">Movie Title</label>
					</div>
					<div class="input-field col s4">
						<input id="year" name="year" type="text" class="validate">
						<label for="year">Year</label>
					</div>
					<div class="input-field col s4">
						<input id="director" name="director" type="text" class="validate">
						<label for="director">Director</label>
					</div>
				</div>

				<div class="row">
					<div class="section">
						<h5 class="flow-text">Star</h5>
					</div>
					<div class="input-field col s4">
						<input id="first_name" name="fn" type="text" class="validate">
						<label for="first_name">First Name</label>
					</div>
					<div class="input-field col s4">
						<input id="last_name" name="ln" type="text" class="validate">
						<label for="last_name">Last Name</label>
					</div>
				</div>

				<input id="SORT" name="SORT" type="hidden" value=""> <input
					id="DESC" name="DESC" type="hidden" value="">

				<button class="btn waves-effect waves-light red" type="submit">Submit</button>
			</form>
		</div>
	</div>
</body>
</html>