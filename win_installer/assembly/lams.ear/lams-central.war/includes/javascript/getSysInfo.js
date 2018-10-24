	<!--
		var isWin = false;
		var isMac = false;

		function Its()
		{
			var n = navigator;
			// string comparisons are much easier if we lowercase everything now.
			// to make indexOf() tests more compact/readable, we prepend a space 
			// to the userAgent string (to get around '-1' indexOf() comparison)
			var ua = ' ' + n.userAgent.toLowerCase();
			var pl = n.platform.toLowerCase(); // not supported in NS3.0
			var an = n.appName.toLowerCase();
	
			// browser version
			this.version = n.appVersion;
			// debug
			//	document.writeln("ua="+ua+"<br/>");
			//	document.writeln("pl="+pl+"<br/>");
			//	document.writeln("an="+an+"<br/>");
	
			this.nn = ua.indexOf('mozilla') > 0;
	
			// 'compatible' versions of mozilla aren't navigator
			if(ua.indexOf('compatible') > 0)
			{
				this.nn = false;
			}
	
			this.opera = ua.indexOf('opera') > 0;
			this.webtv = ua.indexOf('webtv') > 0;
			this.ie = ua.indexOf('msie') > 0;
			this.aol = ua.indexOf('aol') > 0;
			this.omniweb = ua.indexOf('omniweb') > 0;
			this.galeon = ua.indexOf('galeon') > 0;
	
			this.major = parseInt( this.version );
			this.minor = parseFloat( this.version );
	
			// workarounds
			// - IE5 & 6 reports itself as version 4.0
			if(this.ie)
			{
				if(ua.indexOf("msie 5") > 0)
				{
					this.major = 5;
					var actual_index = ua.indexOf("msie 5");
					//alert("indexOf msie 5:"+actual_index);
					var actual_major = ua.substring(actual_index + 5, actual_index + 8);
					//alert("actual_major:"+actual_major);
					this.minor = parseFloat(actual_major);	
				}
				else if (ua.indexOf("msie 6") > 0)
				{
					this.major = 6;
					var actual_index = ua.indexOf("msie 6");
					//alert("indexOf msie 6:"+actual_index);
					var actual_major = ua.substring(actual_index + 5, actual_index + 8);
					//alert("actual_major:"+actual_major);
					this.minor = parseFloat(actual_major);
				}
			}
	
			this.screenheight=screen.height;
			this.screenwidth=screen.width;
			
			if (ua.indexOf("mac") != -1)
			{
				isMac = true;
			}
			
			if (ua.indexOf("win") != -1)
			{
				isWin = true;
			}
		
			this.platformDetected = "";
			
			if(ua.indexOf("16")  != -1  && isWin){
				this.platformDetected = "win16";
			}else if(ua.indexOf("95") != -1  && isWin){
				this.platformDetected = "win95";
			}else if(ua.indexOf("98")!= -1  && isWin){
				this.platformDetected = "win98";
			}else if(ua.indexOf("win 9x 4.90") != -1  && isWin){
				this.platformDetected = "winme";
			}else if(ua.indexOf("nt 5.1")!= -1  && isWin){
				this.platformDetected = "winxp";
			}else if(ua.indexOf("nt 5") != -1  && isWin){
				this.platformDetected = "win2000";
			}else if(ua.indexOf("nt") != -1  && isWin){
				this.platformDetected = "winnt";
			}else if((ua.indexOf("68k") != -1 || ua.indexOf("68000") != -1) && isMac){
				this.platformDetected = "mac68000";
			}else if((ua.indexOf("ppc") != -1 || ua.indexOf("powerpc") != -1) && isMac){
				this.platformDetected = "macppc";
			}else{
				this.platformDetected = "unknown";
			}
		
			//alert( "platform=" + this.platformDetected );
			
			  return this;
			}
	
			var its = new Its();
			/*for LAMS min spec is:
			*		Platform:	WIndows 	98 / NT4 / NT5 (2K) / XP
			*					Macintosh	PPC
			*		Browser:	Internet Explorer 5 +
			*		Flash:		6,0,47,0 (Version 6 Relase 47)
			*/
		//-->
