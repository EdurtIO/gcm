(function () {
	function debounce(func, wait, immediate) {
		var timeout;

		return function () {
			var context = this,
				args = arguments;

			clearTimeout(timeout);

			timeout = setTimeout(function () {
				timeout = null;
				if (!immediate) {
					func.apply(context, args);
				}
			}, wait);

			if (immediate && !timeout) {
				func.apply(context, args);
			}
		};
	}

	function escapeHtml(html) {
		var text = document.createTextNode(html);
		var p = document.createElement("p");
		p.appendChild(text);
		return p.innerHTML;
	}

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

	function getPreview(query, content, previewLength) {
		previewLength = previewLength || (content.length * 2);

		var parts = query.split(" "),
			loweredContent = content.toLowerCase(),
			match = content.toLowerCase().indexOf(query.toLowerCase()),
			matchLength = query.length,
			preview;

		// Find a relevant location in content
		for (var i = 0; i < parts.length; i++) {
			if (match >= 0) {
				break;
			}

			match = loweredContent.indexOf(parts[i].toLowerCase());
			matchLength = parts[i].length;
		}

		// Create preview
		if (match >= 0) {
			var start = match - (previewLength / 2),
				end = start > 0 ? match + matchLength + (previewLength / 2) : previewLength;

			preview = content.substring(start, end).trim();

			if (start > 0) {
				preview = "..." + preview;
			}

			if (end < content.length) {
				preview = preview + "...";
			}

			// Highlight query parts
			preview = preview.replace(new RegExp("(" + parts.join("|") + ")", "gi"), "<strong>$1</strong>");
		} else {
			// Use start of content if no match found
			preview = content.substring(0, previewLength).trim() + (content.length > previewLength ? "..." : "");
		}

		return preview;
	}

	function displaySearchResults(results, query) {
		var searchResultsEl = document.getElementById("search-results"),
			searchResultsStatusEl = document.getElementById("search-results-status"),
			searchProcessEl = document.getElementById("search-process");

		if (results.length) {
			var resultsHTML = "";
			results.forEach(function (result) {
				var item = window.data[result.ref],
					contentPreview = getPreview(query, item.content, 170),
					urlPreview = getPreview(query, item.url),
					titlePreview = getPreview(query, item.title);

				resultsHTML +=
					"<li>" +
						"<h4 class='search-result-title'><a href='" + item.url.trim() + "?h=" + encodeURIComponent(query).replace(/'/g, "%27") + "'>" + titlePreview + "</a></h4>" +
						"<p class='search-result-url'>" + urlPreview + "</p>" +
						"<p class='search-result-snippet'>" + contentPreview + "</p>" +
					"</li>";
			});

			searchResultsStatusEl.innerHTML = results.length + " result" + (results.length === 1 ? "" : "s") + " for <strong>" + escapeHtml(query) + "</strong>";
			searchResultsEl.innerHTML = resultsHTML;
		} else {
			searchResultsStatusEl.innerHTML = "No results" + (query ? " for <strong>" + escapeHtml(query) + "</strong>" : "");
			searchResultsEl.innerHTML = "";
		}
	}

	function runSearch(query) {
		if (!query) {
			return displaySearchResults([], query);
		}

		var searchResultsStatusEl = document.getElementById("search-results-status");
		searchResultsStatusEl.innerHTML = "Loading results for <strong>" + escapeHtml(query) + "</strong>...";

		var sanitised = query.replace(/(^ +| +$|\*)/g, "").replace(/ +/g, " ");
		if (!sanitised) {
			return displaySearchResults([], sanitised);
		}

		var parts = sanitised.split(" "),
			wildcardQueries = [];

		for (var i = 0; i < parts.length; i++) {
			wildcardQueries.push(parts[i] + " " + parts[i] + "*");
		}

		displaySearchResults(window.index.search(wildcardQueries.join(" ")), sanitised);
	}

	window.index = lunr(function () {
		this.field("id");
		this.field("title", {boost: 2});
		this.field("url");
		this.field("content");

		for (var key in window.data) {
			this.add(window.data[key], {
				boost: window.data[key].boost || 1
			});
		}
	});

	var query = decodeURIComponent((getQueryVariable("q") || "").replace(/\+/g, "%20")),
		searchInputEls = document.getElementsByClassName("search-input");

	for (var i = 0; i < searchInputEls.length; i++) {
		searchInputEls[i].value = query;
	}

	if (searchInputEls.length) {
		searchInputEls[searchInputEls.length - 1].focus();
		searchInputEls[searchInputEls.length - 1].select();
	}

	runSearch(query);

	var contentSearchInputEl = document.getElementById("content-search-input");
	contentSearchInputEl.addEventListener("input", debounce(function () {
		runSearch(contentSearchInputEl.value);
	}, 300));
})();