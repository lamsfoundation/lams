//In order to use this script define exportExcelUrl and languageLabelWait variables first.

//Detecting-the-file-download-dialog-in-the-browser: http://geekswithblogs.net/GruffCode/archive/2010/10/28/detecting-the-file-download-dialog-in-the-browser.aspx
		var fileDownloadCheckTimer;
		function blockExportButton() {
		    var token = new Date().getTime(); //use the current timestamp as the token value
		    
			$('#export-link-area').block({
		    	message: '<h1 style="color:#fff";>' + languageLabelWait + '</h1>',
		    	baseZ: 1000000,
		    	fadeIn:  0, 
			    css: { 
		        	border: 'none', 
		        	padding: '3px', 
		        	backgroundColor: '#000', 
		        	'-webkit-border-radius': '10px', 
		        	'-moz-border-radius': '10px', 
		        	opacity: .8 
		    	},
		    	overlayCSS: {
			    	opacity: 0
		    	}
	        });
		    
		    fileDownloadCheckTimer = window.setInterval(function () {
		    	var cookieValue = $.cookie('fileDownloadToken');
		    	if (cookieValue == token) {
		    		unBlockExportButton();
		    	}
		    }, 1000);
			
		    document.location.href = exportExcelUrl + "&downloadTokenValue=" + token;
			return false;
		}
		
		function unBlockExportButton() {
			window.clearInterval(fileDownloadCheckTimer);
			$.cookie('fileDownloadToken', null); //clears this cookie value
			$('#export-link-area').unblock();
		}