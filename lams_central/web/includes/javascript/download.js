// Helper methods for downloading files

// Download a file using the given URL.
// If spinnerDiv is set, then displays the spinnerDiv area that should contain a waiting spinner until the file is downloaded
// If resultsMsgDiv and resultsMsg are set, then displays the resultsMsg in the resultsMsgDiv area
// If buttonStyle is set then it disables the button with that style until the file is downloaded.
// Back end must handle "&downloadTokenValue=<token>" on the URL
// Detecting the file download dialog in the browser: 
// http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
var fileDownloadCheckTimer;
function downloadFile(downloadUrl, spinnerDiv, resultsMsg, resultsMsgDiv, buttonStyle) {
	if ( buttonStyle ) {
		$('.'+buttonStyle).prop('disabled', true);
	}
	if ( resultsMsg && resultsMsgDiv ) {
		$('#'+resultsMsgDiv).html('');
	} 
	if ( spinnerDiv ) {
		$('#'+spinnerDiv).show();
	}
	var token = new Date().getTime(); //use the current timestamp as the token value

	fileDownloadCheckTimer = window.setInterval(function () {
		var cookieValue = $.cookie('fileDownloadToken');
		if (cookieValue == token) {
		    //unBlock export button
			window.clearInterval(fileDownloadCheckTimer);
			$.cookie('fileDownloadToken', null); //clears this cookie value
			if ( spinnerDiv ) {
				$('#'+spinnerDiv).hide();
			}
			if ( resultsMsg && resultsMsgDiv ) {
				$('#'+resultsMsgDiv).html(resultsMsg);
			} 
			if ( buttonStyle ) {
				$('.'+buttonStyle).prop('disabled', false);
			}
		}
	}, 1000);
	
	//dynamically create a form and submit it
	var form = $('<form method="post" action="' + downloadUrl + '"></form>');
    var hiddenInput = $('<input type="hidden" name="downloadTokenValue" value=' + token + '></input>');
    form.append(hiddenInput);
    $(document.body).append(form);
    form.submit();

	return false;
}