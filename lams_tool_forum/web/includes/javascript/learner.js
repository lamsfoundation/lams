function removeAtt(mapID){
	$("#itemAttachmentArea").load(
		removeItemAttachmentUrl + "?sessionMapID="+ mapID,
		{
			sessionMapID: mapID,
			reqID: (new Date()).getTime()
		}
	);
}
			
function validateForm() {
	//in case main characters restriction is ON check it's been fullfilled
	var isMinCharactersEnabled = $("#min-characters-enabled").val()  == "true";
	var charsMissing = $("#char-required-div").html();
				
	var isValid = !isMinCharactersEnabled || isMinCharactersEnabled && (charsMissing == "0");
	if (!isValid) {
		var warningMsg = warning.replace("{0}", charsMissing); 
		alert(warningMsg);
	} else {
		var fileSelect = document.getElementById('attachmentFile');
		if ( fileSelect ) {
			// Get the selected files from the input.
			var files = fileSelect.files;
			if (files.length > 0) {
				var file = files[0];
				isValid = validateShowErrorNotExecutable(file, LABEL_NOT_ALLOWED_FORMAT, false, EXE_FILE_TYPES) && validateShowErrorFileSize(file, UPLOAD_FILE_MAX_SIZE, LABEL_MAX_FILE_SIZE, false);
			}
		}
	}
	
	return isValid;
}

function getNumberOfCharacters(value, isRemoveHtmlTags) {
	
    //HTML tags stripping 
	if (isRemoveHtmlTags) {
		value = value.replace(/&nbsp;/g, ' ').replace(/\n/gi, '').replace(/<\/?[a-z][^>]*>/gi, '');
	}
	
    var wordCount = value ? (value).length : 0;
    return wordCount;
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

/* Used by both Reply and Edit */
function disableSubmitButton() {
	$("#submitButton").attr("disabled", true);
}
function enableSubmitButton() {
	$("#submitButton").removeAttr("disabled");
}

