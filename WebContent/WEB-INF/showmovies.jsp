<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!--Show Movies-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Show Movies</title>

<jsp:include page="header.jsp" />
</head>

<body class="grey lighten-4">
	<jsp:include page="nav.jsp" />

	<jsp:include page="fab.jsp" />
	<div class="container">
		<div class="section">
			<h4 class="header flow-text">Movie Results</h4>
			<c:if test="${fn:length(requestScope.movie_results) != 0}">
				<c:if test="${page ne '1'}">
					<c:if test="${type ne 'AdvSearch' }">
						<a
							href="${base_url}?page=${prev}&show=${show}&tag=${tag}&SORT=${sort}&DESC=${desc}"
							class="btn red"><i class="fa fa-arrow-left"
							aria-hidden="true"></i></a>
					</c:if>
					<c:if test="${type == 'AdvSearch' }">
						<a
							href="${base_url}?page=${prev}&show=${show}&title=${title}&year=${year}&director=${director}&fn=${fn}&ln=${ln}&SORT=${sort}&DESC=${desc}"
							class="btn red"><i class="fa fa-arrow-left"
							aria-hidden="true"></i></a>
					</c:if>
				</c:if>
				<c:if test="${page ne maxPage}">
					<c:if test="${type ne 'AdvSearch' }">
						<a
							href="${base_url}?page=${next}&show=${show}&tag=${tag}&SORT=${sort}&DESC=${desc}"
							class="btn red"><i class="fa fa-arrow-right"
							aria-hidden="true"></i></a>
					</c:if>
					<c:if test="${type == 'AdvSearch' }">
						<a
							href="${base_url}?page=${next}&show=${show}&title=${title}&year=${year}&director=${director}&fn=${fn}&ln=${ln}&SORT=${sort}&DESC=${desc}"
							class="btn red"><i class="fa fa-arrow-right"
							aria-hidden="true"></i></a>
					</c:if>
				</c:if>
			</c:if>
			<!-- Dropdown Trigger -->
			<a class='dropdown-button btn red' href='#'
				data-activates='num_listing'><i class="fa fa-chevron-down"
				aria-hidden="true"></i></a>
			<!-- Dropdown Structure -->
			<ul id='num_listing' class='dropdown-content'>
				<li><a id="page-url-s9" href="#">9</a></li>
				<li><a id="page-url-s18" href="#">18</a></li>
				<li><a id="page-url-s27" href="#">27</a></li>
				<li><a id="page-url-s36" href="#">36</a></li>
			</ul>
		</div>
		<div class="row">
			<c:forEach var="movie" items="${requestScope.movie_results}">
				<div class="col s4">
					<div class="card grey lighten-5 z-depth-1 hoverable">
						<div class="card-image">
							<img src="${movie.banner_url}" width="300" height="500">
						</div>
						<div class="card-content">
							<span
								class="card-title activator grey-text text-darken-4 truncate">${movie.title}
							</span>
						</div>
						<div class="card-reveal">
							<span class="card-title grey-text text-darken-4">${movie.title}
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
							<div class="card-action">
								<a href="${movie.trailer_url}" target="_blank"><i
									class="fa fa-video-camera orange-text link-icon"
									aria-hidden="true"></i> Trailer</a> <a href="#${movie.id}Order"><i
									class="fa fa-shopping-bag orange-text link-icon"
									aria-hidden="true"></i> Buy</a>
							</div>
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
</body>

<script>
	$(document).ready(function() {
		$('.modal').modal();

		$('.card').hover(function() {
			$(this).find('> .card-content > .activator').click();
		}, function() {
			$(this).find('> .card-reveal > .card-title').click();
		});
	});

	str1 = updateURLParameter('SORT', 'title', window.location.href);
	str1 = updateURLParameter('DESC', 'false', str1);

	str2 = updateURLParameter('SORT', 'title', window.location.href);
	str2 = updateURLParameter('DESC', 'true', str2);

	str3 = updateURLParameter('SORT', 'year', window.location.href);
	str3 = updateURLParameter('DESC', 'false', str3);

	str4 = updateURLParameter('SORT', 'year', window.location.href);
	str4 = updateURLParameter('DESC', 'true', str4);

	n1 = updateURLParameter('show', '9', window.location.href);
	n1 = updateURLParameter('page', '1', n1);
	n2 = updateURLParameter('show', '18', window.location.href);
	n2 = updateURLParameter('page', '1', n2);
	n3 = updateURLParameter('show', '27', window.location.href);
	n3 = updateURLParameter('page', '1', n3);
	n4 = updateURLParameter('show', '36', window.location.href);
	n4 = updateURLParameter('page', '1', n4);

	base_url = window.location.href.split(/[?#]/)[0];

	document.getElementById('page-url-title-asc').href = str1;
	document.getElementById('page-url-title-des').href = str2;
	document.getElementById('page-url-year-asc').href = str3;
	document.getElementById('page-url-year-des').href = str4;
	document.getElementById('page-url-s9').href = n1;
	document.getElementById('page-url-s18').href = n2;
	document.getElementById('page-url-s27').href = n3;
	document.getElementById('page-url-s36').href = n4;

	document.getElementById('base_url').href = base_url;
</script>

</html>