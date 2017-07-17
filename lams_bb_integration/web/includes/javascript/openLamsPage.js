// Open the LAMS Seuence Author Window
function openAuthor(courseId) {
	var authorUrl = '../openLamsPage?method=openAuthor&course_id='  + courseId;

	if(authorWin && !authorWin.closed) {
		try {
			authorWin.focus();
		}catch(e){
			// popups blocked by a 3rd party
			alert("Pop-up windows have been blocked by your browser. Please allow pop-ups for this site and try again");
		}
	} else {
		try {
			authorWin = window.open(authorUrl,'aWindow','width=1280,height=720,resizable,scrollbars=yes');
			authorWin.focus();
		}catch(e){
			// popups blocked by a 3rd party
			alert("Pop-up windows have been blocked by your browser. Please allow pop-ups for this site and try again");
		}
	}
}

// Open the LAMS Seuence Monitor Window       
function openMonitor(courseId, lsid) {
	var monitorUrl = '../openLamsPage?method=openMonitor&course_id=' + courseId + '&lsid=' + lsid;
    if(monitorWin && !monitorWin.closed) {
        try {
            monitorWin.focus();
        } catch(e) {
        	// popups blocked by a 3rd party
        	alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
        }
    } else {
        try {
            monitorWin = window.open(monitorUrl,'aWin','width=1280,height=720,resizable=1,scrollbars=yes');
            monitorWin.opener = self;
            monitorWin.focus();
        } catch(e) {
            // popups blocked by a 3rd party
            alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");    
        }
    }
}