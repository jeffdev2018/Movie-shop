<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Home</title>

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
	<jsp:include page="nav.jsp" />
	<div class="my-wrapper valign-wrapper center-align">
		<div class="row container">
			<div class="col s12 m8 offset-m2 l6 offset-l3">
				<div class="card-panel grey lighten-5 z-depth-1">
					<form>
						<div class="input-field">
							<input id="search" oninput="ajaxFunction()" autocomplete="off"
								type="search" required> <label class="label-icon"
								for="search"><i class="fa fa-search" aria-hidden="true"></i></label>
						</div>
					</form>
					<div id="livesearch"></div>
				</div>
			</div>
		</div>
	</div>
	<script>
		function ajaxFunction() {
			var ajaxRequest; // The variable that makes Ajax possible!

			try {
				// Opera 8.0+, Firefox, Safari
				ajaxRequest = new XMLHttpRequest();
			} catch (e) {
				// Internet Explorer Browsers
				try {
					ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
				} catch (e) {
					try {
						ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
					} catch (e) {
						// Something went wrong
						alert("Your browser broke!");
						return false;
					}
				}
			}
			var x = document.getElementById("search").value;
			ajaxRequest.open("GET", "/Fabflix/servlet/AutoComplete?query=" + x,
					true);
			ajaxRequest.send(null);
			// Create a function that will receive data sent from the server
			ajaxRequest.onreadystatechange = function() {
				if (ajaxRequest.readyState == 4) {
					document.getElementById("livesearch").innerHTML = ajaxRequest.responseText;
				}
			}

		}
	</script>
</body>
</html>