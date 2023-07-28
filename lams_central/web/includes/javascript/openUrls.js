// resolution checking
var belowMinRes;
if (screen.width <= 800 && screen.height <= 600) {
	belowMinRes = true;
} else {
	belowMinRes = false;
}

var learner_width = 1280;
var learner_height = 720;

var authorWin = null;
var learnWin = null;
var kumaliveWin = null;
var monitorLessonWin = null;
var addLessonWin = null;
var epWin = null;
var appadminWin = null;
var omWin = null;
var pWin = null;
var copyrightWin = null;
var customWin = null;
var gradebookMonWin = null;
var gradebookLrnWin = null;
var gradebookMonLessonWin = null;

function getCenterParams(width, height) {
	var left = ((screen.width / 2) - (width / 2)),
		// open the window a bit higher than center
		top = ((screen.height / 2) - (height / 2)) / 2;
	return ",top=" + top + ",left=" + left;
}

function closeAllChildren() {
	if (authorWin && !authorWin.closed) authorWin.closeWindow();
	if (learnWin && !learnWin.closed) learnWin.close();
	if (monitorLessonWin && !monitorLessonWin.closed) monitorLessonWin.closeWindow();
	if (addLessonWin && !addLessonWin.closed) addLessonWin.close();
	if (epWin && !epWin.closed) epWin.close();
	if (appadminWin && !appadminWin.closed) appadminWin.close();
	if (omWin && !omWin.closed) omWin.close();
	if (pWin && !pWin.closed) pWin.close();
	if (copyrightWin && !copyrightWin.closed) copyrightWin.close();
	if (gradebookMonWin && !gradebookMonWin.closed) gradebookMonWin.close();
	if (gradebookLrnWin && !gradebookLrnWin.closed) gradebookLrnWin.close();
	if (gradebookMonLessonWin && !gradebookMonLessonWin.closed) gradebookMonLessonWin.close();
}

function openMonitorLesson( lessonID, url ) {
	if (!url) {
		url = '/lams/home/monitorLesson.do?';
	}
	url += 'lessonID=' + lessonID;
	sessionStorage.removeItem("lamsMonitoringCurrentTab");
	window.location.href = url;
}

function openLearner( lessonId, url ) {
	if (!url) {
		url = '/lams/home/learner.do?';
	}
	var learnerUrl = url + 'lessonID=' + lessonId;
	
	if (isMac) {
		learnWin = window.open(learnerUrl,'lWindow','width=' + learner_width + ',height=' + learner_height 
				   + ',resizable,scrollbars=yes,status=yes' + getCenterParams(learner_width, learner_height));
	} else {
		if (learnWin && !learnWin.closed ) {
			learnWin.location = learnerUrl;		
			learnWin.focus();
		} else {
			learnWin = window.open(learnerUrl,'lWindow','width=' + learner_width + ',height=' + learner_height 
					   + ',resizable,scrollbars=yes,status=yes' + getCenterParams(learner_width, learner_height));
		}
	}
}

function openKumalive(orgID, role) {
	var kumaliveUrl = LAMS_URL + '/learning/kumalive/kumalive.jsp?organisationID=' + orgID + '&role=' + role
	
	if (isMac) {
		kumaliveWin = window.open(kumaliveUrl,'kumaliveWindow','width=' + learner_width + ',height=' + learner_height 
					  + ',resizable,scrollbars=yes,status=yes' + getCenterParams(learner_width, learner_height));
	} else {
		if (kumaliveWin && !kumaliveWin.closed) {
			kumaliveWin.location = kumaliveUrl;		
			kumaliveWin.focus();
		} else {
			kumaliveWin = window.open(kumaliveUrl,'kumaliveWindow','width=' + learner_width + ',height=' + learner_height 
						  + ',resizable,scrollbars=yes,status=yes' + getCenterParams(learner_width, learner_height));
		}
	}
}

/**
 * Method designed to open learner window from shortened LAMS learner
 * URLS like /lams/r/*. Works exactly the same as openLearner() method
 * except it opens slightly modified path "../home.do".
 */
function openLearnerShortenedUrl( lessonId ) {
	if (isMac) {
		learnWin = window.open('/lams/home/learner.do?lessonID='+lessonId,'lWindow','width=' + learner_width 
				   + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes'
				   + getCenterParams(learner_width, learner_height));
	} else {
		if (learnWin && !learnWin.closed ) {
			learnWin.location = '/lams/home/learner.do?lessonID='+lessonId;		
			learnWin.focus();
		} else {
			learnWin = window.open('/lams/home/learner.do?lessonID='+lessonId,'lWindow','width='
					   + learner_width + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes'
					   + getCenterParams(learner_width, learner_height));
		}
	}	
}

function openAppadmin() {
	var height = 768;
	var width = 1024;
	var left = 0;
	var top = 0;
	
	if (self.screen) {
		top = (self.screen.height/2-height/2) / 2;
		left = self.screen.width/2-width/2;
	}
	
	if (isMac) {
		appadminWin = window.open('admin/appadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height
					  +',resizable,location,menubar,scrollbars,dependent,status,toolbar');
	} else {
		if (appadminWin && !appadminWin.closed ) {
			appadminWin.location = 'admin/appadminstart.do';
			appadminWin.focus();
		} else {
			appadminWin = window.open('admin/appadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='
					      +height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			appadminWin.focus();
		}
	}
}

function openOrgManagement(orgId) {
	var height = 648;
	var width = 1152;
	var left = 0;
	var top = 0;
	if (self.screen) {
		top = (self.screen.height/2-height/2) / 2;
		left = self.screen.width/2-width/2;
	}
	if (isMac) {
		omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width
				+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
	} else {
		if (omWin && !omWin.closed ) {
			omWin.location = 'admin/orgmanage.do?org='+orgId;
			omWin.focus();
		} else {
			omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width
					+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			omWin.focus();
		}
	}
}	

function openCopyRight() {
	var height = 388;
	var width = 750;
	var left = 0;
	var top = 0;
	if (self.screen) {
		top = (self.screen.height/2-height/2) / 2;
		left = self.screen.width/2-width/2;
	}
	if (isMac) {
		copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top
					   +',width=750,height=388,scrollbars');
	} else {
		if (copyrightWin && !copyrightWin.closed ) {
			copyrightWin.focus();
		} else {
			copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top
						   +',width=750,height=388,scrollbars');
			copyrightWin.focus();
		}
	}
}

function openQbCollections(){
	window.open('qb/collection/show.do', '_blank');
}

function openCustom(url) {
	var left = 0;
	var top = 0;
	if (isMac) {
		customWin = window.open(url,'custom','resizable,left='+left+',top='+top+',scrollbars');
	} else {
		if (customWin && !customWin.closed ) {
			customWin.focus();
		} else {
			customWin = window.open(url,'custom','resizable,left='+left+',top='+top+',scrollbars');
			customWin.focus();
		}
	}
}

function openTeamworkWindow (){
	window.open('teamwork/show.do', '_blank');
}