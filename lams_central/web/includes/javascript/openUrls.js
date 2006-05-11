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
		var teachWin = null;
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
		
		function openStaff( orgId )
		{
			if(isMac)
			{
				if(belowMinRes)
				{
					teachWin = window.open('home.do?method=staff&orgId='+orgId,'tWindow','width=796,height=575,resizable,scrollbars');
				}
				else
				{
					// teachWin = window.open('home.do?method=staff&orgId='+orgId,'tWindow','width=779,height=575');
					teachWin = window.open('home.do?method=staff&orgId='+orgId,'tWindow','width=779,height=575,resizable,scrollbars');
				}
			}
			else
			{
				if(teachWin && teachWin.open && !teachWin.closed)
				{
					teachWin.focus();
				}
				else
				{
					// teachWin = window.open('home.do?method=staff&orgId='+orgId,'tWindow','width=779,height=575,resizable');
					teachWin = window.open('home.do?method=staff&orgId='+orgId,'tWindow','width=779,height=575,resizable,resizable,scrollbars');
				}
			}
		}
		
		function openLearner( orgId )
		{
			if(isMac)
			{
				learnWin = window.open('home.do?method=learner&orgId='+orgId,'lWindow','width=796,height=570,resizable,status=yes');
			}
			else
			{
				if(learnWin && learnWin.open && !learnWin.closed )
				{
					learnWin.focus();
				}
				else
				{
					learnWin = window.open('home.do?method=learner&orgId='+orgId,'lWindow','width=796,height=570,resizable,status=yes');
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
