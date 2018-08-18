	// The DIV to be made full screen must have id="fullPageContentDiv" and call setupFullScreenEvents(); in the document.ready() method
	// The button that triggers full screen must have the class "launch-fullscreen"
	// The button that exits full screen must have the class "exit-fullscreen"

	// Increase to full screen - find the right method, call on correct element
	function launchIntoFullscreen() {
		var element = document.getElementById("fullPageContentDiv");		
		if(element.requestFullscreen) {
			element.requestFullscreen();
		} else if(element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if(element.webkitRequestFullscreen) {
			element.webkitRequestFullscreen();
		} else if(element.msRequestFullscreen) {
			element.msRequestFullscreen();
		}
	}

	// Reduce full screen back to normal screen
	function exitFullscreen() {
	  if(document.exitFullscreen) {
	    document.exitFullscreen();
	  } else if(document.mozCancelFullScreen) {
	    document.mozCancelFullScreen();
	  } else if(document.webkitExitFullscreen) {
	    document.webkitExitFullscreen();
	  }
	}
	
	
	// Detect when screen changes and update buttons
	function setupFullScreenEvents() {
		fullscreenEnabled = document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled;
		if ( fullscreenEnabled ) {
			document.addEventListener("fullscreenchange", fullScreenChanged);
			document.addEventListener("mozfullscreenchange", fullScreenChanged);
			document.addEventListener("webkitfullscreenchange", fullScreenChanged);
			document.addEventListener("msfullscreenchange", fullScreenChanged);
		} else {
 			$(".exit-fullscreen").hide();
			$(".launch-fullscreen").hide();
 		}
	}
	
	function fullScreenChanged( event ) {
		var fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;
		if ( fullscreenElement && fullscreenElement != null ) {
			$(".launch-fullscreen").hide();
			$(".exit-fullscreen").show();
	    } else {
			$(".exit-fullscreen").hide();
			$(".launch-fullscreen").show();
	    }
	}