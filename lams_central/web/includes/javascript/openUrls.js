		// resolution checking
		var belowMinRes;
		if (screen.width <= 800 && screen.height <= 600) {
			belowMinRes = true;
		} else {
			belowMinRes = false;
		}
		
		var authorWin = null;
		var learnWin = null;
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
		
		function openProfile() {
			if(isMac) {
				pWin = window.open('profile.do?method=view','pWindow','width=796,height=570,resizable,scrollbars');
			} else {
				if (pWin && !pWin.closed) {
					pWin.location='profile.do?method=view';
					pWin.focus();
				} else {
					pWin = window.open('profile.do?method=view','pWindow','width=796,height=570,resizable,scrollbars');
					pWin.focus();
				}
			}
		}
		
		function openPedagogicalPlanner() {
			if(isMac) {
					authorWin = window.open('pedagogicalPlanner.do','aWindow','width='+pedagogical_planner_width+',height='+pedagogical_planner_height+',resizable,scrollbars');
			} else {
				if(authorWin && !authorWin.closed && authorWin.location.pathname.indexOf('pedagogicalPlanner.do') > -1) {
					authorWin.focus();
				} else {
					authorWin = window.open('pedagogicalPlanner.do','aWindow','width='+pedagogical_planner_width+',height='+pedagogical_planner_height+',resizable,scrollbars');
					authorWin.focus();
				}
			}
		}
		
		function returnToMonitorLessonIntegrated( lessonID ) {
			window.location = 'home.do?method=monitorLesson&lessonID='+lessonID;
		}

		function openMonitorLesson( lessonID ) {
			if (isMac) {
				if(belowMinRes) {
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=' + monitor_width + ',height=' + monitor_height + ',resizable,scrollbars');
				} else {
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=' + monitor_width + ',height=' + monitor_height + ',resizable,scrollbars');
				}
			} else {
				if (monitorLessonWin && !monitorLessonWin.closed) {
					monitorLessonWin.location = 'home.do?method=monitorLesson&lessonID='+lessonID;
					monitorLessonWin.focus();
				} else {
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=' + monitor_width + ',height=' + monitor_height + ',resizable,resizable,scrollbars');
				}
			}
		}
		
		function openAddLesson ( courseID, classID ) {
			if (isMac) {
				if (belowMinRes) {
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=623,height=480,scrollbars');
				} else {
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=610,height=480,scrollbars');
				}
			} else {
				if (addLessonWin && !addLessonWin.closed) {
					addLessonWin.location = 'home.do?method=addLesson&courseID='+courseID+'&classID='+classID;
					addLessonWin.focus();
				} else {
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=610,height=480,scrollbars');
				}
			}
		}

		function openLearner( lessonId ) {
			var learnerUrl = 'home.do?method=learner&lessonID=' + lessonId;
			
			if (isMac) {
				learnWin = window.open(learnerUrl,'lWindow','width=' + learner_width + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes');
			} else {
				if (learnWin && !learnWin.closed ) {
					learnWin.location = learnerUrl;		
					learnWin.focus();
				} else {
					learnWin = window.open(learnerUrl,'lWindow','width=' + learner_width + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes');
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
				learnWin = window.open('../home.do?method=learner&lessonID='+lessonId,'lWindow','width=' + learner_width + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes');
			} else {
				if (learnWin && !learnWin.closed ) {
					learnWin.location = '../home.do?method=learner&lessonID='+lessonId;		
					learnWin.focus();
				} else {
					learnWin = window.open('../home.do?method=learner&lessonID='+lessonId,'lWindow','width=' + learner_width + ',height=' + learner_height + ',resizable,scrollbars=yes,status=yes');
				}
			}	
		}
		
		function openSysadmin() {
			var height = sys_admin_height;
			var width = sys_admin_width;
			var left = 0;
			var top = 0;
			
			if (self.screen) {
				top = self.screen.height/2-height/2;
				left = self.screen.width/2-width/2;
			}
			
			if (isMac) {
				sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			} else {
				if (sysadminWin && !sysadminWin.closed ) {
					sysadminWin.location = 'admin/sysadminstart.do';
					sysadminWin.focus();
				} else {
					sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
					sysadminWin.focus();
				}
			}
		}
		
		function openOrgManagement(orgId) {
			var height = 570;
			var width = 796;
			var left = 0;
			var top = 0;
			if (self.screen) {
				top = self.screen.height/2-height/2;
				left = self.screen.width/2-width/2;
			}
			if (isMac) {
				omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			} else {
				if (omWin && !omWin.closed ) {
					omWin.location = 'admin/orgmanage.do?org='+orgId;
					omWin.focus();
				} else {
					omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
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
				top = self.screen.height/2-height/2;
				left = self.screen.width/2-width/2;
			}
			if (isMac) {
				copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top+',width=750,height=388,scrollbars');
			} else {
				if (copyrightWin && !copyrightWin.closed ) {
					copyrightWin.focus();
				} else {
					copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top+',width=750,height=388,scrollbars');
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
	
		function openGradebookCourseMonitorPopup(winname, url, width, height, left, top) {
			if (gradebookMonWin && !gradebookMonWin.closed ) {
				gradebookMonWin.focus();
			} else {
				gradebookMonWin = window.open(url, "gradebookMonWin",'resizable,left='+left+',top='+top+',width='+width+',height='+height+',scrollbars');
				gradebookMonWin.focus();
			}
		}
		
		function openGradebookLearnerPopup(winname, url, width, height, left, top) {
			if (gradebookLrnWin && !gradebookLrnWin.closed ) {
				gradebookLrnWin.focus();
			} else {
				gradebookLrnWin = window.open(url, "gradebookLrnWin",'resizable,left='+left+',top='+top+',width='+width+',height='+height+',scrollbars');
				gradebookLrnWin.focus();
			}
		}
		
		function openGradebookLessonMonitorPopup(winname, url, width, height, left, top) {
			if (gradebookMonLessonWin && !gradebookMonLessonWin.closed ) {
				gradebookMonLessonWin.focus();
			} else {
				gradebookMonLessonWin = window.open(url, "gradebookMonLessonWin",'resizable,left='+left+',top='+top+',width='+width+',height='+height+',scrollbars');
				gradebookMonLessonWin.focus();
			}
		}
