<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Show Star</title>
<jsp:include page="header.jsp" />
</head>
<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />

	<div class="container">
		<div class="section">
			<h4 class="flow-text">Star Info</h4>
		</div>
		<div class="row">
			<c:set var="star" value="${requestScope.star_info}" />
			<div class="col s12">
				<div class="card-panel grey lighten-5 z-depth-1 hoverable">
					<div class="row valign-wrapper">
						<div class="col s4">
							<img class="circle responsive-img" src="${star.photo_url}">
						</div>
						<div class="col s7 push-s2">
							<h1 class="flow-text">
								<strong>Name:</strong> ${star.fn} ${star.ln}
							</h1>
							<h1 class="flow-text">
								<strong>ID:</strong> ${star.id}
							</h1>
							<h1 class="flow-text">
								<strong>Date of Birth:</strong> ${star.dob}
							</h1>
							<br>
							<div class="card-action">
								<a href="https://en.wikipedia.org/wiki/${star.fn}_${star.ln}"
									target="_blank">Read more on wikipedia</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="section">
			<h4 class="flow-text">Related Movies</h4>
		</div>

		<div class="row">
			<c:forEach var="movie" items="${requestScope.movie_results}">
				<div class="col s4">
					<div class="card large grey lighten-5 z-depth-1 hoverable">
						<div class="card-image">
							<img class="materialboxed" src="${movie.banner_url}">
						</div>
						<div class="card-content">
							<span
								class="card-title activator grey-text text-darken-4 truncate">${movie.title}
							</span>
							<p>
								<strong>ID:</strong> ${movie.id}
							</p>
							<p>
								<strong>Year:</strong> ${movie.year}
							</p>
							<p>
								<strong>Director:</strong> ${movie.director}
							</p>
						</div>
						<div class="card-action">
							<a href="${movie.trailer_url}" target="_blank"><i
								class="fa fa-video-camera orange-text link-icon"
								aria-hidden="true"></i> Trailer</a> <a href="#${movie.id}Order"><i
								class="fa fa-shopping-bag orange-text link-icon"
								aria-hidden="true"></i> Buy</a>
						</div>
						<div class="card-reveal">
							<span class="card-title grey-text text-darken-4">${movie.title}
							</span>
							<p>
								<strong>Cast Name(s)</strong>
								<c:forEach var="star" items="${movie.names}">
									<c:set var="fnln" value="${fn:split(star, ':')}" />
									<p>
										<a
											href="/Fabflix/servlet/StarSearch?fn=${fnln[0]}&ln=${fnln[1]}">${fnln[0]}
											${fnln[1]}</a>
									</p>
								</c:forEach>
								<strong>Genre(s)</strong>
								<c:forTokens items="${movie.genres}" delims="," var="g">
									<p>
										<a
											href="/Fabflix/servlet/GenreSearch?page=1&show=9&tag=${g}&SORT=&DESC=">${g}</a>
									</p>
								</c:forTokens>
							</p>
						</div>
					</div>
				</div>
				<!-- Modal Structure -->
				<div id="${movie.id}Order" class="modal">
					<div class="modal-content">
						<h4>Add to Shopping Cart</h4>
						<p>
							You currently have <b>${movie.copies}</b> copies of <b>${movie.title}</b>
						</p>
						<form
							action="/Fabflix/servlet/Purchase?title=${movie.title}&id=${movie.id}"
							method="POST">
							<div class="row">
								<div class="input-field col s1">
									<input id="copies" type="number" min="0" name="copies"
										value="${movie.copies}"> <label for="copies">Copies</label>
								</div>
							</div>
							<br>
							<button type="submit" class="btn red">Add to Cart</button>
						</form>
					</div>
					<div class="modal-footer">
						<a href=""
							class="modal-action modal-close waves-effect waves-red btn-flat">Close</a>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			$('.modal').modal();
		});
	</script>
</body>
</html>