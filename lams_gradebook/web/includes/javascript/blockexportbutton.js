//Detecting the file download dialog in the browser: 
//http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
var fileDownloadCheckTimer;

function blockExportButton(areaToBlock, exportExcelUrl, labelWait) {
	var token = new Date().getTime(); //use the current timestamp as the token value
		    
	$('#' + areaToBlock).block({
		message: '<h1 style="color:#fff";>' + labelWait + '</h1>',
		baseZ: 1000000,
		fadeIn:  0, 
		css: { 
			border: 'none', 
		    padding: $('#' + areaToBlock).height()/2 + 'px', 
		    backgroundColor: '#000', 
		    '-webkit-border-radius': '10px', 
		    '-moz-border-radius': '10px', 
		    opacity: .98 
		},
		overlayCSS: {
			opacity: 0
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
			
	document.location.href = exportExcelUrl + "&downloadTokenValue=" + token;
	return false;
}