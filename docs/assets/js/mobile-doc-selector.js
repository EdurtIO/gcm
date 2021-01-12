var mobileNav = document.getElementById("doc-nav-mobile");

if (mobileNav) {
	var selects = mobileNav.getElementsByTagName("select");

	function selectNavigation(event) {
		window.location = event.target.value;
	}

	for (var i = 0; i < selects.length; i++) {
		selects[i].addEventListener("change", selectNavigation);
	}
}

var sidebar = new StickySidebar('#docs-nav', {
	topSpacing: 99,
	bottomSpacing: 20
});

document.getElementById("doc-path").addEventListener("click", function (event) {
	event.preventDefault();
	document.getElementById("mobile-docs-nav").classList.toggle("open");
	document.getElementsByTagName("BODY")[0].classList.toggle("fixed-position");
});

document.getElementById("close-nav").addEventListener("click", function (event) {
	event.preventDefault();
	document.getElementById("mobile-docs-nav").classList.toggle("open");
	document.getElementsByTagName("BODY")[0].classList.toggle("fixed-position");
});