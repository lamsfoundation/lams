// The DIV to be made full screen must have class="full-screen-content-div" and call setupFullScreenEvents() in the document.ready() method
// The button that triggers full screen must have the class "full-screen-launch-button" and be in "full-screen-content-div"
// The button that exits full screen must have the class "full-screen-exit-button" and be in "full-screen-content-div"

// Increase to full screen - find the right method, call on correct element
function launchIntoFullscreen(button) {
	let element = $(button).closest('.full-screen-content-div')[0];
	if (element.requestFullscreen) {
		element.requestFullscreen();
	} else if (element.mozRequestFullScreen) {
		element.mozRequestFullScreen();
	} else if (element.webkitRequestFullscreen) {
		element.webkitRequestFullscreen();
	} else if (element.msRequestFullscreen) {
		element.msRequestFullscreen();
	}
}

// Reduce full screen back to normal screen
function exitFullscreen() {
	if (document.exitFullscreen) {
		document.exitFullscreen();
	} else if (document.mozCancelFullScreen) {
		document.mozCancelFullScreen();
	} else if (document.webkitExitFullscreen) {
		document.webkitExitFullscreen();
	}
}


// Detect when screen changes and update buttons
function setupFullScreenEvents() {
	let fullscreenEnabled = document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled;
	if (fullscreenEnabled) {
		document.addEventListener("fullscreenchange", fullScreenChanged);
		document.addEventListener("mozfullscreenchange", fullScreenChanged);
		document.addEventListener("webkitfullscreenchange", fullScreenChanged);
		document.addEventListener("msfullscreenchange", fullScreenChanged);
	} else {
		$(".full-screen-exit-button").hide();
		$(".full-screen-launch-button").hide();
	}
}

function fullScreenChanged() {
	var fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;
	if (fullscreenElement && fullscreenElement != null) {
		$(".full-screen-launch-button", fullscreenElement).hide();
		$(".full-screen-exit-button", fullscreenElement).show();
	} else {
		$(".full-screen-exit-button").hide();
		$(".full-screen-launch-button").show();
	}
}