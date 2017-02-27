// LAMS Helper methods to validate file upload

// returns true if not an executable, shows error message if not valid
function validateShowErrorNotExecutable(file, errorMessage, showFilename, exeListStr) {
	clearFileError();
	if ( ! validateNotExecutable( file, exeListStr) ) {
		showFileError(showFilename ? filename + ": " + errorMessage: errorMessage);
		return false;
	}
	return true;
}

// returns true if not an executable
function validateNotExecutable(file, exeListStr) {
	var filename = file.name;
	var executable = false;
	if ( filename ) {
		var extname = filename.substr((~-filename.lastIndexOf(".") >>> 0) + 2);
		if ( extname.length == 0) {
			executable = false;
		} else {
			extname = extname.toUpperCase();
			extname = "." + extname;
	
			var extList = exeListStr.split(',');
			var arrayLength = extList.length;
			for (var i = 0; i < arrayLength && ! executable; i++) {
				executable = extname === extList[i].toUpperCase();
			}
		}
	}
	return ! executable;
}

// returns false if not an image type, shows error message if not valid
function validateShowErrorImageType(file, errorMessage, showFilename, exeTypes) {
    // Check the file type.
	clearFileError();
	if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
		showFileError(showFilename ? file.name + ": " + errorMessage: errorMessage);
		return false;
	} 
    return true;
}

// file: the file to check
// maxSize: maximum size allowed in bytes
// rawErrorMessage: message text from I18N files. Must contain '{0}' which will be replaced by the size and the units
//  e.g. 'Uploaded file exceeded maximum size: {0}'
function validateShowErrorFileSize(file, maxSize, errorMessage, showFilename) {
	if ( file.size > maxSize ) {
		var updatedErrorMessage = errorMessage.replace('{0}', formatBytesForMessage(maxSize) )
		showFileError(showFilename ? file.name + ": " + updatedErrorMessage: updatedErrorMessage);
		return false;
	} 
	return true;
}

//file: the file to check
//maxSize: maximum size allowed in bytes
function validateFileSize(file, maxSize) {
	return file.size <= maxSize;
}

// rawErrorMessage: message text from I18N files. Must contain '{0}' which will be replaced by the size and the units
function formatBytesForMessage(sizeinBytes) {
	var unit = null;
	if (sizeinBytes >= 1024) {
		sizeinBytes = sizeinBytes / 1024;
	    if (sizeinBytes >= 1024) {
	    	sizeinBytes = sizeinBytes / 1024;
			unit = " MB";
	    } else {
			unit = " KB";
	    }
	} else {
	    unit = " B";
	}
	return parseFloat(sizeinBytes).toFixed(1) + unit;
}

function clearFileError() {
	var errDiv = $("#file-error-msg");
	errDiv.empty();
	errDiv.css( "display", "none" );
}
function showFileError(error) {
	var errDiv = $("#file-error-msg");
	if ( errDiv.size() > 0 ) {
		errDiv.append(error);
		errDiv.css( "display", "block" );
	} else {
		alert(error);
	}
}
