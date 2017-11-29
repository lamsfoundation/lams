//Script taken from https://github.com/mdn/samples-server/blob/master/s/webrtc-capturestill/capture.js
//Last commit date - Mar 11, 2015.

(function() {
  // The width and height of the captured photo. We will set the
  // width to the value defined here, but the height will be
  // calculated based on the aspect ratio of the input stream.

  var width = 0;    // We will scale the photo width to this
  var height = 0;     // This will be computed based on the input stream

  // |streaming| indicates whether or not we're currently streaming
  // video from the camera. Obviously, we start at false.

  var streaming = false;

  // The various HTML elements we need to configure or control. These
  // will be set by the startup() function.

  var video = null;
  var canvas = null;
  var photo = null;
  var startbutton = null;
  //*LAMS* var added by LAMS. It will hold a URL representing the webcamera picture. It gets created by URL.createObjectURL() method.
  var objectURL;

  function startup() {
    video = document.getElementById('video');
    canvas = document.getElementById('canvas');
    photo = document.getElementById('photo');
    startbutton = document.getElementById('startbutton');
    
    //*LAMS* onload event added by LAMS
    photo.onload = function() {
    	if (typeof objectURL !== 'undefined') {
    		// no longer need to read the blob so it's revoked
    		URL.revokeObjectURL(objectURL);
    	}
    };

    navigator.getMedia = ( navigator.getUserMedia ||
                           navigator.webkitGetUserMedia ||
                           navigator.mozGetUserMedia ||
                           navigator.msGetUserMedia);

    navigator.getMedia(
      {
        video: true,
        audio: false
      },
      function(stream) {
        if (navigator.mozGetUserMedia) {
          video.mozSrcObject = stream;
        } else {
          var vendorURL = window.URL || window.webkitURL;
          video.src = vendorURL.createObjectURL(stream);
        }
        video.play();
      },
      function(err) {
        console.log("An error occured! " + err);
      }
    );

    video.addEventListener('canplay', function(ev){
      if (!streaming) {
    	  	//*LAMS* modified by LAMS
    	  	var videoHeight = video.videoHeight;
    	  	var videoWidth = video.videoWidth;
    	  	if (videoWidth > videoHeight) {
    	  		height = PORTRAIT_SIZE;
    	  		width =  (videoWidth*height)/videoHeight;
    	  	} else {
    	  		width = PORTRAIT_SIZE;
    	  		height = (videoHeight*width)/videoWidth;   	  		
    	  	}  
      
        // Firefox currently has a bug where the height can't be read from
        // the video, so we will make assumptions if this happens.
      
        if (isNaN(height)) {
          height = width / (4/3);
        }
      
        video.setAttribute('width', width);
        video.setAttribute('height', height);
        canvas.setAttribute('width', width);
        canvas.setAttribute('height', height);
        streaming = true;
      }
    }, false);

    startbutton.addEventListener('click', function(ev){
      takepicture();
      ev.preventDefault();
    }, false);
    
    clearphoto();
  }

  // Fill the photo with an indication that none has been
  // captured.

  function clearphoto() {
	//*LAMS* reimplemented this method
	  $("#still-portrait").hide();
  }
  
  // Capture a photo by fetching the current contents of the video
  // and drawing it into a canvas, then converting that to a PNG
  // format data URL. By drawing it on an offscreen canvas and then
  // drawing that to the screen, we can change its size and/or apply
  // other changes before drawing it.

  function takepicture() {
    var context = canvas.getContext('2d');
    if (width && height) {
      canvas.width = width;
      canvas.height = height;
      context.drawImage(video, 0, 0, width, height);
      
      //*LAMS* added by LAMS. Creates a Blob object representing the image contained in the canvas. Which we then display in photo img.
      canvas.toBlob(function(blob) {
    	  	$("#still-portrait").show();
    	  	$('html, body').animate({
            scrollTop: $("#still-portrait").offset().top
        }, 2000);

    	  	//create object URL
        objectURL = URL.createObjectURL(blob);
        
        //display croppie
        if (!croppieWidget) {
	  	  	croppieWidget = $('#photo').croppie({
	  	  	    viewport: {
	  	  	        width: PORTRAIT_SIZE,
	  	  	        height: PORTRAIT_SIZE
	  	  	    },
	  	  	    boundary: { width: width, height: height },
	  	  	});
        }
  	  	croppieWidget.croppie('bind', {
  	  	    url: objectURL
  	  	});
      },
      "image/jpeg",
      1
      );
      
      //*LAMS* commented out by LAMS
      //var data = canvas.toDataURL('image/png');
      //photo.setAttribute('src', data);
    } else {
      clearphoto();
    }
  }

  // Set up our event listener to run the startup process
  // once loading is complete.
  window.addEventListener('load', startup, false);
})();