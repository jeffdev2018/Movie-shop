function updateURLParameter(paramName, paramValue, url) {
	var hash = location.hash;
	url = url.replace(hash, '');
	if (url.indexOf("?") >= 0) {
		var params = url.substring(url.indexOf("?") + 1).split("&");
		var paramFound = false;
		params.forEach(function(param, index) {
			var p = param.split("=");
			if (p[0] == paramName) {
				params[index] = paramName + "=" + paramValue;
				paramFound = true;
			}
		});
		if (!paramFound)
			params.push(paramName + "=" + paramValue);
		url = url.substring(0, url.indexOf("?") + 1) + params.join("&");
	} else
		url += "?" + paramName + "=" + paramValue;
	return url + hash;
}

$(document).ready(function() {
	$('.materialboxed').materialbox();
	$('.dropdown-button').dropdown({
		inDuration : 300,
		outDuration : 225,
		constrainWidth : false, // Does not change width of dropdown to that of
		// the activator
		hover : false, // Activate on hover
		gutter : 0, // Spacing from edge
		belowOrigin : true, // Displays dropdown below the button
		alignment : 'left', // Displays dropdown with edge aligned to the left
		// of button
		stopPropagation : false
	// Stops event propagation
	});
});
