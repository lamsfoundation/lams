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
		
		function openAuthor( )
		{
			//alert( "open author" );
			
			//cannot check if window is still open on macIE 5
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
					//parent.close();
				}
			}
		}
		
		function openStaff( )
		{
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
			if(isMac)
			{
				adminWin = window.open('admin/adminMenu.jsp','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
			}
			else
			{
				if(adminWin && adminWin.open && !adminWin.closed )
				{
					adminWin.focus();
				}
				else
				{
					adminWin = window.open('admin/adminMenu.jsp','adWindow','width=796,height=570,resizable,location,menubar,scrollbars,dependent,status,toolbar');
					adminWin.focus();
				}
			}
		}
			
	//-->
