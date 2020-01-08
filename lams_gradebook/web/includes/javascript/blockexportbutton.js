//Detecting the file download dialog in the browser: 
//http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
var fileDownloadCheckTimer;

function blockExportButton(areaToBlock, exportExcelUrl, labelWait, smallVersion) {
	var token = new Date().getTime(); //use the current timestamp as the token value
	var fontSize = smallVersion ? '12px' : "16px";

	$('#' + areaToBlock).block({
		message: '<span style="font-size: '+fontSize+';color:#fff";>' + labelWait + '</span>',
		baseZ: 1000000,
		fadeIn:  0, 
		css: { 
			border: 'none', 
		    padding: '5px', 
		    backgroundColor: '#000', 
		    '-webkit-border-radius': '5px', 
		    '-moz-border-radius': '5px', 
		    opacity: .98 
		},
		overlayCSS: {
			opacity: 0.3
		}
	});
		    
	fileDownloadCheckTimer = window.setInterval(function () {
		var cookieValue = $.cookie('fileDownloadToken');
		if (cookieValue == token) {
		    //unBlock export button
			window.clearInterval(fileDownloadCheckTimer);
			$.cookie('fileDownloadToken', null); //clears this cookie value
			$('#' + areaToBlock).unblock();
		}
	}, 1000);
	
	//dynamically create a form and submit it
	var form = $('<form></form>');
    form.attr("method", "post");
    form.attr("action", exportExcelUrl);
    
    var hiddenField = $('<input></input>');
    hiddenField.attr("type", "hidden");
    hiddenField.attr("name", "downloadTokenValue");
    hiddenField.attr("value", token);
    form.append(hiddenField);

    // The form needs to be a part of the document in order to be submitted
    $(document.body).append(form);
    form.submit();
	
	return false;
}
