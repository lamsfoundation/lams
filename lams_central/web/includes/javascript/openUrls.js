// resolution checking
var belowMinRes;
if (screen.width <= 800 && screen.height <= 600) {
	belowMinRes = true;
} else {
	belowMinRes = false;
}

var authorWin = null;
var learnWin = null;
var kumaliveWin = null;
var monitorLessonWin = null;
var addLessonWin = null;
var epWin = null;
var sysadminWin = null;
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
	if (sysadminWin && !sysadminWin.closed) sysadminWin.close();
	if (omWin && !omWin.closed) omWin.close();
	if (pWin && !pWin.closed) pWin.close();
	if (copyrightWin && !copyrightWin.closed) copyrightWin.close();
	if (gradebookMonWin && !gradebookMonWin.closed) gradebookMonWin.close();
	if (gradebookLrnWin && !gradebookLrnWin.closed) gradebookLrnWin.close();
	if (gradebookMonLessonWin && !gradebookMonLessonWin.closed) gradebookMonLessonWin.close();
}

function openPedagogicalPlanner() {
	if(isMac) {
			authorWin = window.open('planner/pedagogicalPlanner/start.do','aWindow','width=' + pedagogical_planner_width
						+ ',height=' + pedagogical_planner_height + ',resizable,scrollbars'
						+ getCenterParams(pedagogical_planner_width, pedagogical_planner_height));
	} else {
		if(authorWin && !authorWin.closed && authorWin.location.pathname.indexOf('pedagogicalPlanner/start.do') > -1) {
			authorWin.focus();
		} else {
			authorWin = window.open('planner/pedagogicalPlanner/start.do','aWindow','width=' + pedagogical_planner_width
					+ ',height=' + pedagogical_planner_height + ',resizable,scrollbars'
					+ getCenterParams(pedagogical_planner_width, pedagogical_planner_height));
			authorWin.focus();
		}
	}
}

function returnToMonitorLessonIntegrated( lessonID ) {
	window.location = '/lams/home/monitorLesson.do?lessonID='+lessonID;
}

function openMonitorLesson( lessonID ) {
	if (isMac) {
		if(belowMinRes) {
			monitorLessonWin = window.open('/lams/home/monitorLesson.do?lessonID='+ lessonID,'mWindow','width=' + monitor_width 
							   + ',height=' + monitor_height + ',resizable,scrollbars' + getCenterParams(monitor_width, monitor_height));
		} else {
			monitorLessonWin = window.open('/lams/home/monitorLesson.do?lessonID='+lessonID,'mWindow','width=' + monitor_width
							   + ',height=' + monitor_height + ',resizable,scrollbars' + getCenterParams(monitor_width, monitor_height));
		}
	} else {
		if (monitorLessonWin && !monitorLessonWin.closed) {
			monitorLessonWin.location = '/lams/home/monitorLesson.do?lessonID='+lessonID;
			monitorLessonWin.focus();
		} else {
			monitorLessonWin = window.open('/lams/home/monitorLesson.do?lessonID='+lessonID,'mWindow','width=' + monitor_width
							   + ',height=' + monitor_height + ',resizable,resizable,scrollbars'
							   + getCenterParams(monitor_width, monitor_height));
		}
	}
}

function openLearner( lessonId ) {
	var learnerUrl = '/lams/home/learner.do?lessonID=' + lessonId;
	
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

function openSysadmin() {
	var height = sys_admin_height;
	var width = sys_admin_width;
	var left = 0;
	var top = 0;
	
	if (self.screen) {
		top = (self.screen.height/2-height/2) / 2;
		left = self.screen.width/2-width/2;
	}
	
	if (isMac) {
		sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height
					  +',resizable,location,menubar,scrollbars,dependent,status,toolbar');
	} else {
		if (sysadminWin && !sysadminWin.closed ) {
			sysadminWin.location = 'admin/sysadminstart.do';
			sysadminWin.focus();
		} else {
			sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='
					      +height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			sysadminWin.focus();
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