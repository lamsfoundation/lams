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
	
	// validate uploading file if we add it for the first time
	if (!eval($("#has-file").val())) {
		var fileSelect = document.getElementById('fileSelector');
		// Get the selected files from the input.
		var files = fileSelect.files;
		
		if (files.length == 0) {
			alert(LABEL_ITEM_BLANK);
			return;
		}
		var file = files[0];

	    // Check the file type and file size
		if ( ! validateShowErrorImageType(file, LABEL_NOT_ALLOWED_FORMAT ) || 
				! validateShowErrorFileSize(file, UPLOAD_FILE_LARGE_MAX_SIZE, LABEL_MAX_FILE_SIZE) ) {
			return;
		}

		// Add the file to the request.
		formData.append('file', file, file.name);
		$('#uploadButtons').hide();
		$('#itemAttachmentArea_Busy').show();
		
	}

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

function submitMultipleImageGalleryItems(){
	var formData = new FormData();
	formData.append('sessionMapID', $("#sessionMapID").val());
	
	// Get selected files from the inputs
	var validateFiles = [];
	var errorDivs = [];
	if ($('#file1').val()) { 
		var file = document.getElementById('file1').files[0];
		validateFiles.push(file);
		errorDivs.push('fileerror1');
		formData.append('file1', file, file.name);
	}
	if ($('#file2').val()) { 
		var file = document.getElementById('file2').files[0];
		validateFiles.push(file);
		errorDivs.push('fileerror2');
		formData.append('file2', file, file.name);
	}
	if ($('#file3').val()) { 
		var file = document.getElementById('file3').files[0];
		validateFiles.push(file);
		errorDivs.push('fileerror3');
		formData.append('file3', file, file.name);
	}
	if ($('#file4').val()) {
		var file = document.getElementById('file4').files[0];
		validateFiles.push(file);
		errorDivs.push('fileerror4');
		formData.append('file4', file, file.name);
	}
	if ($('#file5').val()) { 
		var file = document.getElementById('file5').files[0];
		validateFiles.push(file);
		errorDivs.push('fileerror5');
		formData.append('file5', file, file.name);
	}
		
	// validate files 
	if (validateFiles.length == 0) {
		alert(LABEL_ITEM_BLANK);
		return;
	}
	var valid = true;
	for (var i = 0; i < validateFiles.length; i++) {
	    // Check the file type and file size
		var file = validateFiles[i];
		var errorDivId = errorDivs[i];
		if ( ! validateShowErrorImageType(file, LABEL_NOT_ALLOWED_FORMAT, false, errorDivId ) || 
				! validateShowErrorFileSize(file, UPLOAD_FILE_LARGE_MAX_SIZE, LABEL_MAX_FILE_SIZE, false, errorDivId) ) {
			valid=false;
		}
	}
	if ( ! valid ) {
		return;
	}
	
	$('#uploadButtons').hide();
	$('#itemAttachmentArea_Busy').show();


	$.ajax({
    	type: 'POST',
    	url: saveMultipleImagesUrl,
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
