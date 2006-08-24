	<!--
		// resolution checking
		var belowMinRes;
		if(screen.width <= 800 && screen.height <= 600){
			belowMinRes = true;
		}else{
			belowMinRes = false;
		}
		
		var authorWin = null;
		var learnWin = null;
		var monitorLessonWin = null;
		var addLessonWin = null;
		var epWin = null;
		var sysadminWin = null;
		var omWin = null;
		var pWin = null
		
		function closeAllChildren(){
			if (authorWin && authorWin.open && !authorWin.closed) authorWin.close();
			if (learnWin && learnWin.open && !learnWin.closed) learnWin.close();
			if (monitorLessonWin && monitorLessonWin.open && !monitorLessonWin.closed) monitorLessonWin.close();
			if (addLessonWin && addLessonWin.open && !addLessonWin.closed) addLessonWin.close();
			if (epWin && epWin.open && !epWin.closed) epWin.close();
			if (sysadminWin && sysadminWin.open && !sysadminWin.closed) sysadminWin.close();
			if (omWin && omWin.open && !omWin.closed) omWin.close();
			if (pWin && pWin.open && !pWin.closed) pWin.close();
			
		}
		
		function openProfile(username)
		{
			if(isMac)
			{
				pWin = window.open('profile.do?method=view','pWindow','width=796,height=570,resizable,scrollbars');
			}
			else
			{
				if(pWin && pWin.open && !pWin.closed)
				{
					pWin.location='profile.do?method=view';
					pWin.focus();
				}
				else
				{
					pWin = window.open('profile.do?method=view','pWindow','width=796,height=570,resizable,scrollbars');
					pWin.focus();
				}
			}
		}

		function openAuthor()
		{
			if(isMac)
			{
				authorWin = window.open('home.do?method=author','aWindow','width=796,height=570,resizable');
			}
			else
			{
				if(authorWin && authorWin.open && !authorWin.closed)
				{
					//authorWin.location = 'home.do?method=author';
					authorWin.focus();
				}
				else
				{
					authorWin = window.open('home.do?method=author','aWindow','width=796,height=570,resizable');
					authorWin.focus();
				}
			}
		}

		function openMonitorLesson( lessonID )
		{
			if(isMac)
			{
				if(belowMinRes)
				{
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=796,height=575,resizable,scrollbars');
				}
				else
				{
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=779,height=575,resizable,scrollbars');
				}
			}
			else
			{
				if(monitorLessonWin && monitorLessonWin.open && !monitorLessonWin.closed)
				{
					monitorLessonWin.location = 'home.do?method=monitorLesson&lessonID='+lessonID;
					monitorLessonWin.focus();
				}else{
					monitorLessonWin = window.open('home.do?method=monitorLesson&lessonID='+lessonID,'mWindow','width=779,height=575,resizable,resizable,scrollbars');
				}
			}
		}
		
		function openAddLesson( courseID, classID )
		{
			if(isMac)
			{
				if(belowMinRes)
				{
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=623,height=450,scrollbars');
				}
				else
				{
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=610,height=450,scrollbars');
				}
			}
			else
			{
				if(addLessonWin && addLessonWin.open && !addLessonWin.closed)
				{
					addLessonWin.location = 'home.do?method=addLesson&courseID='+courseID+'&classID='+classID;
					addLessonWin.focus();
				}
				else
				{
					addLessonWin = window.open('home.do?method=addLesson&courseID='+courseID+'&classID='+classID,'alWindow','width=610,height=450,scrollbars');
				}
			}
		}

		function openLearner( lessonId )
		{
			if(isMac)
			{
				learnWin = window.open('home.do?method=learner&lessonID='+lessonId,'lWindow','width=796,height=570,resizable,status=yes');
			}
			else
			{
				if(learnWin && learnWin.open && !learnWin.closed )
				{
					learnWin.location = 'home.do?method=learner&lessonID='+lessonId;		
					learnWin.focus();
				}
				else
				{
					learnWin = window.open('home.do?method=learner&lessonID='+lessonId,'lWindow','width=796,height=570,resizable,status=yes');
				}
			}
		}
		
		function openExportPortfolio( lessonId )
		{
			if(isMac)
			{
				epWin = window.open('learning/exportWaitingPage.jsp?mode=learner&lessonID='+lessonId,'epWindow','width=796,height=570,resizable,status=yes');
			}
			else
			{
				if(epWin && epWin.open && !epWin.closed )
				{
					epWin.location = 'learning/exportWaitingPage.jsp?mode=learner&lessonID='+lessonId;		
					ep.focus();
				}
				else
				{
					epWin = window.open('learning/exportWaitingPage.jsp?mode=learner&lessonID='+lessonId,'epWindow','width=796,height=570,resizable,status=yes');
				}
			}
		}
		
		function openSysadmin()
		{
			var height = 650;
			var width = 850;
			var left = 0;
			var top = 0;
			if(self.screen){
				top = self.screen.height/2-height/2;
				left = self.screen.width/2-width/2;
			}
			if(isMac)
			{
				sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(sysadminWin && sysadminWin.open && !sysadminWin.closed )
				{
					sysadminWin.location = 'admin/sysadminstart.do';
					sysadminWin.focus();
				}
				else
				{
					sysadminWin = window.open('admin/sysadminstart.do','saWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
					sysadminWin.focus();
				}
			}
		}
		
		function openOrgManagement(orgId)
		{
			var height = 570;
			var width = 796;
			var left = 0;
			var top = 0;
			if(self.screen){
				top = self.screen.height/2-height/2;
				left = self.screen.width/2-width/2;
			}
			if(isMac)
			{
				omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(omWin && omWin.open && !omWin.closed )
				{
					omWin.location = 'admin/orgmanage.do?org='+orgId;
					omWin.focus();
				}
				else
				{
					omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left='+left+',top='+top+',width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
					omWin.focus();
				}
			}
		}	
		
			
	//-->
