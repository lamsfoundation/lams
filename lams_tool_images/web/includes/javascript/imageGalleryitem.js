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

	    // Check the file type.
		if (file.name.length < 1) {
			alert("file.name.length < 1");
			return;
		} else if (file.size > UPLOAD_FILE_LARGE_MAX_SIZE) {
			alert(LABEL_MAX_FILE_SIZE);
			return;
		} else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
			alert(LABEL_NOT_ALLOWED_FORMAT);
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
    		$('#new-image-input-area').html(data);
    	},
    	error: function(jqXHR, textStatus, errorMessage) {
        	alert(errorMessage);
    	}
	});
} 

function submitMultipleImageGalleryItems(){
	var formData = new FormData();
	formData.append('sessionMapID', $("#sessionMapID").val());
	
	// Get selected files from the inputs
	var validateFiles = [];
	if ($('#file1').val()) { 
		var file = document.getElementById('file1').files[0];
		validateFiles.push(file);
		formData.append('file1', file, file.name);
	}
	if ($('#file2').val()) { 
		var file = document.getElementById('file2').files[0];
		validateFiles.push(file);
		formData.append('file2', file, file.name);
	}
	if ($('#file3').val()) { 
		var file = document.getElementById('file3').files[0];
		validateFiles.push(file);
		formData.append('file3', file, file.name);
	}
	if ($('#file4').val()) {
		var file = document.getElementById('file4').files[0];
		validateFiles.push(file);
		formData.append('file4', file, file.name);
	}
	if ($('#file5').val()) { 
		var file = document.getElementById('file5').files[0];
		validateFiles.push(file);
		formData.append('file5', file, file.name);
	}
		
	// validate files 
	if (validateFiles.length == 0) {
		alert(LABEL_ITEM_BLANK);
		return;
	}
	for (var i = 0; i < validateFiles.length; i++) {
		  var file = validateFiles[i];

		  // Check the file type.
		  if(file.name.length < 1) {
			  alert("file.name.length < 1");
			  return;
		  } else if (file.size > UPLOAD_FILE_LARGE_MAX_SIZE) {
			  alert(LABEL_MAX_FILE_SIZE);
			  return;
		  } else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
			  alert(LABEL_NOT_ALLOWED_FORMAT);
			  return;
		  }

		  // Add the file to the request.
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
    		$('#new-image-input-area').html(data);
    	},
    	error: function(jqXHR, textStatus, errorMessage) {
        	alert(errorMessage);
    	}
	});
} 
