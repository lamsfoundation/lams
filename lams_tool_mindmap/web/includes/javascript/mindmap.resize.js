/*
 * Resizing functions for Mindmap Tool
 */

function getWindowSize(dim) {
	var myWidth = 0, myHeight = 0;
	if (typeof (window.innerWidth) == 'number') {
		// Non-IE
		myWidth = window.innerWidth;
		myHeight = window.innerHeight;
	} else if (document.documentElement
			&& (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
		// IE 6+ in 'standards compliant mode'
		myWidth = document.documentElement.clientWidth;
		myHeight = document.documentElement.clientHeight;
	} else if (document.body
			&& (document.body.clientWidth || document.body.clientHeight)) {
		// IE 4 compatible
		myWidth = document.body.clientWidth;
		myHeight = document.body.clientHeight;
	}
	// window.alert( 'Width = ' + myWidth );
	// window.alert( 'Height = ' + myHeight );

	if (dim == "height")
		return myHeight;
	else
		return myWidth;
}

function makeNice() {
	var isIE = navigator.appName.indexOf("Microsoft") != -1;
	flash = document.getElementById('flashContent');
	container = document.getElementById('center12');
	if (isIE) {
		flash.style.width = 4;
		flash.style.height = 3;
		flash.style.width = container.scrollWidth;
		flash.style.height = (container.scrollWidth * 0.75);	
	} else {
		flash.style.width = container.scrollWidth+"px";
		flash.style.height = (container.scrollWidth * 0.75)+"px";
	}
}
