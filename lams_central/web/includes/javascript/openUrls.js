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
		var adminWin = null;
		var sysadminWin = null;
		
		function openAuthor( orgId )
		{
			if(isMac)
			{
				authorWin = window.open('home.do?method=author&orgId='+orgId,'aWindow','width=796,height=570,resizable');
			}
			else
			{
				if(authorWin && authorWin.open && !authorWin.closed)
				{
					authorWin.focus();
				}
				else
				{
					authorWin = window.open('home.do?method=author&orgId='+orgId,'aWindow','width=796,height=570,resizable');
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
		
		function openSysadmin( orgId )
		{
			if(isMac)
			{
				sysadminWin = window.open('home.do?method=sysadmin&orgId='+orgId,'saWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(sysadminWin && sysadminWin.open && !sysadminWin.closed )
				{
					sysadminWin.focus();
				}
				else
				{
					sysadminWin = window.open('home.do?method=sysadmin&orgId='+orgId,'saWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					sysadminWin.focus();
				}
			}
		}
			
		
			
	//-->
