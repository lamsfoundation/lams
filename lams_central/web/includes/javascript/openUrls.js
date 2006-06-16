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
		var dmWin = null;
		var sysadminWin = null;
		var omWin = null;
		var pWin = null
		
		function openProfile(username)
		{
			if(isMac)
			{
				pWin = window.open('profile.do?user='+username,'pWindow','width=796,height=570,resizable');
			}
			else
			{
				if(pWin && pWin.open && !pWin.closed)
				{
					pWin.focus();
				}
				else
				{
					pWin = window.open('profile.do?user='+username,'pWindow','width=796,height=570,resizable');
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
					authorWin.focus();
				}
				else
				{
					authorWin = window.open('home.do?method=author','aWindow','width=796,height=570,resizable');
					authorWin.focus();
				}
			}
		}

		function openDummyMonitor()
		{
			if(isMac)
			{
				dmWin = window.open('monitoring/dummy.jsp','dmWindow','width=796,height=570,resizable');
			}
			else
			{
				if(dmWin && dmWin.open && !dmWin.closed)
				{
					dmWin.focus();
				}
				else
				{
					dmWin = window.open('monitoring/dummy.jsp','dmWindow','width=796,height=570,resizable');
					dmWin.focus();
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
					monitorLessonWin.focus();
				}
				else
				{
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
					learnWin.focus();
				}
				else
				{
					learnWin = window.open('home.do?method=learner&lessonID='+lessonId,'lWindow','width=796,height=570,resizable,status=yes');
				}
			}
		}
		
		function openSysadmin()
		{
			if(isMac)
			{
				sysadminWin = window.open('admin/sysadminstart.do','saWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(sysadminWin && sysadminWin.open && !sysadminWin.closed )
				{
					sysadminWin.focus();
				}
				else
				{
					sysadminWin = window.open('admin/sysadminstart.do','saWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					sysadminWin.focus();
				}
			}
		}
		
		function openOrgManagement(orgId)
		{
			var hight = 768;
			var width = 1024;
			if(screen){
				height = screen.availHeight;
				width = screen.availWidth;
			}
			if(isMac)
			{
				omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left=0,top=0,width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(omWin && omWin.open && !omWin.closed )
				{
					omWin.focus();
				}
				else
				{
					omWin = window.open('admin/orgmanage.do?org='+orgId,'omWindow','left=0,top=0,width='+width+',height='+height+',resizable,location,menubar,scrollbars,dependent,status,toolbar');
					omWin.focus();
				}
			}
		}	
		
			
	//-->
