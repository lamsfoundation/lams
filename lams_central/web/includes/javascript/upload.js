// LAMS Helper methods to validate file upload

// returns true if not an executable, shows error message if not valid
function validateShowErrorNotExecutable(file, errorMessage, showFilename, exeListStr, errDivId) {
	clearFileError(errDivId);
	if ( ! validateNotExecutable( file, exeListStr) ) {
		showFileError(showFilename ? file.name + ": " + errorMessage: errorMessage, errDivId);
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
function validateShowErrorImageType(file, errorMessage, showFilename, errDivId) {
    // Check the file type.
	clearFileError(errDivId);
	if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
		showFileError(showFilename ? file.value + ": " + errorMessage: errorMessage, errDivId);
		return false;
	} 
    return true;
}

//returns false if not simple .xls file.
function validateShowErrorSpreadsheetType(file, errorMessage, showFilename, errDivId) {
    // Check the file type.
	clearFileError(errDivId);
	var filename = file.name;
	var isExcel = false;
	if ( filename ) {
		var extname = filename.substr((~-filename.lastIndexOf(".") >>> 0) + 2);
		isExcel = extname.length > 0 && extname.toUpperCase() === "XLS" ;
	} 
	if ( ! isExcel ) {
		showFileError(showFilename ? file.value + ": " + errorMessage: errorMessage, errDivId);
	}
	return isExcel;
}
// file: the file to check
// maxSize: maximum size allowed in bytes
// rawErrorMessage: message text from I18N files. Must contain '{0}' which will be replaced by the size and the units
//  e.g. 'Uploaded file exceeded maximum size: {0}'
function validateShowErrorFileSize(file, maxSize, errorMessage, showFilename, errDivId) {
	if ( file.size > maxSize ) {
		var updatedErrorMessage = errorMessage.replace('{0}', formatBytesForMessage(maxSize) )
		showFileError(showFilename ? file.value + ": " + updatedErrorMessage: updatedErrorMessage, errDivId);
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

function clearFileError(errDivId) {
	if ( ! errDivId || errDivId.length == 0 ) {
		errDivId = 'file-error-msg';
	}
	var errDiv = $('#'+errDivId);
	errDiv.empty();
	errDiv.css( "display", "none" );
}
function showFileError(error, errDivId) {
	if ( ! errDivId || errDivId.length == 0 ) {
		errDivId = 'file-error-msg';
	}
	var errDiv = $('#'+errDivId);
	if ( errDiv.length > 0 ) {
		errDiv.append(error);
		errDiv.css( "display", "block" );
	} else {
		alert(error);
	}
}
