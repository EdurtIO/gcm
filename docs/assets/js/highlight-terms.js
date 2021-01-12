(function () {
	function getQueryVariable(variable) {
		var query = window.location.search.substring(1),
			vars = query.split("&");

		for (var i = 0; i < vars.length; i++) {
			var pair = vars[i].split("=");

			if (pair[0] === variable) {
				return pair[1];
			}
		}
	}

	var term = decodeURIComponent((getQueryVariable("h") || "").replace(/\+/g, "%20"));

	if (term) {
		var contentEl = document.querySelector(window.contentSelector),
			mark = new Mark(contentEl);

		mark.mark(term);

		var firstMark = document.querySelector("mark");
		if (firstMark) {
			firstMark.scrollIntoView({behavior: "smooth", block: "center", inline: "nearest"});
			window.preventAutofocusScroll = true;
		}
	}
})();