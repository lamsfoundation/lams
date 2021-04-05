var itemAttachmentTargetDiv = "itemAttachmentArea";
function removeItemAttachment(idx){
    removeItemAttachmentLoading();
	$("#" + itemAttachmentTargetDiv).load(
		removeItemAttachmentUrl,
		{
			reqID: new Date()
		}
	);	    
}

function removeItemAttachmentLoading(){
	showBusy(itemAttachmentTargetDiv);
}
	
function cancelImageGalleryItem(){
	var win = null;
	if (window.hideMessage) { 
		win = window;
	} else if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else {
		win = window.top;
	}
	win.hideMessage();
}

function submitImageGalleryItem(){

	if ( typeof CKEDITOR !== 'undefined' ) {
		for ( instance in CKEDITOR.instances ) {
			CKEDITOR.instances[instance].updateElement();
		}
	}

	var formData = new FormData();
	$.each($('#imageGalleryItemForm').serializeArray(), function(i, field) {
		formData.append(field.name, field.value);
	});

	$.ajax({
    	type: 'POST',
    	url: $("#imageGalleryItemForm").attr('action'),
    	data: formData,
        processData: false,
        contentType: false,
    	success: function(data) {
    		//invoke callback function in case image was successfully uploaded
    		imageUploadedCallback(data);
    	},
    	error: function(jqXHR, textStatus, errorMessage) {
    		$('#uploadButtons').show();
    		$('#itemAttachmentArea_Busy').hide();    
        	alert(errorMessage);
    	}
	});
}

/**
 * Initialise Uppy as the file upload widget
 */
function initFileUpload(tmpFileUploadId, singleFileUpload, language) {
	  var uppyProperties = {
		  // upload immediately 
		  autoProceed: true,
		  allowMultipleUploads: true,
		  debug: false,
		  restrictions: {
			// taken from LAMS configuration
		    maxFileSize: +UPLOAD_FILE_LARGE_MAX_SIZE,
		    // when file gets replaced in an existing item, we can only select one
		    maxNumberOfFiles: singleFileUpload ? 1 : 10,
		    allowedFileTypes: UPLOAD_ALLOWED_EXTENSIONS
		  },
		  meta: {
			  // all uploaded files go to this subdir in LAMS tmp dir
			  // its format is: upload_<userId>_<timestamp>
			  'tmpFileUploadId' : tmpFileUploadId,
			  // for server-side file type validation; consistent with CKEditor parameter
			  'Type' : 'Image',
			  'largeFilesAllowed' : true
		  }
	  };
	  
	  switch(language) {
	  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
		case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
		case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
	  }
	  
	  
	  var uppy = Uppy.Core(uppyProperties);
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