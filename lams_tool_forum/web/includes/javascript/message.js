	/*
	 This is Resource Item instrcution area.
	 */
    var itemAttachmentTargetDiv = "itemAttachmentArea";
// Please set these 1 variables in JSP file for using tag reason:
//removeItemAttachmentUrl
	function removeItemAttachment(){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url= removeItemAttachmentUrl;
	    var reqIDVar = new Date();
	    var param = "reqID="+reqIDVar.getTime();
		var data = {
			'reqID=' : reqIDVar.getTime()
		};
		data[csrfTokenName] = csrfTokenValue;
	    removeItemAttachmentLoading();
	    
		$.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(data) {
            	$("#"+itemAttachmentTargetDiv).html(data);
            	removeItemAttachmentComplete();
            }
        });
	}
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}

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
	
	function highlightMessage() {
		$('.highlight').filter($('table')).css('background','none');
		$('.highlight').filter($('div')).switchClass('highlight', '', 6000);
		// monitor messages have bg-info class - need to fade from highlight yellow to that, otherwise from yellow to nothing
		$('.highlight').filter($('div')).children('.bg-info').removeClass('bg-info').switchClass('highlight', 'bg-info', 6000);

	}

	function setupJRating(rateMessagePath) {
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
			phpPath : rateMessagePath,
			rateMax : 5,
			decimalLength : 1,
			onSuccess : function(data, messageId){
				$("#averageRating" + messageId).html(data.averageRating);
				$("#numberOfVotes" + messageId).html(data.numberOfVotes);
				$("#numOfRatings").html(data.numOfRatings);

				//disable rating feature in case maxRate limit reached
				if (data.noMoreRatings) {
					$(".rating-stars").each(function() {
						$(this).jRating('readOnly');
					});
				}
			},
			onError : function(){
				jError('Error : please retry');
			}
		});
		$(".rating-stars-new").filter($(".rating-stars-disabled")).jRating({
			rateMax : 5,
			isDisabled : true
		});
		$(".rating-stars-new").removeClass("rating-stars-new");
	}
	
	/**
	 * Initialised Uppy as the file upload widget
	 */
	function initFileUpload(tmpFileUploadId, largeFilesAllowed, language) {
		  var uppyProperties = {
			  // upload immediately 
			  autoProceed: true,
			  allowMultipleUploads: true,
			  debug: false,
			  restrictions: {
				// taken from LAMS configuration
			    maxFileSize: largeFilesAllowed ?  +UPLOAD_FILE_LARGE_MAX_SIZE : +UPLOAD_FILE_MAX_SIZE,
			    maxNumberOfFiles: 1 
			  },
			  meta: {
				  // all uploaded files go to this subdir in LAMS tmp dir
				  // its format is: upload_<userId>_<timestamp>
				  'tmpFileUploadId' : tmpFileUploadId,
				  'largeFilesAllowed' : largeFilesAllowed
			  },
			  onBeforeFileAdded: function(currentFile, files) {
				  var name = currentFile.data.name,
				  	  extensionIndex = name.lastIndexOf('.'),
				  	  valid = extensionIndex < 0 || !EXE_FILE_TYPES.includes(name.substring(extensionIndex).trim().toLowerCase());
				  if (!valid) {
					  uppy.info(EXE_FILE_ERROR, 'error', 10000);
				  }
				  
				  return valid;
		    }
		  };
		  
		  switch(language) {
		  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
			case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
			case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
			case 'it' : uppyProperties.locale = Uppy.locales.it_IT; break; 			
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
			  height: 200,
			  width: '100%',
			  showProgressDetails : true,
			  hideRetryButton : true,
			  hideCancelButton : true,
			  showRemoveButtonAfterComplete: true,
			  proudlyDisplayPoweredByUppy: false	  
			});
		  
		  uppy.use(Uppy.Informer, {
			  target: '#image-upload-area'
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
		  
		  //assign uppy's button id, so we can assign label to it
		  $("#image-upload-area button").first().attr("id","uppy-upload-button");
	}
