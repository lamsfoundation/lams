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
		
		function openSysAdmin()
		{
			if(isMac)
			{
				adminWin = window.open('home.do?method=sysadmin&orgId="javascript:document.forms[0].orgIdForSysAdmin.value;"','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(adminWin && adminWin.open && !adminWin.closed )
				{
					adminWin.focus();
				}
				else
				{
					adminWin = window.open('home.do?method=admin&orgId="javascript:document.forms[0].orgIdForSysAdmin.value;"','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					adminWin.focus();
				}
			}
		}
		
		function openAuthor( )
		{
			//alert( "open author" );
			
			//cannot check if window is still open on macIE 5
			//<c:set value=document.forms[0].orgIdForAuthor.value var=orgIdForAuthor>
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
		
		function openStaff( )
		{
			//<c:set value=document.forms[0].orgIdForStaff.value var=orgIdForStaff>
			if(isMac)
			{
				if(belowMinRes)
				{
					teachWin = window.open('home.do?method=staff','tWindow','width=796,height=575,resizable,scrollbars');
				}
				else
				{
					teachWin = window.open('home.do?method=staff','tWindow','width=779,height=575');
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
					teachWin = window.open('home.do?method=staff','tWindow','width=779,height=575,resizable');
				}
			}
		}
		
		function openLearner()
		{
			//<c:set value=document.forms[0].orgIdForLearner.value var=orgIdForLearner>
			if(isMac)
			{
				learnWin = window.open('home.do?method=learner','lWindow','width=796,height=570,resizable,status=yes');
			}
			else
			{
				if(learnWin && learnWin.open && !learnWin.closed )
				{
					learnWin.focus();
				}
				else
				{
					learnWin = window.open('home.do?method=learner','lWindow','width=796,height=570,resizable,status=yes');
				}
			}
		}
		
		function openAdmin()
		{
			//<c:set value=document.forms[0].orgIdForAdmin.value var=orgIdForAdmin>
			if(isMac)
			{
				adminWin = window.open('home.do?method=admin','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(adminWin && adminWin.open && !adminWin.closed )
				{
					adminWin.focus();
				}
				else
				{
					adminWin = window.open('home.do?method=admin','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					adminWin.focus();
				}
			}
		}
			
	//-->
