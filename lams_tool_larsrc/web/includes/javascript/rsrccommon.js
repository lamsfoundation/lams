	function showBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = '';
		}
	}
	function hideBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = 'none';
		}				
	}
	function disableButtons() {
		$('.btn-disable-on-submit').prop('disabled', true);
		$("#saveCancelButtons .btn").prop('disabled', true);
	}
	function enableButtons() {
		$('.btn-disable-on-submit').prop('disabled', false);
		$("#saveCancelButtons .btn").prop('disabled', false);
	}

	/**
	 * Initialised Uppy as the file upload widget
	 */
	function initFileUpload(tmpFileUploadId, extensionValidation, language) {
		  var uppyProperties = {
			  // upload immediately 
			  autoProceed: true,
			  allowMultipleUploads: true,
			  debug: false,
			  restrictions: {
				// taken from LAMS configuration
			    maxFileSize: +UPLOAD_FILE_LARGE_MAX_SIZE,
			    maxNumberOfFiles: 1
			  },
			  meta: {
				  // all uploaded files go to this subdir in LAMS tmp dir
				  // its format is: upload_<userId>_<timestamp>
				  'tmpFileUploadId' : tmpFileUploadId,
				  'largeFilesAllowed' : true
			  },
			  onBeforeFileAdded: extensionValidation
		  };
		  
		  switch(language) {
		  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
			case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
			case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
		  }
		  
		  
		  // global variable
		  uppy = Uppy.Core(uppyProperties);
		  // upload using Ajax
		  uppy.use(Uppy.XHRUpload, {
			  endpoint: LAMS_URL + 'tmpFileUpload',
			  fieldName : 'file',
			  // files are uploaded one by one
			  limit : 1
		  });
		  
		  uppy.use(Uppy.Dashboard, {
			  target: '#image-upload-area',
			  inline: true,
			  height: 300,
			  width: '100%',
			  showProgressDetails : true,
			  hideRetryButton : true,
			  hideCancelButton : true,
			  showRemoveButtonAfterComplete: true,
			  proudlyDisplayPoweredByUppy: false
		  });
		  
		  uppy.use(Uppy.Webcam, {
			  target: Uppy.Dashboard,
			  modes: ['picture']
		  });
		  
		  uppy.on('upload-success', function (file, response) {
			  // if file name was modified by server, reflect it in Uppy
			  file.meta.name = response.body.name;
		  });
		  
		  uppy.on('file-removed', function (file, reason) {
			  if (reason === 'removed-by-user') {
				 // delete file from temporary folder on server
			    $.ajax({
			    	url :  LAMS_URL + 'tmpFileUploadDelete',
			    	data : {
			    		'tmpFileUploadId' : tmpFileUploadId,
			    		'name' : file.meta.name
			    	}
			    })
			  }
		  })
	}
